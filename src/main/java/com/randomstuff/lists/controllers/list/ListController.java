package com.randomstuff.lists.controllers.list;

import com.randomstuff.lists.entities.Lista;
import com.randomstuff.lists.repositories.ListaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/list")
public class ListController {

    private final ListaRepository listaRepository;

    @PostMapping("/shuffle")
    @CrossOrigin(origins = "http://localhost:5500")
    public ResponseEntity<List<String>> getShuffledList(@RequestBody List<String> items) {
        Collections.shuffle(items);
        return ResponseEntity.ok().body(items);
    }

    @PostMapping
    @CrossOrigin(origins = "http://localhost:5500")
    public ResponseEntity<Lista> getShuffledList(@RequestBody Lista lista) {
        lista = listaRepository.save(lista);
        return ResponseEntity.ok().body(lista);
    }

}
