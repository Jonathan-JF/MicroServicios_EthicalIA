package MC_EthicalIA.originalidad;

import MC_EthicalIA.originalidad.controller.OriginalidadController;
import MC_EthicalIA.originalidad.model.Originalidad;
import MC_EthicalIA.originalidad.service.OriginalidadService;

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

@WebMvcTest(OriginalidadController.class)
class originalidadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OriginalidadService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Listar todos los análisis de originalidad")
    void listarAnalisis() throws Exception {
        Originalidad o = new Originalidad(1L, "texto1234567", 15.5, "http://fuente.com", LocalDateTime.now());
        Mockito.when(service.findAll()).thenReturn(List.of(o));

        mockMvc.perform(get("/api/v1/originalidad"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("texto1234567"));
    }

    @Test
    @DisplayName("✅ Buscar análisis por texto exacto")
    void buscarPorTexto() throws Exception {
        Originalidad o = new Originalidad(2L, "textoABCDEF12", 30.0, "http://fuente2.com", LocalDateTime.now());
        Mockito.when(service.findByTexto("textoABCDEF12")).thenReturn(List.of(o));

        mockMvc.perform(get("/api/v1/originalidad/buscar?texto=textoABCDEF12"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("textoABCDEF12"));
    }

    @Test
    @DisplayName("✅ Buscar análisis con porcentaje de plagio mayor a un valor")
    void buscarPorPlagio() throws Exception {
        Originalidad o = new Originalidad(3L, "textoPLAGIO", 80.0, "http://fuente3.com", LocalDateTime.now());
        Mockito.when(service.findByPlagioMayorA(50.0)).thenReturn(List.of(o));

        mockMvc.perform(get("/api/v1/originalidad/plagio?min=50.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].porcentajePlagio").value(80.0));
    }

    @Test
    @DisplayName("✅ Búsqueda avanzada por plagio y palabra en texto")
    void buscarAvanzado() throws Exception {
        Originalidad o = new Originalidad(4L, "palabraClave", 60.0, "http://fuente4.com", LocalDateTime.now());
        Mockito.when(service.buscarPorPlagioYTexto(50.0, "palabra")).thenReturn(List.of(o));

        mockMvc.perform(get("/api/v1/originalidad/filtrado?limite=50.0&palabra=palabra"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("palabraClave"));
    }

    @Test
    @DisplayName("✅ Crear un nuevo análisis de originalidad")
    void crearAnalisis() throws Exception {
        Originalidad nuevo = new Originalidad(null, "nuevoTexto", 10.0, "http://fuente5.com", LocalDateTime.now());
        Originalidad guardado = new Originalidad(5L, "nuevoTexto", 10.0, "http://fuente5.com", LocalDateTime.now());
        Mockito.when(service.save(any(Originalidad.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/originalidad")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.texto").value("nuevoTexto"));
    }

    @Test
    @DisplayName("✅ Eliminar análisis de originalidad por ID")
    void eliminarAnalisis() throws Exception {
        Mockito.when(service.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/originalidad/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Eliminar análisis de originalidad por ID (no encontrado)")
    void eliminarAnalisisNoEncontrado() throws Exception {
        Mockito.when(service.deleteById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/originalidad/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Obtener análisis de originalidad por ID (HATEOAS)")
    void obtenerOriginalidadPorIdHateoas() throws Exception {
        Originalidad o = new Originalidad(1L, "textoHATEOAS", 55.0, "http://fuente.com", LocalDateTime.now());
        Mockito.when(service.findById(1L)).thenReturn(Optional.of(o));

        mockMvc.perform(get("/api/v1/originalidad/hateoas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("textoHATEOAS"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("✅ Listar todos los análisis de originalidad (HATEOAS)")
    void listarOriginalidadesHateoas() throws Exception {
        Originalidad o = new Originalidad(1L, "textoHATEOAS", 55.0, "http://fuente.com", LocalDateTime.now());
        Mockito.when(service.findAll()).thenReturn(List.of(o));

        mockMvc.perform(get("/api/v1/originalidad/hateoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.originalidadList[0].texto").value("textoHATEOAS"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}
