package ReporteGlobal.ReporteGlobal;

import ReporteGlobal.ReporteGlobal.controller.ReporteGlobalController;
import ReporteGlobal.ReporteGlobal.model.ReporteGlobal;
import ReporteGlobal.ReporteGlobal.service.ReporteGlobalService;

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

@WebMvcTest(ReporteGlobalController.class)
class ReporteGlobalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReporteGlobalService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Listar todos los reportes globales")
    void testListarReportes() throws Exception {
        ReporteGlobal rep = new ReporteGlobal(1L, 100, 2, LocalDateTime.now(), "Observación de prueba");
        Mockito.when(service.findAll()).thenReturn(List.of(rep));

        mockMvc.perform(get("/api/v1/reportes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cantidadAnalisis").value(100));
    }

    @Test
    @DisplayName("✅ Obtener reporte global por ID")
    void testObtenerReportePorId() throws Exception {
        ReporteGlobal rep = new ReporteGlobal(1L, 100, 2, LocalDateTime.now(), "Observación de prueba");
        Mockito.when(service.findById(1L)).thenReturn(Optional.of(rep));

        mockMvc.perform(get("/api/v1/reportes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidadAnalisis").value(100));
    }

    @Test
    @DisplayName("❌ Obtener reporte global por ID (no encontrado)")
    void testObtenerReportePorIdNotFound() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/reportes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Crear un nuevo reporte global")
    void testCrearReporte() throws Exception {
        ReporteGlobal nuevo = new ReporteGlobal(null, 200, 0, LocalDateTime.now(), "Nuevo reporte");
        ReporteGlobal guardado = new ReporteGlobal(2L, 200, 0, LocalDateTime.now(), "Nuevo reporte");
        Mockito.when(service.save(any(ReporteGlobal.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/reportes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.cantidadAnalisis").value(200));
    }

    @Test
    @DisplayName("✅ Eliminar reporte global por ID")
    void testEliminarReporte() throws Exception {
        Mockito.when(service.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/reportes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Eliminar reporte global por ID (no encontrado)")
    void testEliminarReporteNotFound() throws Exception {
        Mockito.when(service.deleteById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/reportes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Obtener reporte global por ID (HATEOAS)")
    void testObtenerReportePorIdHateoas() throws Exception {
        ReporteGlobal rep = new ReporteGlobal(1L, 100, 2, LocalDateTime.now(), "Observación HATEOAS");
        Mockito.when(service.findById(1L)).thenReturn(Optional.of(rep));

        mockMvc.perform(get("/api/v1/reportes/hateoas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidadAnalisis").value(100))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("✅ Listar todos los reportes globales (HATEOAS)")
    void testListarReportesHateoas() throws Exception {
        ReporteGlobal rep = new ReporteGlobal(1L, 100, 2, LocalDateTime.now(), "Observación HATEOAS");
        Mockito.when(service.findAll()).thenReturn(List.of(rep));

        mockMvc.perform(get("/api/v1/reportes/hateoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.reporteGlobalList[0].cantidadAnalisis").value(100))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}
