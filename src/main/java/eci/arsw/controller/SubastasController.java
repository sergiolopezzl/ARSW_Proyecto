package eci.arsw.controller;

import org.springframework.web.bind.annotation.PostMapping;
import eci.arsw.model.Subastas;
import eci.arsw.repository.ApuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SubastasController {

    @Autowired
    private ApuestaRepository apuestaRepository;

    @GetMapping
    public List<Subastas> getAllApuestas() {
        return apuestaRepository.findAll();
    }

    @PostMapping
    public Subastas createApuesta(@RequestBody Subastas apuesta) {
        return apuestaRepository.save(apuesta);
    }


}
