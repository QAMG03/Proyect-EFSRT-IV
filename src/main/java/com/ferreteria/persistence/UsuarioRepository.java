package com.ferreteria.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ferreteria.entity.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
	Optional<Usuario> findByUsername(String username);
	
	@Modifying
	@Query("UPDATE Usuario u SET u.estado = :estado WHERE u.id = :id")
	void actualizarEstado(@Param("id") Long id, @Param("estado") String estado);
	
	Optional<Usuario> findById(Long id);
	
	@Query("SELECT u FROM Usuario u WHERE u.id = :id")
	 Optional<Usuario> buscarUsuarioPorId(@Param("id") Long id);
}
