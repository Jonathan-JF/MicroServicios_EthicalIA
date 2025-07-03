package MC_EthicalIA.informe;

import MC_EthicalIA.informe.controller.informeController;
import MC_EthicalIA.informe.model.informe;
import MC_EthicalIA.informe.service.informeService;
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

@WebMvcTest(informeController.class)
class informeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private informeService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("✅ Listar todos los informes")
    void testListarInformes() throws Exception {
        informe inf = new informe(1L, 10L, "Resumen de prueba", "http://pdf.com/1", LocalDateTime.now());
        Mockito.when(service.findAll()).thenReturn(List.of(inf));

        mockMvc.perform(get("/api/v1/informes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].resumen").value("Resumen de prueba"));
    }

    @Test
    @DisplayName("✅ Obtener informe por ID")
    void testObtenerInformePorId() throws Exception {
        informe inf = new informe(1L, 10L, "Resumen de prueba", "http://pdf.com/1", LocalDateTime.now());
        Mockito.when(service.findById(1L)).thenReturn(Optional.of(inf));

        mockMvc.perform(get("/api/v1/informes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resumen").value("Resumen de prueba"));
    }

    @Test
    @DisplayName("❌ Obtener informe por ID (no encontrado)")
    void testObtenerInformePorIdNotFound() throws Exception {
        Mockito.when(service.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/informes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Crear un nuevo informe")
    void testCrearInforme() throws Exception {
        informe inf = new informe(null, 10L, "Nuevo resumen", "http://pdf.com/2", LocalDateTime.now());
        informe guardado = new informe(2L, 10L, "Nuevo resumen", "http://pdf.com/2", LocalDateTime.now());
        Mockito.when(service.save(any(informe.class))).thenReturn(guardado);

        mockMvc.perform(post("/api/v1/informes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inf)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.resumen").value("Nuevo resumen"));
    }

    @Test
    @DisplayName("✅ Eliminar informe por ID")
    void testEliminarInforme() throws Exception {
        Mockito.when(service.deleteById(1L)).thenReturn(true);

        mockMvc.perform(delete("/api/v1/informes/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("❌ Eliminar informe por ID (no encontrado)")
    void testEliminarInformeNotFound() throws Exception {
        Mockito.when(service.deleteById(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/v1/informes/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("✅ Obtener informe por ID (HATEOAS)")
    void testObtenerInformePorIdHateoas() throws Exception {
        informe inf = new informe(1L, 10L, "Resumen HATEOAS", "http://pdf.com/3", LocalDateTime.now());
        Mockito.when(service.findById(1L)).thenReturn(Optional.of(inf));

        mockMvc.perform(get("/api/v1/informes/hateoas/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resumen").value("Resumen HATEOAS"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }

    @Test
    @DisplayName("✅ Listar todos los informes (HATEOAS)")
    void testListarInformesHateoas() throws Exception {
        informe inf = new informe(1L, 10L, "Resumen HATEOAS", "http://pdf.com/3", LocalDateTime.now());
        Mockito.when(service.findAll()).thenReturn(List.of(inf));

        mockMvc.perform(get("/api/v1/informes/hateoas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.informeList[0].resumen").value("Resumen HATEOAS"))
                .andExpect(jsonPath("$._links.self.href").exists());
    }
}
