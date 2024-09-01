package br.edu.ufabc.mfmachado.codehub;

import br.edu.ufabc.mfmachado.codehub.entity.Host;
import br.edu.ufabc.mfmachado.codehub.usecase.JoinChord;
import br.edu.ufabc.mfmachado.codehub.usecase.LoadHostList;
import br.edu.ufabc.mfmachado.codehub.usecase.impl.DefaultJoinChordImpl;
import br.edu.ufabc.mfmachado.codehub.usecase.impl.DefaultLoadHostListImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class CodehubApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CodehubApplication.class, args);
	}

    @Override
    public void run(String... args) {
        System.out.println("Welcome to Codehub! ");

        LoadHostList loadHostList = new DefaultLoadHostListImpl();
        List<Host> hosts = loadHostList.loadHostList("./src/main/resources/hosts.json");

        JoinChord joinChord = new DefaultJoinChordImpl();
        joinChord.join(hosts);
    }
}
