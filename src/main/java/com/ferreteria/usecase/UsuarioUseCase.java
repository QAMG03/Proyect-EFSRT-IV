package com.ferreteria.usecase;

import com.ferreteria.entity.Usuario;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface UsuarioUseCase {
	 List<Usuario> listarUsuarios();
	 Optional<Usuario> obtenerUsuarioPorId(Long id);
	 void actualizarUsuario(Usuario usuario);
	 void eliminarUsuario(Long id);
	 void cambiarEstado(Long id);
	 
	 Optional<Usuario> buscarUsuarioPorId(Long id);
	 
}
