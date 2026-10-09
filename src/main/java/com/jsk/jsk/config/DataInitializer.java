package com.jsk.jsk.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.jsk.jsk.entity.*;
import com.jsk.jsk.entity.enums.Empresa;
import com.jsk.jsk.repository.RolRepository;
import com.jsk.jsk.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class DataInitializer implements CommandLineRunner {
    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String[] roles = { "ADMIN", "COORDINADOR", "IMPULSADOR", "VENDEDOR" };
        for (String r : roles) {
            rolRepository.findByNombre(r)
                    .orElseGet(() -> {
                        Rol nuevo = new Rol();
                        nuevo.setNombre(r);
                        return rolRepository.save(nuevo);
                    });
        }
        if (usuarioRepository.findByDocumentoIdentidad("001").isEmpty()) {
            Rol adminRol = rolRepository.findByNombre("ADMIN").get();

            Admin admin = new Admin();
            admin.setDocumentoIdentidad("001");
            admin.setNombre("admin");
            admin.setPassword(passwordEncoder.encode("root"));
            admin.setRol(adminRol);
            admin.setActivo(true);
            admin.setEmpresa(Empresa.JSK);
            usuarioRepository.save(admin);

        }
    }

}
