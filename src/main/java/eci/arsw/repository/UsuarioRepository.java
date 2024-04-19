package eci.arsw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import eci.arsw.model.Usuario;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findTopByOrderByIdDesc();
    // Puedes agregar consultas personalizadas si es necesario
}
