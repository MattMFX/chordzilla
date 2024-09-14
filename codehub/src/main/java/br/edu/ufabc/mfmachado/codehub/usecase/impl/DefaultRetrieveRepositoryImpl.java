package br.edu.ufabc.mfmachado.codehub.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.handler.RetrieveHandler;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import br.edu.ufabc.mfmachado.codehub.usecase.RetrieveRepository;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import org.apache.logging.log4j.util.Strings;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class DefaultRetrieveRepositoryImpl implements RetrieveRepository {
    private RetrieveHandler retrieveHandler;
    @Override
    public void retrieve(String key, String directoryPath) {
        ManagedChannel channel = null;
        try {
            System.out.println("Retrieving repository");
            SelfNode selfNode = SelfNode.getInstance();
            channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(selfNode.ip(), selfNode.port()), InsecureChannelCredentials.create())
                    .maxInboundMessageSize(1024 * 1024 * 100)
                    .build();
            retrieveHandler = new RetrieveHandler(channel);

            String directoryName = key.concat("/");
            Path repository = Paths.get(directoryPath).toAbsolutePath().normalize();
            Map<String, Path> files = retrieveDirectory(directoryName, repository);

            while (!files.isEmpty()) {
                Map<String, Path> newFiles = new HashMap<>();
                List<String> retrievedFiles = new ArrayList<>();
                files.forEach((file, path) -> {
                    if (file.endsWith("/")) {
                        newFiles.putAll(retrieveDirectory(file, path));
                    } else {

                        retrieveFile(file, path);
                    }
                    retrievedFiles.add(file);
                });
                files.putAll(newFiles);
                retrievedFiles.forEach(files::remove);
            }
        } catch (Exception e) {
            System.err.println("Error while retrieving data: " + e.getMessage());
        } finally {
            if (channel != null) {
                channel.shutdown();
            }
        }
    }

    private Map<String, Path> retrieveDirectory(String directoryName, Path directoryPath) {
        try {
            System.out.println("Retrieving directory " + directoryName);
            Path directoryNamePath = Path.of(directoryName);
            Path repository = directoryPath.resolve(directoryNamePath.getFileName());
            Files.createDirectories(repository);

            Optional<byte[]> dataOptional = retrieveHandler.retrieve(directoryName);
            byte[] data = dataOptional.orElseThrow(() -> new RuntimeException("Directory not found"));
            String content = new String(data);
            List<String> files = Arrays.asList(content.split(","));

            Map<String, Path> map = new HashMap<>();
            files.stream().filter(Strings::isNotBlank).forEach(file -> map.put(file, repository));

            return map;
        } catch (Exception e) {
            System.err.println("Error while retrieving data: " + e.getMessage());
            throw new RuntimeException("Error while retrieving data", e);
        }
    }

    private void retrieveFile(String fileName, Path filePath) {
        try {
            System.out.println("Retrieving file " + fileName);

            //REMOVE
            if (Strings.isBlank(fileName)) {
                throw new IllegalArgumentException("File name cannot be blank");
            }
            //REMOVE

            Path fileNamePath = Path.of(fileName);
            Files.write(filePath.resolve(fileNamePath.getFileName()), retrieveHandler.retrieve(fileName).orElseThrow(() -> new RuntimeException("File not found")));
        } catch (Exception e) {
            System.err.println("Error while retrieving data: " + e.getMessage());
            throw new RuntimeException("Error while retrieving data", e);
        }
    }
}
