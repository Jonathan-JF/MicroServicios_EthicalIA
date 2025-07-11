package Seguridad.Seguridad.controller;

import Seguridad.Seguridad.model.Seguridad;
import Seguridad.Seguridad.service.SeguridadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Seguridad", description = "Operaciones sobre seguridad")
@RestController
@RequestMapping("/api/v1/seguridad")
public class SeguridadController {

    private SeguridadService service;

    public SeguridadController(SeguridadService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los registros de seguridad")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de registros"))
    @GetMapping
    public ResponseEntity<List<Seguridad>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener registro por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro encontrado"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Seguridad> obtenerPorId(
            @Parameter(description = "ID del registro") @PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo registro de seguridad")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Registro creado correctamente"))
    @PostMapping
    public ResponseEntity<Seguridad> crear(@RequestBody Seguridad nuevo) {
        return ResponseEntity.ok(service.save(nuevo));
    }

    @Operation(summary = "Actualizar un registro existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Registro actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Seguridad> actualizar(
            @Parameter(description = "ID del registro") @PathVariable Long id,
            @RequestBody Seguridad actualizado) {
        return service.findById(id)
                .map(s -> {
                    actualizado.setId(id);
                    return ResponseEntity.ok(service.save(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar registro por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Registro eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Registro no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del registro") @PathVariable Long id) {
        return service.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
