package eci.arsw.controller;

import org.springframework.web.bind.annotation.PostMapping;
import eci.arsw.model.Subastas;
import eci.arsw.repository.SubastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SubastasController {

    @Autowired
    private SubastaRepository subastaRepository;

    @GetMapping
    public List<Subastas> getAllSubastas() {
        return subastaRepository.findAll();
    }

    @PostMapping
    public Subastas createSubastas(@RequestBody Subastas subastas) {
        return subastaRepository.save(subastas);
    }


}
