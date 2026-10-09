package com.jsk.jsk.service;

import com.jsk.jsk.entity.*;
import com.jsk.jsk.repository.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CoordinadorRepository coordinadorRepository;

    @Mock
    private ImpulsadorRepository impulsadorRepository;

    @Mock
    private RolRepository rolRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    @DisplayName("Debe crear coordinador exitosamente cuando el creador tiene rol ADMIN")
    void deberiaCrearCoordinadorExitosamente_CuandoCreadorEsAdmin() {
        // Arrange
        Rol rolAdmin = new Rol();
        rolAdmin.setNombre("ADMIN");

        Usuario admin = new Admin();
        admin.setRol(rolAdmin);

        Rol rolCoord = new Rol();
        rolCoord.setNombre("COORDINADOR");

        Coordinador nuevoCoord = new Coordinador();
        nuevoCoord.setPassword("password123");

        when(rolRepository.findByNombre("COORDINADOR")).thenReturn(Optional.of(rolCoord));
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(coordinadorRepository.save(any(Coordinador.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Coordinador resultado = usuarioService.crearCoordinador(admin, nuevoCoord);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isActivo());
        assertEquals(rolCoord, resultado.getRol());
        assertEquals("encodedPassword", resultado.getPassword());
        verify(coordinadorRepository, times(1)).save(nuevoCoord);
    }

    @Test
    @DisplayName("Debe lanzar excepción al crear coordinador si el creador no es ADMIN")
    void deberiaLanzarExcepcion_CuandoCreadorNoEsAdmin() {
        // Arrange
        Rol rolVendedor = new Rol();
        rolVendedor.setNombre("VENDEDOR");

        Usuario vendedor = new Vendedor();
        vendedor.setRol(rolVendedor);

        Coordinador nuevoCoord = new Coordinador();

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.crearCoordinador(vendedor, nuevoCoord);
        });

        assertEquals("Solo un ADMIN puede crear Coordinadores", exception.getMessage());
        verifyNoInteractions(coordinadorRepository);
    }

    @Test
    @DisplayName("Debe lanzar excepción al buscar por ID si el usuario no existe")
    void deberiaLanzarExcepcion_CuandoUsuarioNoExistePorId() {
        // Arrange
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.buscarPorId(99L);
        });

        assertEquals("Usuario no encontrado", exception.getMessage());
    }

    @Test
    @DisplayName("Debe lanzar excepción al cambiar estado si el usuario no fue creado por dicho coordinador")
    void deberiaLanzarExcepcion_CuandoCoordinadorNoAutorizadoCambiaEstado() {
        // Arrange
        Usuario u = new Vendedor();
        Coordinador creadorOriginal = new Coordinador();
        creadorOriginal.setId(10L);
        u.setCreatedBy(creadorOriginal);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(u));

        // Act & Assert: Intenta modificar un coordinador con id 20L
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            usuarioService.cambiarEstadoUsuario(1L, 20L, false);
        });

        assertEquals("No autorizado para modificar este usuario", exception.getMessage());
        verify(usuarioRepository, never()).save(any());
    }
}
