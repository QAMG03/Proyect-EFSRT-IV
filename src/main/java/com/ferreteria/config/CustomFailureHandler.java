package com.ferreteria.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class CustomFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    AuthenticationException exception)
	        throws IOException, ServletException {

	    String mensaje;

	    Throwable causa = exception;
	    while (causa.getCause() != null) {
	        causa = causa.getCause();
	    }

	    if (causa instanceof UsernameNotFoundException) {
	        mensaje = "El usuario no existe";
	    } else if (causa instanceof DisabledException) {
	        mensaje = "Tu cuenta está inactiva. Contacta con el administrador.";
	    } else if (causa instanceof BadCredentialsException) {
	        mensaje = "Contraseña incorrecta";
	    } else {
	        mensaje = causa.getMessage(); 
	    }

	    String mensajeCodificado = URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
	    response.sendRedirect("/login?errorMsg=" + mensajeCodificado);
	}
}
