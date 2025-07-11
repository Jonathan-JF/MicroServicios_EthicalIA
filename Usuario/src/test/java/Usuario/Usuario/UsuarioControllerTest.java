package Usuario.Usuario;

import Usuario.Usuario.controller.UsuarioController;
import Usuario.Usuario.model.Usuario;
import Usuario.Usuario.service.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Listar todos los usuarios")
    void listarUsuarios() throws Exception {
        Usuario u = new Usuario(1L, "correo@ejemplo.com", "Juan Pérez", "USER", "123456");
        Mockito.when(service.findAll()).thenReturn(List.of(u));

        mockMvc.perform(get("/api/v1/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].correo").value("correo@ejemplo.com"));
    }

    @Test
    @DisplayName("✅ Obtener usuario por ID")
    void obtenerPorId() throws Exception {
        Usuario u = new Usuario(2L, "otro@ejemplo.com", "Ana Ruiz", "ADMIN", "abcdef");
        Mockito.when(service.findById(2L)).thenReturn(Optional.of(u));

        mockMvc.perform(get("/api/v1/usuarios/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Ana Ruiz"));
    }

    @Test
    @DisplayName("❌ Obtener usuario por ID (no encontrado)")
    void obtenerPorIdNoEncontrado() throws Exception {
        Mockito.when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/usuarios/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Crear un nuevo usuario")
    void crearUsuario() throws Exception {
        Usuario nuevo = new Usuario(null, "nuevo@correo.com", "Nuevo Usuario", "USER", "clave123");
        Usuario guardado = new Usuario(3L, "nuevo@correo.com", "Nuevo Usuario", "USER", "clave123");
        Mockito.when(service.save(any(Usuario.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.correo").value("nuevo@correo.com"));
    }

    @Test
    @DisplayName("✅ Actualizar usuario existente")
    void actualizarUsuario() throws Exception {
        Usuario existente = new Usuario(4L, "actual@correo.com", "Actual Nombre", "USER", "clave");
        Usuario actualizado = new Usuario(4L, "actual@correo.com", "Nombre Actualizado", "ADMIN", "claveNueva");

        Mockito.when(service.findById(4L)).thenReturn(Optional.of(existente));
        Mockito.when(service.save(any(Usuario.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/usuarios/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nombre Actualizado"))
                .andExpect(jsonPath("$.rol").value("ADMIN"));
    }

    @Test
    @DisplayName("❌ Actualizar usuario no existente")
    void actualizarUsuarioNoExistente() throws Exception {
        Usuario actualizado = new Usuario(99L, "noexiste@correo.com", "No Existe", "USER", "clave");
        Mockito.when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/usuarios/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Eliminar usuario por ID")
    void eliminarUsuario() throws Exception {
        Mockito.when(service.deleteById(5L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/usuarios/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Eliminar usuario por ID (no encontrado)")
    void eliminarUsuarioNoEncontrado() throws Exception {
        Mockito.when(service.deleteById(100L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/usuarios/100"))
                .andExpect(status().isNotFound());
    }
}
