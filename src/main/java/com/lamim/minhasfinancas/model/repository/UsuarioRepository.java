package com.lamim.minhasfinancas.model.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lamim.minhasfinancas.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
		
	boolean existsByEmail(String email);
	
	boolean existsByNome(String nome);
		
	Optional<Usuario> findByEmail(String email);

}
