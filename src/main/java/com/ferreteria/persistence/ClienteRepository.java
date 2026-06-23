package com.ferreteria.persistence;

import com.ferreteria.entity.Cliente;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
	
	boolean existsByDni(String dni);
	Cliente findByDni(String dni);
	
	//------------------------------------------
	List<Cliente> findByNombresContainingIgnoreCaseOrDniContainingAndEstado(String nombres, String dni, String estado);
	
	List<Cliente> findByEstado(String estado);

	Optional<Cliente> findByUsuarioUsername(String username);
	Cliente findByUsuarioId(Long usuarioId);
	

}

