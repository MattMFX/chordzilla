package br.edu.ufabc.mfmachado.chordzilla;

import br.edu.ufabc.mfmachado.chordzilla.core.GrpcServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.io.IOException;

@SpringBootApplication
public class ChordzillaApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(ChordzillaApplication.class, args);

        try {
            GrpcServer grpcServer = context.getBean(GrpcServer.class);
            grpcServer.start();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
	}

}
