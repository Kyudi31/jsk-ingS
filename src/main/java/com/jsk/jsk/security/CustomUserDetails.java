package com.jsk.jsk.security;

import com.jsk.jsk.entity.Usuario;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final Usuario usuario;

    public CustomUserDetails(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // asumimos que usuario.getRol().getNombre() devuelve "ADMIN" / "COORDINADOR" / ...
        String role = usuario.getRol().getNombre();
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return usuario.getPassword();
    }

    @Override
    public String getUsername() {
        return usuario.getDocumentoIdentidad();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return usuario.isActivo(); } // si quieres bloquear usa este campo
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return usuario.isActivo(); }

    // getter para acceder al usuario original si hace falta
    public Usuario getUsuario() {
        return usuario;
    }
}
