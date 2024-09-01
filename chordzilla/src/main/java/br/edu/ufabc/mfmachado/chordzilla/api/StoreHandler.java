package br.edu.ufabc.mfmachado.chordzilla.api;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StoreHandler {

    public void store() {
        System.out.println("Storing data");
        Path directoryPath = Paths.get("./");

        try (Stream<Path> paths = Files.walk(directoryPath, 1)) {
            paths.filter(path -> !path.equals(directoryPath)) // Exclude the root directory itself
                    .forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
