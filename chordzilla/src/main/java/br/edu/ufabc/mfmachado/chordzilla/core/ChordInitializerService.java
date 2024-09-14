package br.edu.ufabc.mfmachado.chordzilla.core;

import br.edu.ufabc.mfmachado.chordzilla.api.Client;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServerFactory;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.SecureHash;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
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
    private final String ip;
    private final Integer port;
    private final List<BindableService> services = List.of(
            new JoinChordServiceImpl(),
            new AcknowledgeJoinServiceImpl(),
            new NotifyPredecessorNewNodeServiceImpl(),
            new DisplayChordServiceImpl(),
            new LeaveChordServiceImpl(),
            new NotifyPredecessorNodeGoneServiceImpl(),
            new TransferDataServiceImpl(),
            new StoreDataServiceImpl(),
            new RetrieveDataServiceImpl(),
            new RetrieveDataNotFoundServiceImpl(),
            new RetrieveDataOkServiceImpl()
    );

    public ChordInitializerService(HashStrategy hashStrategy, String ip, Integer port) {
        if (Objects.isNull(hashStrategy)) {
            hashStrategy = new SecureHash();
        }

        if (Objects.isNull(ip) || Objects.isNull(port)) {
            ip = getIp();
            port = new Random().nextInt(1024, 49515);
        }

        this.hashStrategy = hashStrategy;
        this.ip = ip;
        this.port = port;
    }

    public void initialize() {
        SelfNode node = startChordNode();
        initialize(node);
        LOGGER.info("Started node with id {} at {}:{}!", node.id(), node.ip(), node.port());
    }

    private void initialize(Node node) {
        startGrpcServer(node.port(), services);
        startClient(hashStrategy);
    }

    private SelfNode startChordNode() {
        try {
            if (SelfNode.isInitialized()) {
                SelfNode selfNode = SelfNode.getInstance();
                LOGGER.info(
                        "Node already initialized. Running on host {}:{} with ID {}.",
                        selfNode.ip(),
                        selfNode.port(),
                        selfNode.id()
                );
                return SelfNode.getInstance();
            }

            BigInteger id = hashStrategy.hash(String.join(":", ip, port.toString()).getBytes());
            LOGGER.debug("Starting node...");
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
                        LOGGER.debug("Found IP Address: {}", inetAddress.getHostAddress());
                        ipList.add((Inet4Address) inetAddress);
                    }
                }
            }
            int i = new Random().nextInt(ipList.size());
            return ipList.get(i).getHostAddress();
        } catch (Exception e) {
            LOGGER.error("Error getting IP address: {}", e.getMessage(), e);
            throw new RuntimeException("Error getting IP address", e);
        }
    }
}
