package Texto.Texto.controller;

import Texto.Texto.model.Texto;
import Texto.Texto.service.TextoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Texto", description = "Operaciones sobre textos")
@RestController
@RequestMapping("/api/v1/textos")
public class TextoController {

    private final TextoService service;

    public TextoController(TextoService service) {
        this.service = service;
    }

    @Operation(summary = "Listar todos los textos")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de textos"))
    @GetMapping
    public ResponseEntity<List<Texto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener texto por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Texto encontrado"),
        @ApiResponse(responseCode = "404", description = "Texto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Texto> obtenerPorId(
            @Parameter(description = "ID del texto") @PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear un nuevo texto")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Texto creado correctamente"))
    @PostMapping
    public ResponseEntity<Texto> crear(@RequestBody Texto nuevo) {
        return ResponseEntity.ok(service.save(nuevo));
    }

    @Operation(summary = "Actualizar un texto existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Texto actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Texto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Texto> actualizar(
            @Parameter(description = "ID del texto") @PathVariable Long id,
            @RequestBody Texto actualizado) {
        return service.findById(id)
                .map(t -> {
                    actualizado.setId(id);
                    return ResponseEntity.ok(service.save(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar texto por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Texto eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Texto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID del texto") @PathVariable Long id) {
        return service.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
