package Historial.Historial.controller;

import Historial.Historial.model.Historial;
import Historial.Historial.service.HistorialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Historial", description = "Operaciones sobre el historial de análisis")
@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    private HistorialService historialService;

    @Autowired
    public HistorialController(HistorialService historialService) {
        this.historialService = historialService;
    }

    // --- ENDPOINTS TRADICIONALES ---

    @Operation(summary = "Guardar historial")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Historial guardado correctamente"))
    @PostMapping
    public Historial guardarHistorial(@RequestBody Historial historial) {
        return historialService.save(historial);
    }

    @Operation(summary = "Listar todos los historiales")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de historiales"))
    @GetMapping
    public List<Historial> listarHistoriales() {
        return historialService.findAll();
    }

    @Operation(summary = "Buscar historial por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial encontrado"),
        @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    public Optional<Historial> obtenerHistorialPorId(@PathVariable Long id) {
        return historialService.findById(id);
    }

    @Operation(summary = "Eliminar historial por ID")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Historial eliminado"))
    @DeleteMapping("/{id}")
    public void eliminarHistorial(@PathVariable Long id) {
        historialService.deleteById(id);
    }

    // --- ENDPOINTS HATEOAS ---

    @Operation(summary = "Obtener historial por ID (HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Historial encontrado"),
        @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Historial>> obtenerHistorialPorIdHateoas(@PathVariable Long id) {
        Optional<Historial> historialOpt = historialService.findById(id);
        if (historialOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Historial historial = historialOpt.get();
        EntityModel<Historial> recurso = EntityModel.of(historial,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistorialController.class).obtenerHistorialPorIdHateoas(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistorialController.class).listarHistorialesHateoas()).withRel("todos-los-historiales"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistorialController.class).eliminarHistorialHateoas(id)).withRel("eliminar")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(summary = "Listar todos los historiales (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de historiales"))
    @GetMapping("/hateoas")
    public CollectionModel<EntityModel<Historial>> listarHistorialesHateoas() {
        List<EntityModel<Historial>> historiales = historialService.findAll().stream()
                .map(h -> EntityModel.of(h,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistorialController.class).obtenerHistorialPorIdHateoas(h.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistorialController.class).eliminarHistorialHateoas(h.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(historiales,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistorialController.class).listarHistorialesHateoas()).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistorialController.class).guardarHistorialHateoas(null)).withRel("crear")
        );
    }

    @Operation(summary = "Guardar historial (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Historial guardado correctamente"))
    @PostMapping("/hateoas")
    public ResponseEntity<?> guardarHistorialHateoas(@RequestBody Historial historial) {
        Historial guardado = historialService.save(historial);
        return ResponseEntity.created(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(HistorialController.class).obtenerHistorialPorIdHateoas(guardado.getId())).toUri()
        ).build();
    }

    @Operation(summary = "Eliminar historial por ID (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Historial eliminado"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarHistorialHateoas(@PathVariable Long id) {
        historialService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
