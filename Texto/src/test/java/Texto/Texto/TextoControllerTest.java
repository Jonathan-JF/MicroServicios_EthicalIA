package Texto.Texto;


import Texto.Texto.controller.TextoController;
import Texto.Texto.model.Texto;
import Texto.Texto.service.TextoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TextoController.class)
class TextoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TextoService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Listar todos los textos")
    void listarTextos() throws Exception {
        Texto t = new Texto(1L, "Texto de prueba", LocalDateTime.now());
        Mockito.when(service.findAll()).thenReturn(List.of(t));

        mockMvc.perform(get("/api/v1/textos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contenido").value("Texto de prueba"));
    }

    @Test
    @DisplayName("✅ Obtener texto por ID")
    void obtenerTextoPorId() throws Exception {
        Texto t = new Texto(2L, "Otro texto", LocalDateTime.now());
        Mockito.when(service.findById(2L)).thenReturn(Optional.of(t));

        mockMvc.perform(get("/api/v1/textos/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido").value("Otro texto"));
    }

    @Test
    @DisplayName("❌ Obtener texto por ID (no encontrado)")
    void obtenerTextoPorIdNoEncontrado() throws Exception {
        Mockito.when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/textos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Crear un nuevo texto")
    void crearTexto() throws Exception {
        Texto nuevo = new Texto(null, "Nuevo contenido", LocalDateTime.now());
        Texto guardado = new Texto(3L, "Nuevo contenido", nuevo.getFechaEnvio());

        Mockito.when(service.save(any(Texto.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/textos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.contenido").value("Nuevo contenido"));
    }

    @Test
    @DisplayName("✅ Actualizar texto existente")
    void actualizarTexto() throws Exception {
        Texto existente = new Texto(4L, "Antiguo", LocalDateTime.now());
        Texto actualizado = new Texto(4L, "Actualizado", LocalDateTime.now());

        Mockito.when(service.findById(4L)).thenReturn(Optional.of(existente));
        Mockito.when(service.save(any(Texto.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/textos/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido").value("Actualizado"));
    }

    @Test
    @DisplayName("❌ Actualizar texto no existente")
    void actualizarTextoNoExistente() throws Exception {
        Texto actualizado = new Texto(99L, "No existe", LocalDateTime.now());
        Mockito.when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/textos/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Eliminar texto por ID")
    void eliminarTexto() throws Exception {
        Mockito.when(service.deleteById(5L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/textos/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Eliminar texto por ID (no encontrado)")
    void eliminarTextoNoEncontrado() throws Exception {
        Mockito.when(service.deleteById(100L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/textos/100"))
                .andExpect(status().isNotFound());
    }
}
