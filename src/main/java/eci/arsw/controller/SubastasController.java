package eci.arsw.controller;

import org.springframework.web.bind.annotation.PostMapping;
import eci.arsw.model.Subastas;
import eci.arsw.repository.SubastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class SubastasController {

    @Autowired
    private SubastaRepository subastaRepository;

    @GetMapping("/subastas")
    public List<Subastas> getAllSubastas() {
        return subastaRepository.findAll();
    }

    @PostMapping("/subastas")
    public Subastas createSubastas(@RequestBody Subastas subastas) {
        return subastaRepository.save(subastas);
    }


}
