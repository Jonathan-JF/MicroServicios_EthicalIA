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
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;

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

    @Operation(
        summary = "Obtener coherencia por ID (HATEOAS)",
        description = "Devuelve una coherencia con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coherencia encontrada con enlaces HATEOAS"),
        @ApiResponse(responseCode = "404", description = "Coherencia no encontrada")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Coherencia>> obtenerCoherenciaPorIdHateoas(@PathVariable Long id) {
        Optional<Coherencia> coherenciaOpt = coherenciaService.getCoherenciaById(id);
        if (coherenciaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Coherencia coherencia = coherenciaOpt.get();
        EntityModel<Coherencia> recurso = EntityModel.of(coherencia,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaController.class).obtenerCoherenciaPorIdHateoas(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaController.class).listarCoherenciasHateoas()).withRel("todas-las-coherencias"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaController.class).eliminarCoherencia(id)).withRel("eliminar")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(
        summary = "Listar todas las coherencias (HATEOAS)",
        description = "Devuelve una colección de coherencias con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de coherencias con enlaces HATEOAS"))
    @GetMapping("/hateoas")
    public CollectionModel<EntityModel<Coherencia>> listarCoherenciasHateoas() {
        List<EntityModel<Coherencia>> coherencias = coherenciaService.getAllCoherencias().stream()
                .map(coh -> EntityModel.of(coh,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaController.class).obtenerCoherenciaPorIdHateoas(coh.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaController.class).eliminarCoherencia(coh.getId())).withRel("eliminar")
                ))
                .toList();
        return CollectionModel.of(coherencias,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaController.class).listarCoherenciasHateoas()).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaController.class).guardarCoherenciaHateoas(null)).withRel("crear")
        );
    }

    @Operation(
        summary = "Guardar coherencia (HATEOAS)",
        description = "Guarda una coherencia y devuelve la ubicación del recurso creado con enlaces HATEOAS."
    )
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Coherencia guardada correctamente"))
    @PostMapping("/hateoas")
    public ResponseEntity<?> guardarCoherenciaHateoas(@RequestBody Coherencia coherencia) {
        Coherencia guardada = coherenciaService.saveCoherencia(coherencia);
        return ResponseEntity.created(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaController.class).obtenerCoherenciaPorIdHateoas(guardada.getId())).toUri()
        ).build();
    }

    @Operation(
        summary = "Eliminar coherencia por ID (HATEOAS)",
        description = "Elimina una coherencia por su ID y devuelve una respuesta vacía."
    )
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Coherencia eliminada correctamente"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarCoherencia(@PathVariable Long id) {
        coherenciaService.deleteCoherencia(id);
        return ResponseEntity.noContent().build();
    }
}