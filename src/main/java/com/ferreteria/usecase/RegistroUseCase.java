package com.ferreteria.usecase;

import org.springframework.stereotype.Component;

import com.ferreteria.entity.Usuario;

@Component
public interface RegistroUseCase {
	
	void registrarUsuario(Usuario usuario);
}
