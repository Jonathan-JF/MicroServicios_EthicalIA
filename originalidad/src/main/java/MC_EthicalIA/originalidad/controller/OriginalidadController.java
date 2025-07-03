package MC_EthicalIA.originalidad.controller;

import MC_EthicalIA.originalidad.model.Originalidad;
import MC_EthicalIA.originalidad.service.OriginalidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

@Tag(name = "Originalidad", description = "Operaciones sobre análisis de originalidad de textos")
@RestController
@RequestMapping("/api/v1/originalidad")
public class OriginalidadController {

    private final OriginalidadService service;

    public OriginalidadController(OriginalidadService service) {
        this.service = service;
    }


    @Operation(summary = "Listar todos los análisis de originalidad")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis de originalidad"))
    @GetMapping
    public ResponseEntity<List<Originalidad>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Buscar análisis por texto exacto")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis encontrados"))
    @GetMapping("/buscar")
    public ResponseEntity<List<Originalidad>> buscarPorTexto(@RequestParam String texto) {
        return ResponseEntity.ok(service.findByTexto(texto));
    }

    @Operation(summary = "Buscar análisis con porcentaje de plagio mayor a un valor")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis encontrados"))
    @GetMapping("/plagio")
    public ResponseEntity<List<Originalidad>> buscarPorPlagio(@RequestParam double min) {
        return ResponseEntity.ok(service.findByPlagioMayorA(min));
    }

    @Operation(summary = "Búsqueda avanzada por plagio y palabra en texto")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis encontrados"))
    @GetMapping("/filtrado")
    public ResponseEntity<List<Originalidad>> buscarAvanzado(
            @Parameter(description = "Porcentaje mínimo de plagio") @RequestParam double limite,
            @Parameter(description = "Palabra a buscar en el texto") @RequestParam String palabra) {
        return ResponseEntity.ok(service.buscarPorPlagioYTexto(limite, palabra));
    }

    @Operation(summary = "Crear un nuevo análisis de originalidad")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Análisis creado correctamente"))
    @PostMapping
    public ResponseEntity<Originalidad> crear(@RequestBody Originalidad resultado) {
        return ResponseEntity.ok(service.save(resultado));
    }

    @Operation(summary = "Actualizar un análisis de originalidad existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Análisis actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Análisis no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Originalidad> actualizar(
            @PathVariable Long id,
            @RequestBody Originalidad actualizado) {
        return service.findById(id)
                .map(r -> {
                    actualizado.setId(id);
                    return ResponseEntity.ok(service.save(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar análisis de originalidad por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Análisis eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Análisis no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @Operation(
        summary = "Obtener análisis de originalidad por ID (HATEOAS)",
        description = "Devuelve un análisis de originalidad con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Análisis encontrado con enlaces HATEOAS"),
        @ApiResponse(responseCode = "404", description = "Análisis no encontrado")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Originalidad>> obtenerOriginalidadPorIdHateoas(@PathVariable Long id) {
        Optional<Originalidad> originalidadOpt = service.findById(id);
        if (originalidadOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Originalidad originalidad = originalidadOpt.get();
        EntityModel<Originalidad> recurso = EntityModel.of(originalidad,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).obtenerOriginalidadPorIdHateoas(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).listarOriginalidadesHateoas()).withRel("todas-las-originalidades"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).eliminarOriginalidadHateoas(id)).withRel("eliminar")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(
        summary = "Listar todos los análisis de originalidad (HATEOAS)",
        description = "Devuelve una colección de análisis de originalidad con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis con enlaces HATEOAS"))
    @GetMapping("/hateoas")
    public CollectionModel<EntityModel<Originalidad>> listarOriginalidadesHateoas() {
        List<EntityModel<Originalidad>> originalidades = service.findAll().stream()
                .map(o -> EntityModel.of(o,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).obtenerOriginalidadPorIdHateoas(o.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).eliminarOriginalidadHateoas(o.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(originalidades,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).listarOriginalidadesHateoas()).withSelfRel());
    }

    @Operation(summary = "Buscar análisis por texto (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis encontrados con enlaces HATEOAS"))
    @GetMapping("/hateoas/buscar")
    public CollectionModel<EntityModel<Originalidad>> buscarPorTextoHateoas(@RequestParam String texto) {
        List<EntityModel<Originalidad>> originalidades = service.findByTexto(texto).stream()
                .map(o -> EntityModel.of(o,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).obtenerOriginalidadPorIdHateoas(o.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).eliminarOriginalidadHateoas(o.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(originalidades,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).buscarPorTextoHateoas(texto)).withSelfRel());
    }

    @Operation(summary = "Buscar análisis por plagio (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis encontrados con enlaces HATEOAS"))
    @GetMapping("/hateoas/plagio")
    public CollectionModel<EntityModel<Originalidad>> buscarPorPlagioHateoas(@RequestParam double min) {
        List<EntityModel<Originalidad>> originalidades = service.findByPlagioMayorA(min).stream()
                .map(o -> EntityModel.of(o,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).obtenerOriginalidadPorIdHateoas(o.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).eliminarOriginalidadHateoas(o.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(originalidades,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).buscarPorPlagioHateoas(min)).withSelfRel());
    }

    @Operation(summary = "Búsqueda avanzada (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis encontrados con enlaces HATEOAS"))
    @GetMapping("/hateoas/filtrado")
    public CollectionModel<EntityModel<Originalidad>> buscarAvanzadoHateoas(
            @Parameter(description = "Porcentaje mínimo de plagio") @RequestParam double limite,
            @Parameter(description = "Palabra a buscar en el texto") @RequestParam String palabra) {
        List<EntityModel<Originalidad>> originalidades = service.buscarPorPlagioYTexto(limite, palabra).stream()
                .map(o -> EntityModel.of(o,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).obtenerOriginalidadPorIdHateoas(o.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).eliminarOriginalidadHateoas(o.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(originalidades,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(OriginalidadController.class).buscarAvanzadoHateoas(limite, palabra)).withSelfRel());
    }

    @Operation(
        summary = "Eliminar análisis de originalidad por ID (HATEOAS)",
        description = "Elimina un análisis de originalidad por su ID y devuelve una respuesta vacía."
    )
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Análisis eliminado correctamente"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarOriginalidadHateoas(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
