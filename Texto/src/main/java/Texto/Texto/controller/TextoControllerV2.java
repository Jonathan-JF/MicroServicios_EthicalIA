package Texto.Texto.controller;

import Texto.Texto.model.Texto;
import Texto.Texto.service.TextoService;
import Texto.Texto.assembler.TextoAssembler;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

@Tag(name = "Texto v2", description = "Operaciones sobre textos con HATEOAS")
@RestController
@RequestMapping("/api/v2/textos")
public class TextoControllerV2 {

    private final TextoService service;
    private final TextoAssembler assembler;

    public TextoControllerV2(TextoService service, TextoAssembler assembler) {
        this.service = service;
        this.assembler = assembler;
    }

    @Operation(summary = "Obtener texto por ID (HATEOAS)")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Texto encontrado con enlaces HATEOAS"),
        @ApiResponse(responseCode = "404", description = "Texto no encontrado")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Texto>> obtenerTextoPorIdHateoas(@PathVariable Long id) {
        Optional<Texto> textoOpt = service.findById(id);
        return textoOpt.map(texto -> ResponseEntity.ok(assembler.toModel(texto)))
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Listar todos los textos (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de textos con enlaces HATEOAS"))
    @GetMapping("/hateoas")
    public ResponseEntity<CollectionModel<EntityModel<Texto>>> listarTextosHateoas() {
        List<EntityModel<Texto>> textos = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        CollectionModel<EntityModel<Texto>> collectionModel = CollectionModel.of(textos,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TextoControllerV2.class)
                        .listarTextosHateoas()).withSelfRel());

        return ResponseEntity.ok(collectionModel);
    }

    @Operation(summary = "Crear texto (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Texto creado correctamente"))
    @PostMapping("/hateoas")
    public ResponseEntity<?> crearTextoHateoas(@RequestBody Texto nuevo) {
        Texto guardado = service.save(nuevo);

        return ResponseEntity.created(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TextoControllerV2.class)
                        .obtenerTextoPorIdHateoas(guardado.getId())).toUri()
        ).build();
    }

    @Operation(summary = "Eliminar texto por ID (HATEOAS)")
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Texto eliminado correctamente"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarTextoHateoas(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
