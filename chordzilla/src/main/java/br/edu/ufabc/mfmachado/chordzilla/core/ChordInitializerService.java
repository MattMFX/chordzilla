package br.edu.ufabc.mfmachado.chordzilla.core;

import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServerFactory;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl.AcknowledgeJoinServiceImpl;
import br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl.JoinChordServiceImpl;
import br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl.NotifyPredecessorNewNodeServiceImpl;
import io.grpc.BindableService;

import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.*;

public class ChordInitializerService {

    private final HashStrategy hashStrategy;
    private final List<BindableService> services = List.of(
            new JoinChordServiceImpl(),
            new AcknowledgeJoinServiceImpl(),
            new NotifyPredecessorNewNodeServiceImpl()
    );

    public ChordInitializerService(HashStrategy hashStrategy) {
        this.hashStrategy = hashStrategy;
    }

    public void initialize() throws InterruptedException {
        SelfNode node = startChordNode();
        startGrpcServer(node.port(), services);
    }

    private SelfNode startChordNode() {
        BigInteger id = hashStrategy.hash(UUID.randomUUID().toString().getBytes());
        String ip = getIp();
        Integer port = new Random().nextInt(1024, 49515);
        System.out.println("Starting node with id " + id + " at " + ip + ":" + port);
        return SelfNode.init(id, ip, port);
    }

    private void startGrpcServer(int port, List<BindableService> services) throws InterruptedException {
        GrpcServer grpcServer = GrpcServerFactory.newFactory()
                .withServices(services)
                .withPort(port)
                .create();
        grpcServer.startAsync();
    }

    /** Código gerado pelo ChatGPT e adaptado
     *
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
