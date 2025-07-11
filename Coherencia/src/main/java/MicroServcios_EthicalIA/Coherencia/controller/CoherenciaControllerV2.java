package MicroServcios_EthicalIA.Coherencia.controller;

import MicroServcios_EthicalIA.Coherencia.assembler.CoherenciaAssembler;
import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import MicroServcios_EthicalIA.Coherencia.service.CoherenciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v2/coherencia")
@Tag(name = "Coherencia v2", description = "Operaciones sobre coherencia con HATEOAS")
public class CoherenciaControllerV2 {

    private final CoherenciaService service;
    private final CoherenciaAssembler assembler;

    public CoherenciaControllerV2(CoherenciaService service, CoherenciaAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Obtener coherencia por ID (HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Coherencia encontrada"),
        @ApiResponse(responseCode = "404", description = "Coherencia no encontrada")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Coherencia>> obtenerPorIdHateoas(@PathVariable Long id) {
        Optional<Coherencia> opt = service.getCoherenciaById(id);
        return opt.map(coh -> ResponseEntity.ok(assembler.toModel(coh)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todas las coherencias (HATEOAS)")
    @ApiResponse(responseCode = "200", description = "Lista de coherencias con enlaces HATEOAS")
    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<Coherencia>>> listarHateoas() {
        List<EntityModel<Coherencia>> recursos = service.getAllCoherencias().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return ResponseEntity.ok(CollectionModel.of(recursos));
    }

    @Operation(summary = "Crear coherencia (HATEOAS)")
    @ApiResponse(responseCode = "201", description = "Coherencia creada correctamente")
    @PostMapping("/hateoas")
    public ResponseEntity<?> crearHateoas(@RequestBody Coherencia nuevo) {
        Coherencia guardado = service.saveCoherencia(nuevo);
        return ResponseEntity.created(assembler.getSelfLinkUri(guardado.getId())).build();
    }

    @Operation(summary = "Eliminar coherencia por ID (HATEOAS)")
    @ApiResponse(responseCode = "204", description = "Coherencia eliminada correctamente")
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarHateoas(@PathVariable Long id) {
        service.deleteCoherencia(id);
        return ResponseEntity.noContent().build();
    }
}

