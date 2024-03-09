package eci.arsw.controller;

import org.springframework.web.bind.annotation.PostMapping;
import eci.arsw.model.Apuesta;
import eci.arsw.repository.ApuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ApuestaController {

    @Autowired
    private ApuestaRepository apuestaRepository;

    @GetMapping
    public List<Apuesta> getAllApuestas() {
        return apuestaRepository.findAll();
    }

    @PostMapping
    public Apuesta createApuesta(@RequestBody Apuesta apuesta) {
        return apuestaRepository.save(apuesta);
    }


}
