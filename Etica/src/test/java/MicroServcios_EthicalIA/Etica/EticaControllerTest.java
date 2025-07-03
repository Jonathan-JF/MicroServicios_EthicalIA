package MicroServcios_EthicalIA.Etica;

import MicroServcios_EthicalIA.Etica.controller.EticaController;
import MicroServcios_EthicalIA.Etica.model.Etica;
import MicroServcios_EthicalIA.Etica.service.EticaService;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EticaController.class)
public class EticaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EticaService eticaService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Guardar análisis ético")
    void testSaveEtica() throws Exception {
        Etica etica = new Etica(1L, "Texto de prueba", true, false, "Observación", LocalDateTime.now());
        Mockito.when(eticaService.saveEtica(any(Etica.class))).thenReturn(etica);

        mockMvc.perform(post("/api/etica")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(etica)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("Texto de prueba"));
    }

    @Test
    @DisplayName("✅ Listar todos los análisis éticos")
    void testGetAllEticas() throws Exception {
        Etica etica = new Etica(1L, "Texto", true, false, "Obs", LocalDateTime.now());
        Mockito.when(eticaService.getAllEticas()).thenReturn(List.of(etica));

        mockMvc.perform(get("/api/etica"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Texto"));
    }

    @Test
    @DisplayName("✅ Buscar análisis ético por ID")
    void testGetEticaById() throws Exception {
        Etica etica = new Etica(1L, "Texto", true, false, "Obs", LocalDateTime.now());
        Mockito.when(eticaService.getEticaById(1L)).thenReturn(Optional.of(etica));

        mockMvc.perform(get("/api/etica/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.texto").value("Texto"));
    }

    @Test
    @DisplayName("✅ Eliminar análisis ético por ID")
    void testDeleteEtica() throws Exception {
        Mockito.doNothing().when(eticaService).deleteEtica(1L);

        mockMvc.perform(delete("/api/etica/1"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("✅ Buscar análisis éticos por texto")
    void testSearchEticas() throws Exception {
        Etica etica = new Etica(1L, "Texto especial", true, false, "Obs", LocalDateTime.now());
        Mockito.when(eticaService.searchEticasByTexto("especial")).thenReturn(List.of(etica));

        mockMvc.perform(get("/api/etica/buscar?texto=especial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Texto especial"));
    }

    @Test
    @DisplayName("✅ Obtener análisis éticos recientes")
    void testGetRecentEticas() throws Exception {
        Etica etica = new Etica(1L, "Reciente", true, false, "Obs", LocalDateTime.now());
        Mockito.when(eticaService.getRecentEticas()).thenReturn(List.of(etica));

        mockMvc.perform(get("/api/etica/recientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].texto").value("Reciente"));
    }

    @Test
    @DisplayName("✅ Filtrar análisis éticos por lenguaje inclusivo")
    void testFilterEticasByLenguajeInclusivo() throws Exception {
        Etica etica = new Etica(1L, "Inclusivo", true, false, "Obs", LocalDateTime.now());
        Mockito.when(eticaService.getEticasByLenguajeInclusivo(true)).thenReturn(List.of(etica));

        mockMvc.perform(get("/api/etica/filtro?lenguajeInclusivo=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lenguajeInclusivo").value(true));
    }

    @Test
    @DisplayName("✅ Filtrar análisis éticos por lenguaje ofensivo")
    void testFilterEticasByLenguajeOfensivo() throws Exception {
        Etica etica = new Etica(1L, "Ofensivo", false, true, "Obs", LocalDateTime.now());
        Mockito.when(eticaService.getEticasByLenguajeOfensivo(true)).thenReturn(List.of(etica));

        mockMvc.perform(get("/api/etica/filtro?lenguajeOfensivo=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lenguajeOfensivo").value(true));
    }

    @Test
    @DisplayName("✅ Filtrar análisis éticos por inclusivo y ofensivo")
    void testFilterEticasByInclusivoAndOfensivo() throws Exception {
        Etica etica = new Etica(1L, "Ambos", true, true, "Obs", LocalDateTime.now());
        Mockito.when(eticaService.getEticasByLenguajeInclusivoAndOfensivo(true, true)).thenReturn(List.of(etica));

        mockMvc.perform(get("/api/etica/filtro?lenguajeInclusivo=true&lenguajeOfensivo=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lenguajeInclusivo").value(true))
                .andExpect(jsonPath("$[0].lenguajeOfensivo").value(true));
    }
}