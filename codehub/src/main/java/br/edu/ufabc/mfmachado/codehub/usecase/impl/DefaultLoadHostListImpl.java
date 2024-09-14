package br.edu.ufabc.mfmachado.codehub.usecase.impl;

import br.edu.ufabc.mfmachado.codehub.entity.Host;
import br.edu.ufabc.mfmachado.codehub.entity.KnownHosts;
import br.edu.ufabc.mfmachado.codehub.usecase.LoadHostList;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class DefaultLoadHostListImpl implements LoadHostList {
    @Override
    public List<Host> load(String filePath) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            File jsonFile = new File(filePath);
            KnownHosts knownHosts = objectMapper.readValue(jsonFile, KnownHosts.class);

            System.out.println("IP: " + knownHosts.knownHosts().getFirst().ip());
            System.out.println("Port: " + knownHosts.knownHosts().getFirst().port());

            return knownHosts.knownHosts();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
