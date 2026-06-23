package com.ferreteria.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import org.springframework.security.core.*;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, 
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        String redirectURL = request.getContextPath();

        
        var authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            String rol = authority.getAuthority();
            if (rol.equals("ROLE_ADMIN")) {
                redirectURL += "/admin-home";  
                break;
            } else if (rol.equals("ROLE_USER")) {
                redirectURL += "/user-home";  
                break;
            }
        }
        response.sendRedirect(redirectURL);
    }
}

