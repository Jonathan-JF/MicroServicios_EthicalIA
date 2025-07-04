package MC_EthicalIA.informe.controller;

import MC_EthicalIA.informe.model.informe;
import MC_EthicalIA.informe.service.informeService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Informe v1", description = "Operaciones sobre informes generados")
@RestController
@RequestMapping("/api/v1/informes")
public class informeController {

    @Autowired
    private informeService service;


    @Operation(summary = "Listar todos los informes")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de informes"))
    @GetMapping
    public ResponseEntity<List<informe>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @Operation(summary = "Obtener informe por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Informe encontrado"),
        @ApiResponse(responseCode = "404", description = "Informe no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<informe> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar informes por ID de texto")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de informes para el texto"))
    @GetMapping("/texto/{textoId}")
    public ResponseEntity<List<informe>> listarPorTexto(@PathVariable Long textoId) {
        return ResponseEntity.ok(service.findByTextoId(textoId));
    }

    @Operation(summary = "Buscar informes por palabra en el resumen")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de informes encontrados"))
    @GetMapping("/buscar")
    public ResponseEntity<List<informe>> buscarPorResumen(@RequestParam String palabra) {
        return ResponseEntity.ok(service.buscarPorResumen(palabra));
    }

    @Operation(summary = "Listar informes recientes")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de informes recientes"))
    @GetMapping("/recientes")
    public ResponseEntity<List<informe>> recientes() {
        return ResponseEntity.ok(service.listarRecientes());
    }

    @Operation(summary = "Crear un nuevo informe")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Informe creado correctamente"))
    @PostMapping
    public ResponseEntity<informe> crear(@RequestBody informe informe) {
        return ResponseEntity.ok(service.save(informe));
    }

    @Operation(summary = "Actualizar un informe existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Informe actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Informe no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<informe> actualizar(@PathVariable Long id, @RequestBody informe actualizado) {
        return service.findById(id)
                .map(i -> {
                    actualizado.setId(id);
                    return ResponseEntity.ok(service.save(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar informe por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Informe eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Informe no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
