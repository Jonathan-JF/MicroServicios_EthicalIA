package Seguridad.Seguridad.controller;
import Seguridad.Seguridad.model.Seguridad;
import Seguridad.Seguridad.service.SeguridadService;
import Seguridad.Seguridad.assembler.SeguridadAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Tag(name = "Seguridad v2", description = "Operaciones sobre seguridad con HATEOAS")
@RestController
@RequestMapping("/api/v2/seguridad")
public class SeguridadControllerV2 {

    private final SeguridadService service;
    private final SeguridadAssembler assembler;

    public SeguridadControllerV2(SeguridadService service, SeguridadAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Obtener registro de seguridad por ID (HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado con enlaces HATEOAS"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Seguridad>> obtenerPorIdHateoas(@PathVariable Long id) {
        return service.findById(id)
                .map(seguridad -> {
                    EntityModel<Seguridad> modelo = EntityModel.of(seguridad,
                            linkTo(methodOn(SeguridadControllerV2.class).obtenerPorIdHateoas(id)).withSelfRel(),
                            linkTo(methodOn(SeguridadControllerV2.class).listarHateoas()).withRel("todos-los-registros"),
                            linkTo(methodOn(SeguridadControllerV2.class).eliminarHateoas(id)).withRel("eliminar")
                    );
                    return ResponseEntity.ok(modelo);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los registros (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de registros con enlaces HATEOAS"))
    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<Seguridad>>> listarHateoas() {
        List<EntityModel<Seguridad>> recursos = service.findAll().stream()
                .map(seguridad -> EntityModel.of(seguridad,
                        linkTo(methodOn(SeguridadControllerV2.class).obtenerPorIdHateoas(seguridad.getId())).withRel("ver"),
                        linkTo(methodOn(SeguridadControllerV2.class).eliminarHateoas(seguridad.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Seguridad>> coleccion = CollectionModel.of(recursos,
                linkTo(methodOn(SeguridadControllerV2.class).listarHateoas()).withSelfRel());

        return ResponseEntity.ok(coleccion);
    }

    @Operation(summary = "Crear registro (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Registro creado correctamente"))
    @PostMapping("/hateoas")
    public ResponseEntity<Void> crearHateoas(@RequestBody Seguridad nuevo) {
        Seguridad guardado = service.save(nuevo);
        URI ubicacion = linkTo(methodOn(SeguridadControllerV2.class)
                .obtenerPorIdHateoas(guardado.getId())).toUri();
        return ResponseEntity.created(ubicacion).build();
    }

    @Operation(summary = "Eliminar registro por ID (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Registro eliminado correctamente"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<Void> eliminarHateoas(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
