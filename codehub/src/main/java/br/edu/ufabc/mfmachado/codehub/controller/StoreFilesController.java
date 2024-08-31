package br.edu.ufabc.mfmachado.codehub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StoreFilesController {

    @PostMapping
    public ResponseEntity<Void> storeFiles() {
        return ResponseEntity.ok().build();
    }
}
