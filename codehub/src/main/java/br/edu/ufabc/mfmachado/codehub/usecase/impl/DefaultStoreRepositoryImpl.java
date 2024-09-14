package br.edu.ufabc.mfmachado.codehub.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.handler.StoreHandler;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import br.edu.ufabc.mfmachado.codehub.usecase.StoreRepository;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DefaultStoreRepositoryImpl implements StoreRepository {
    private StoreHandler storeHandler;

    @Override
    public void store(String directoryPath) {
        SelfNode selfNode = SelfNode.getInstance();
        ManagedChannel channel = Grpc
                .newChannelBuilder(NodeUtils.getNodeAdress(selfNode.ip(), selfNode.port()), InsecureChannelCredentials.create())
                .maxInboundMessageSize(1024 * 1024 * 100)
                .build();
        storeHandler = new StoreHandler(channel);

        Path repository = Paths.get(directoryPath).toAbsolutePath().normalize();
        String repositoryName = repository.getFileName().toString().concat("/");
        System.out.println("Uploading repository " + repositoryName);
        Map<String, Path> map = storeDirectory(repositoryName, repository);

        while (!map.keySet().isEmpty()) {
            Map<String, Path> newMap = new HashMap<>();
            List<String> processedKeys = new ArrayList<>();
            map.forEach((key, value) -> {
                if (Files.isDirectory(value)) {
                    newMap.putAll(storeDirectory(key, value));
                } else {
                    storeFile(key, value);
                }
                processedKeys.add(key);
            });

            processedKeys.forEach(map::remove);
            map.putAll(newMap);
        }

        channel.shutdown();
    }

    private Map<String, Path> storeDirectory(String repositoryName, Path repositoryPath) {
        try {
            System.out.println("Storing directory " + repositoryName);

            Map<String, Path> artifacts = new HashMap<>();
            Stream<Path> paths = Files.list(repositoryPath);
            paths.forEach(path -> {
                String fileName = path.getFileName().toString();
                if (Files.isDirectory(path)) {
                    artifacts.put(repositoryName.concat(fileName).concat("/"), path);
                } else {
                    artifacts.put(repositoryName.concat(fileName), path);
                }
            });

            String files = String.join(",", artifacts.keySet());
            storeHandler.store(repositoryName, files.getBytes());
            return artifacts;
        } catch (Exception e) {
            System.err.println("Error while storing data: " + e.getMessage());
            throw new RuntimeException("Error while storing data", e);
        }
    }

    private void storeFile(String fileName, Path filePath) {
        System.out.println("Storing file " + fileName);
        try {
            byte[] fileContent = Files.readAllBytes(filePath);
            storeHandler.store(fileName, fileContent);
        } catch (Exception e) {
            System.err.println("Error while storing data: " + e.getMessage());
            throw new RuntimeException("Error while storing data", e);
        }
    }
}
