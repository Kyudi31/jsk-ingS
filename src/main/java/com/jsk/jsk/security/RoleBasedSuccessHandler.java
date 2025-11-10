package com.jsk.jsk.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class RoleBasedSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        String redirectUrl = "/"; // fallback

        for (GrantedAuthority auth : authentication.getAuthorities()) {
            String role = auth.getAuthority();
            switch (role) {
                case "ADMIN" -> redirectUrl = "/admin/dashboard";
                case "COORDINADOR" -> redirectUrl = "/coordinador/dashboard";
                case "IMPULSADOR" -> redirectUrl = "/impulsador/dashboard";
                case "VENDEDOR" -> redirectUrl = "/vendedor/dashboard";
            }
        }

        response.sendRedirect(redirectUrl);
    }
}
