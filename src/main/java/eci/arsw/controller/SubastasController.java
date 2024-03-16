package eci.arsw.controller;

import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import eci.arsw.model.Subastas;
import eci.arsw.repository.SubastaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PutMapping("/subastas/{id}")
    public ResponseEntity<Subastas> updateSubastas(@PathVariable(value = "id") Long subastaId,
                                                   @RequestBody Subastas subastaDetails) {
        Subastas subasta = subastaRepository.findById(subastaId)
                .orElseThrow(() -> new ResourceNotFoundException("Subasta not found for this id :: " + subastaId));

        subasta.setId(subastaDetails.getId()); // Reemplazar "campo1" con los nombres de los campos de tu entidad Subastas
        subasta.setNombre(subastaDetails.getNombre());
        subasta.setDescripcion(subastaDetails.getDescripcion());
        subasta.setUsuario(subastaDetails.getUsuario());
        subasta.setCantidad(subastaDetails.getCantidad());
        subasta.setEvento(subastaDetails.getEvento());
        subasta.setPrecioInicial(subastaDetails.getPrecioInicial());
        subasta.setPrecioActual(subastaDetails.getPrecioActual());
        subasta.setActiva(subastaDetails.getActiva());

        // Continuar para los demás campos

        final Subastas updatedSubasta = subastaRepository.save(subasta);
        return ResponseEntity.ok(updatedSubasta);
    }

    @DeleteMapping("/subastas/{id}")
    public Map<String, Boolean> deleteSubastas(@PathVariable(value = "id") Long subastaId)
            throws ResourceNotFoundException {
        Subastas subasta = subastaRepository.findById(subastaId)
                .orElseThrow(() -> new ResourceNotFoundException("Subasta not found for this id :: " + subastaId));

        subastaRepository.delete(subasta);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}
