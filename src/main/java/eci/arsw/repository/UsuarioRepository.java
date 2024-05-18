package eci.arsw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import eci.arsw.model.User;

@Repository
public interface UsuarioRepository extends JpaRepository<User, Integer> {

	User findByUsername(String username);

	User findByUsernameAndPassword(String username, String password);

}
