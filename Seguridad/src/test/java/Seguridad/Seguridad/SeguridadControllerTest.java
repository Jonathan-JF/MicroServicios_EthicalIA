package Seguridad.Seguridad;

import Seguridad.Seguridad.controller.SeguridadController;
import Seguridad.Seguridad.model.Seguridad;
import Seguridad.Seguridad.service.SeguridadService;
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

@WebMvcTest(SeguridadController.class)
class SeguridadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SeguridadService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Listar todos los registros de seguridad")
    void listarSeguridad() throws Exception {
        Seguridad s = new Seguridad(1L, "abc123", LocalDateTime.now());
        Mockito.when(service.findAll()).thenReturn(List.of(s));

        mockMvc.perform(get("/api/v1/seguridad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].contenidoEncriptado").value("abc123"));
    }

    @Test
    @DisplayName("✅ Obtener registro por ID")
    void obtenerPorId() throws Exception {
        Seguridad s = new Seguridad(2L, "xyz456", LocalDateTime.now());
        Mockito.when(service.findById(2L)).thenReturn(Optional.of(s));

        mockMvc.perform(get("/api/v1/seguridad/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenidoEncriptado").value("xyz456"));
    }

    @Test
    @DisplayName("❌ Obtener registro por ID (no encontrado)")
    void obtenerPorIdNoEncontrado() throws Exception {
        Mockito.when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/seguridad/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Crear nuevo registro de seguridad")
    void crearSeguridad() throws Exception {
        Seguridad nuevo = new Seguridad(null, "nuevoTexto", LocalDateTime.now());
        Seguridad guardado = new Seguridad(3L, "nuevoTexto", nuevo.getFechaTransito());

        Mockito.when(service.save(any(Seguridad.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/seguridad")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3))
                .andExpect(jsonPath("$.contenidoEncriptado").value("nuevoTexto"));
    }

    @Test
    @DisplayName("✅ Actualizar registro existente")
    void actualizarSeguridad() throws Exception {
        Seguridad existente = new Seguridad(4L, "viejo", LocalDateTime.now());
        Seguridad actualizado = new Seguridad(4L, "nuevoValor", LocalDateTime.now());

        Mockito.when(service.findById(4L)).thenReturn(Optional.of(existente));
        Mockito.when(service.save(any(Seguridad.class))).thenReturn(actualizado);

        mockMvc.perform(put("/api/v1/seguridad/4")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenidoEncriptado").value("nuevoValor"));
    }

    @Test
    @DisplayName("❌ Actualizar registro no existente")
    void actualizarNoExistente() throws Exception {
        Seguridad actualizado = new Seguridad(99L, "no-existe", LocalDateTime.now());
        Mockito.when(service.findById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/seguridad/99")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actualizado)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Eliminar registro por ID")
    void eliminarSeguridad() throws Exception {
        Mockito.when(service.deleteById(5L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/seguridad/5"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Eliminar registro por ID (no encontrado)")
    void eliminarNoEncontrado() throws Exception {
        Mockito.when(service.deleteById(100L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/seguridad/100"))
                .andExpect(status().isNotFound());
    }
}
