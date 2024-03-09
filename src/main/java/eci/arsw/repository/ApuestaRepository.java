package eci.arsw.repository;

import eci.arsw.model.Apuesta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApuestaRepository extends JpaRepository<Apuesta, Long> {
    // puedes agregar métodos de consulta personalizados aquí si es necesario
}
