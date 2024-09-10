package br.edu.ufabc.mfmachado.chordzilla.core;

import br.edu.ufabc.mfmachado.chordzilla.api.Client;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServerFactory;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl.*;
import io.grpc.BindableService;
import org.checkerframework.checker.index.qual.LengthOf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class ChordInitializerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChordInitializerService.class);

    private final HashStrategy hashStrategy;
    private final List<BindableService> services = List.of(
            new JoinChordServiceImpl(),
            new AcknowledgeJoinServiceImpl(),
            new NotifyPredecessorNewNodeServiceImpl(),
            new DisplayChordServiceImpl(),
            new LeaveChordServiceImpl(),
            new NotifyPredecessorNodeGoneServiceImpl(),
            new TransferDataServiceImpl(),
            new StoreDataServiceImpl()
    );

    public ChordInitializerService(HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
    }

    public void initialize() {
        SelfNode node = startChordNode();
        startGrpcServer(node.port(), services);
        startClient(hashStrategy);
    }

    private SelfNode startChordNode() {
        try {
            BigInteger id = hashStrategy.hash(UUID.randomUUID().toString().getBytes());
            String ip = getIp();
            Integer port = new Random().nextInt(1024, 49515);
            LOGGER.info("Starting node with id {} at {}:{}", id, ip, port);
            return SelfNode.init(id, ip, port);
        } catch (Exception e) {
            LOGGER.error("Error starting chord node: {}", e.getMessage(), e);
            throw new RuntimeException("Error starting chord node", e);
        }
    }

    private void startGrpcServer(int port, List<BindableService> services) {
        try {
            GrpcServer grpcServer = GrpcServerFactory.newFactory()
                    .withServices(services)
                    .withPort(port)
                    .create();
            grpcServer.startAsync();
        } catch (Exception e) {
            LOGGER.error("Error starting gRPC server: {}", e.getMessage(), e);
        }

    }

    private void startClient(HashStrategy hashStrategy) {
        Client.setHashStrategy(hashStrategy);
    }

    /** Código gerado pelo ChatGPT e adaptado
     */
    private String getIp() {
        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            List<Inet4Address> ipList = new ArrayList<>();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                for (InetAddress inetAddress : Collections.list(inetAddresses)) {
                    if (inetAddress instanceof Inet4Address && inetAddress.isLoopbackAddress()) {
                        System.out.println("Found IP Address: " + inetAddress.getHostAddress());
                        ipList.add((Inet4Address) inetAddress);
                    }
                }
            }
            int i = new Random().nextInt(ipList.size());
            return ipList.get(i).getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting IP address", e);
        }
    }
}
