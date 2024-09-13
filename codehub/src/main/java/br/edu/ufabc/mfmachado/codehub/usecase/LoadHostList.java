package br.edu.ufabc.mfmachado.codehub.usecase;

import br.edu.ufabc.mfmachado.codehub.entity.Host;

import java.util.List;

public interface LoadHostList {
    List<Host> load(String filePath);
}
