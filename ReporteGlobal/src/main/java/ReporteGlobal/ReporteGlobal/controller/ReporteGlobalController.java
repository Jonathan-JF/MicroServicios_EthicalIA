package ReporteGlobal.ReporteGlobal.controller;

import ReporteGlobal.ReporteGlobal.model.ReporteGlobal;
import ReporteGlobal.ReporteGlobal.service.ReporteGlobalService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Reporte Global", description = "Operaciones sobre reportes globales generados")
@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteGlobalController {

    private final ReporteGlobalService service;

    public ReporteGlobalController(ReporteGlobalService service) {
        this.service = service;
    }


    @Operation(summary = "Listar todos los reportes globales")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de reportes globales"))
    @GetMapping
    public ResponseEntity<List<ReporteGlobal>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener reporte global por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reporte global encontrado"),
        @ApiResponse(responseCode = "404", description = "Reporte global no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ReporteGlobal> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo reporte global")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Reporte global creado correctamente"))
    @PostMapping
    public ResponseEntity<ReporteGlobal> crear(@RequestBody ReporteGlobal nuevo) {
        return ResponseEntity.ok(service.save(nuevo));
    }

    @Operation(summary = "Actualizar un reporte global existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reporte global actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte global no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReporteGlobal> actualizar(@PathVariable Long id, @RequestBody ReporteGlobal actualizado) {
        return service.findById(id)
                .map(r -> {
                    actualizado.setId(id);
                    return ResponseEntity.ok(service.save(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar reporte global por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Reporte global eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Reporte global no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }


    @Operation(
        summary = "Obtener reporte global por ID (HATEOAS)",
        description = "Devuelve un reporte global con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Reporte global encontrado con enlaces HATEOAS"),
        @ApiResponse(responseCode = "404", description = "Reporte global no encontrado")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<ReporteGlobal>> obtenerReportePorIdHateoas(@PathVariable Long id) {
        Optional<ReporteGlobal> reporteOpt = service.findById(id);
        if (reporteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        ReporteGlobal reporte = reporteOpt.get();
        EntityModel<ReporteGlobal> recurso = EntityModel.of(reporte,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteGlobalController.class).obtenerReportePorIdHateoas(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteGlobalController.class).listarReportesHateoas()).withRel("todos-los-reportes"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteGlobalController.class).eliminarReporteHateoas(id)).withRel("eliminar")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(
        summary = "Listar todos los reportes globales (HATEOAS)",
        description = "Devuelve una colección de reportes globales con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de reportes globales con enlaces HATEOAS"))
    @GetMapping("/hateoas")
    public CollectionModel<EntityModel<ReporteGlobal>> listarReportesHateoas() {
        List<EntityModel<ReporteGlobal>> reportes = service.findAll().stream()
                .map(rep -> EntityModel.of(rep,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteGlobalController.class).obtenerReportePorIdHateoas(rep.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteGlobalController.class).eliminarReporteHateoas(rep.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(reportes,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteGlobalController.class).listarReportesHateoas()).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteGlobalController.class).guardarReporteHateoas(null)).withRel("crear")
        );
    }

    @Operation(
        summary = "Crear reporte global (HATEOAS)",
        description = "Guarda un reporte global y devuelve la ubicación del recurso creado con enlaces HATEOAS."
    )
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Reporte global guardado correctamente"))
    @PostMapping("/hateoas")
    public ResponseEntity<?> guardarReporteHateoas(@RequestBody ReporteGlobal reporte) {
        ReporteGlobal guardado = service.save(reporte);
        return ResponseEntity.created(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(ReporteGlobalController.class).obtenerReportePorIdHateoas(guardado.getId())).toUri()
        ).build();
    }

    @Operation(
        summary = "Eliminar reporte global por ID (HATEOAS)",
        description = "Elimina un reporte global por su ID y devuelve una respuesta vacía."
    )
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Reporte global eliminado correctamente"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarReporteHateoas(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
