package br.edu.ufabc.mfmachado.codehub.usecase.impl;

import br.edu.ufabc.mfmachado.codehub.entity.Host;
import br.edu.ufabc.mfmachado.codehub.entity.KnownHosts;
import br.edu.ufabc.mfmachado.codehub.usecase.LoadHostList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DefaultLoadHostListImpl implements LoadHostList {
    @Override
    public List<Host> loadHostList(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(filePath);
            KnownHosts knownHosts = objectMapper.readValue(jsonFile, KnownHosts.class);

            System.out.println("IP: " + knownHosts.knownHosts().getFirst().ip());
            System.out.println("Port: " + knownHosts.knownHosts().getFirst().port());

            return knownHosts.knownHosts();
        } catch (IOException e) {
            e.printStackTrace(); // TODO Melhorar tratamento de exceção
            throw new RuntimeException(e);
        }
    }
}
