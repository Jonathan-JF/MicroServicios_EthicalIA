package MicroServcios_EthicalIA.Coherencia;

import MicroServcios_EthicalIA.Coherencia.controller.CoherenciaController;
import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import MicroServcios_EthicalIA.Coherencia.service.CoherenciaService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CoherenciaController.class)
public class CoherenciaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoherenciaService coherenciaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Guardar coherencia")
    void testSaveCoherencia() throws Exception {
        Coherencia coherencia = new Coherencia(null, "Texto de prueba", 80.0, "Sugerencia", LocalDateTime.now());
        Coherencia coherenciaGuardada = new Coherencia(1L, "Texto de prueba", 80.0, "Sugerencia", coherencia.getFechaAnalisis());

        when(coherenciaService.saveCoherencia(coherencia)).thenReturn(coherenciaGuardada);

        mockMvc.perform(post("/api/coherencia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coherencia)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.texto").value("Texto de prueba"));
    }

    @Test
    @DisplayName("✅ Listar coherencias")
    void testGetAllCoherencias() throws Exception {
        Coherencia coherencia = new Coherencia(1L, "Texto de prueba", 80.0, "Sugerencia", LocalDateTime.now());
        when(coherenciaService.getAllCoherencias()).thenReturn(List.of(coherencia));

        mockMvc.perform(get("/api/coherencia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Texto de prueba"));
    }

    @Test
    @DisplayName("✅ Buscar coherencia por ID")
    void testGetCoherenciaById() throws Exception {
        Coherencia coherencia = new Coherencia(1L, "Texto de prueba", 80.0, "Sugerencia", LocalDateTime.now());
        when(coherenciaService.getCoherenciaById(1L)).thenReturn(Optional.of(coherencia));

        mockMvc.perform(get("/api/coherencia/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("Texto de prueba"));
    }

    @Test
    @DisplayName("✅ Eliminar coherencia por ID")
    void testDeleteCoherencia() throws Exception {
        doNothing().when(coherenciaService).deleteCoherencia(1L);

        mockMvc.perform(delete("/api/coherencia/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("✅ Buscar coherencias por texto")
    void testSearchCoherencias() throws Exception {
        Coherencia coherencia = new Coherencia(1L, "Texto especial", 90.0, "Sugerencia", LocalDateTime.now());
        when(coherenciaService.searchCoherenciasByTexto("especial")).thenReturn(List.of(coherencia));

        mockMvc.perform(get("/api/coherencia/buscar?texto=especial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Texto especial"));
    }

    @Test
    @DisplayName("✅ Filtrar coherencias por puntuación")
    void testFilterCoherenciasByPuntuacion() throws Exception {
        Coherencia coherencia = new Coherencia(1L, "Texto", 95.0, "Sugerencia", LocalDateTime.now());
        when(coherenciaService.getCoherenciasByPuntuacion(95.0)).thenReturn(List.of(coherencia));

        mockMvc.perform(get("/api/coherencia/filtro?puntuacion=95.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].puntuacionCoherencia").value(95.0));
    }

    @Test
    @DisplayName("✅ Obtener coherencias recientes")
    void testGetRecentCoherencias() throws Exception {
        Coherencia coherencia = new Coherencia(1L, "Texto reciente", 88.0, "Sugerencia", LocalDateTime.now());
        when(coherenciaService.getRecentCoherencias()).thenReturn(List.of(coherencia));

        mockMvc.perform(get("/api/coherencia/recientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Texto reciente"));
    }
}

