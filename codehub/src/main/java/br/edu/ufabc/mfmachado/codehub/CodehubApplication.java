package br.edu.ufabc.mfmachado.codehub;

import br.edu.ufabc.mfmachado.codehub.entity.Host;
import br.edu.ufabc.mfmachado.codehub.usecase.JoinNetwork;
import br.edu.ufabc.mfmachado.codehub.usecase.LeaveNetwork;
import br.edu.ufabc.mfmachado.codehub.usecase.LoadHostList;
import br.edu.ufabc.mfmachado.codehub.usecase.RetrieveRepository;
import br.edu.ufabc.mfmachado.codehub.usecase.impl.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class CodehubApplication implements CommandLineRunner {

	public static void main(String[] args) {
        SpringApplication.run(CodehubApplication.class, args);
	}

    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Codehub! ");
        System.out.println("Type [1] to join a chord network or type [2] to create a new chord network.");
        System.out.print(">>> ");

        int option = readOption(scanner, List.of(1, 2));
        if (option == 1) {
            joinChordNetwork(scanner);
        } else {
            startChordNetwork(scanner);
        }

        interactChordNetwork(scanner);

        System.out.println("Goodbye!");
        System.exit(0);
    }

    private void startChordNetwork(Scanner scanner) {
        JoinNetwork joinNetwork = new DefaultJoinNetworkImpl();
        System.out.println("Type [1] if you wish to choose the IP and port or type [2] to start the network with a random host.");
        System.out.println(">>> ");
        int option = readOption(scanner, List.of(1, 2));

        if (option == 1) {
            System.out.println("Type the IP address of the host.");
            System.out.println(">>> ");
            String ip = scanner.next();

            System.out.println("Type the port of the host.");
            System.out.println(">>> ");
            int port = readInt(scanner);

            joinNetwork.newChord(ip, port);
        } else {
            joinNetwork.newChord();
        }
    }

    private void joinChordNetwork(Scanner scanner) {
        JoinNetwork joinNetwork = new DefaultJoinNetworkImpl();
        System.out.println("Type [1] to inform a specific known host or type [2] to use the known host list.");
        int option = readOption(scanner, List.of(1, 2));

        if (option == 1) {
            System.out.println("Type the IP address of the host.");
            System.out.println(">>> ");
            String ip = scanner.next();

            System.out.println("Type the port of the host.");
            System.out.println(">>> ");
            int port = readInt(scanner);

            joinNetwork.join(ip, port);
        } else {
            System.out.println("The archive src/main/resources/hosts.json will be used to search for a host.");
            LoadHostList loadHostList = new DefaultLoadHostListImpl();
            List<Host> hosts = loadHostList.load("src/main/resources/hosts.json");
            joinNetwork.join(hosts);
        }
    }

    private void interactChordNetwork(Scanner scanner) {
        String input;
        do {
            System.out.println("To store a new repository in the chord type [store], to retrive a repository type [retrieve]. If you wish to leave the network, type [leave].");
            System.out.print(">>> ");
            input = scanner.next();

            if (input.equals("store")) {
                System.out.println("Type the path to the repository you wish to upload (you can use relative or absolute paths).");
                System.out.print(">>> ");
                String path = scanner.next();
                DefaultStoreRepositoryImpl storeRepository = new DefaultStoreRepositoryImpl();
                storeRepository.store(path);
            } else if (input.equals("retrieve")) {
                System.out.println("Type the name of the repository you wish to download.");
                System.out.print(">>> ");
                String name = scanner.next();

                System.out.println("Type the path to the directory where the repository will be downloaded.");
                System.out.print(">>> ");
                String path = scanner.next();

                RetrieveRepository retrieveRepository = new DefaultRetrieveRepositoryImpl();
                retrieveRepository.retrieve(name, path);
            } else {
                System.out.println("Invalid option.");
            }
        } while (!input.equals("leave"));

        LeaveNetwork leaveNetwork = new DefaultLeaveNetworkImpl();
        leaveNetwork.leave();
    }

    private int readOption(Scanner scanner, List<Integer> validOptions) {
        int option = 0;
        for (boolean invalid = true; invalid;) {
            if (scanner.hasNextInt()) {
                invalid = false;
                option = scanner.nextInt();
                if (!validOptions.isEmpty() && !validOptions.contains(option)) {
                    System.out.println("Invalid option. Please type a valid integer.");
                }
            } else {
                System.out.println("Invalid option. Please type a valid integer.");
                scanner.next();
            }
        }

        return option;
    }

    private int readInt(Scanner scanner) {
        return readOption(scanner, Collections.emptyList());
    }
}
