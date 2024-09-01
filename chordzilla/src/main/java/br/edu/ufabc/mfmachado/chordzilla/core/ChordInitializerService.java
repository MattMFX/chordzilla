package br.edu.ufabc.mfmachado.chordzilla.core;

import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServerFactory;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl.JoinChordServiceImpl;
import io.grpc.BindableService;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ChordInitializerService {

    private final HashStrategy hashStrategy;
    private final List<BindableService> services;

    public ChordInitializerService(HashStrategy hashStrategy, List<BindableService> services) {
        this.hashStrategy = hashStrategy;
        this.services = services;
    }

    public void initialize() throws InterruptedException {
        SelfNode node = startChordNode();
        startGrpcServer(node.getPort());
    }

    private void startGrpcServer(int port) throws InterruptedException {
        GrpcServer grpcServer = GrpcServerFactory.newFactory().create();
        grpcServer.start(port);
    }

    private SelfNode startChordNode() {
        byte[] id = hashStrategy.hash(UUID.randomUUID().toString().getBytes());
        String ip = getIp();
        int port = new Random().nextInt(1024, 49515);
        return new SelfNode(id, ip, port);
    }

    // Código gerado pelo ChatGPT e adaptado
    private String getIp() {
        try {
            // Get the list of network interfaces
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();

            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = networkInterfaces.nextElement();

                // Print the name and display name of the network interface
                System.out.println("Interface Name: " + networkInterface.getName());
                System.out.println("Display Name: " + networkInterface.getDisplayName());

                // Print all IP addresses associated with this interface
                Enumeration<java.net.InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    java.net.InetAddress inetAddress = inetAddresses.nextElement();
                    System.out.println("IP Address: " + inetAddress.getHostAddress());
                }

                System.out.println();
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return "";
    }
}
