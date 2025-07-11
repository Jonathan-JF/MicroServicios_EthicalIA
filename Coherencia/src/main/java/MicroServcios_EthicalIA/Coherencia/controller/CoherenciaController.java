package MicroServcios_EthicalIA.Coherencia.controller;

import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import MicroServcios_EthicalIA.Coherencia.service.CoherenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/coherencia")
@Tag(name = "Coherencia", description = "Operaciones sobre coherencia")
public class CoherenciaController {

    @Autowired
    private CoherenciaService coherenciaService;

    @Operation(summary = "Guardar coherencia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coherencia guardada correctamente")
    })
    @PostMapping
    public Coherencia saveCoherencia(@RequestBody Coherencia coherencia) {
        return coherenciaService.saveCoherencia(coherencia);
    }

    @Operation(summary = "Listar todas las coherencias")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de coherencias")
    })
    @GetMapping
    public List<Coherencia> getAllCoherencias() {
        return coherenciaService.getAllCoherencias();
    }

    @Operation(summary = "Buscar coherencia por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coherencia encontrada"),
        @ApiResponse(responseCode = "404", description = "Coherencia no encontrada")
    })
    @GetMapping("/{id}")
    public Optional<Coherencia> getCoherenciaById(
            @Parameter(description = "ID de la coherencia a buscar") @PathVariable Long id) {
        return coherenciaService.getCoherenciaById(id);
    }

    @Operation(summary = "Eliminar coherencia por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Coherencia eliminada")
    })
    @DeleteMapping("/{id}")
    public void deleteCoherencia(
            @Parameter(description = "ID de la coherencia a eliminar") @PathVariable Long id) {
        coherenciaService.deleteCoherencia(id);
    }

    @Operation(summary = "Buscar coherencias por texto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de coherencias encontradas")
    })
    @GetMapping("/buscar")
    public List<Coherencia> searchCoherencias(
            @Parameter(description = "Texto a buscar") @RequestParam String texto) {
        return coherenciaService.searchCoherenciasByTexto(texto);
    }

    @Operation(summary = "Filtrar coherencias por puntuación o fechas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de coherencias filtradas")
    })
    @GetMapping("/filtro")
    public List<Coherencia> filterCoherencias(
            @Parameter(description = "Puntuación mínima") @RequestParam(required = false) Double puntuacion,
            @Parameter(description = "Fecha de inicio") @RequestParam(required = false) LocalDateTime fechaInicio,
            @Parameter(description = "Fecha de fin") @RequestParam(required = false) LocalDateTime fechaFin) {

        if (puntuacion != null) {
            return coherenciaService.getCoherenciasByPuntuacion(puntuacion);
        }
        if (fechaInicio != null && fechaFin != null) {
            return coherenciaService.getCoherenciasByFechaAnalisisBetween(fechaInicio, fechaFin);
        }
        return coherenciaService.getAllCoherencias();
    }

    @Operation(summary = "Obtener coherencias recientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de coherencias recientes")
    })
    @GetMapping("/recientes")
    public List<Coherencia> getRecentCoherencias() {
        return coherenciaService.getRecentCoherencias();
    }
}