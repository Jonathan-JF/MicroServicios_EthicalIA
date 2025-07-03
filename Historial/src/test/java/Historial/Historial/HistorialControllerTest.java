package Historial.Historial;

import Historial.Historial.model.Historial;
import Historial.Historial.service.HistorialService;
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

@WebMvcTest
class HistorialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistorialService historialService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Guardar historial")
    void testGuardarHistorial() throws Exception {
        Historial historial = new Historial(1L, 10L, LocalDateTime.now(), "desc", true);
        Mockito.when(historialService.save(any(Historial.class))).thenReturn(historial);

        mockMvc.perform(post("/api/v1/historial")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(historial)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("desc"));
    }

    @Test
    @DisplayName("✅ Listar todos los historiales")
    void testListarHistoriales() throws Exception {
        Historial historial = new Historial(1L, 10L, LocalDateTime.now(), "desc", true);
        Mockito.when(historialService.findAll()).thenReturn(List.of(historial));

        mockMvc.perform(get("/api/v1/historial"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].descripcion").value("desc"));
    }

    @Test
    @DisplayName("✅ Buscar historial por ID")
    void testObtenerHistorialPorId() throws Exception {
        Historial historial = new Historial(1L, 10L, LocalDateTime.now(), "desc", true);
        Mockito.when(historialService.findById(1L)).thenReturn(Optional.of(historial));

        mockMvc.perform(get("/api/v1/historial/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descripcion").value("desc"));
    }

    @Test
    @DisplayName("❌ Buscar historial por ID (no encontrado)")
    void testObtenerHistorialPorIdNotFound() throws Exception {
        Mockito.when(historialService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/historial/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Eliminar historial por ID")
    void testEliminarHistorial() throws Exception {
        Mockito.when(historialService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/historial/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Eliminar historial por ID (no encontrado)")
    void testEliminarHistorialNotFound() throws Exception {
        Mockito.when(historialService.deleteById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/historial/1"))
                .andExpect(status().isNotFound());
    }
}
