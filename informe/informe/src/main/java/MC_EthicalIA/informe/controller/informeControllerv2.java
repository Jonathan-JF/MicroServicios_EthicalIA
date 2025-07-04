package MC_EthicalIA.informe.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import MC_EthicalIA.informe.model.informe;
import MC_EthicalIA.informe.service.informeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Informe v2", description = "Operaciones sobre informes generados v2")
@RestController
@RequestMapping("/api/v2/informes")
public class informeControllerv2 {

    @Autowired
    private informeService service;

    @Operation(
        summary = "Obtener informe por ID (HATEOAS)",
        description = "Devuelve un informe con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Informe encontrado con enlaces HATEOAS"),
        @ApiResponse(responseCode = "404", description = "Informe no encontrado")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<informe>> obtenerInformePorIdHateoas(@PathVariable Long id) {
        Optional<informe> informeOpt = service.findById(id);
        if (informeOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        informe inf = informeOpt.get();
        EntityModel<informe> recurso = EntityModel.of(inf,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class).obtenerInformePorIdHateoas(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class).listarInformesHateoas()).withRel("todos-los-informes"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class).eliminarInformeHateoas(id)).withRel("eliminar")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(
        summary = "Listar todos los informes (HATEOAS)",
        description = "Devuelve una colección de informes con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de informes con enlaces HATEOAS"))
    @GetMapping("/hateoas")
    public CollectionModel<EntityModel<informe>> listarInformesHateoas() {
        List<EntityModel<informe>> informes = service.findAll().stream()
                .map(inf -> EntityModel.of(inf,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class).obtenerInformePorIdHateoas(inf.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class).eliminarInformeHateoas(inf.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(informes,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class).listarInformesHateoas()).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class).guardarInformeHateoas(null)).withRel("crear")
        );
    }

    @Operation(
        summary = "Crear informe (HATEOAS)",
        description = "Guarda un informe y devuelve la ubicación del recurso creado con enlaces HATEOAS."
    )
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Informe guardado correctamente"))
    @PostMapping("/hateoas")
    public ResponseEntity<?> guardarInformeHateoas(@RequestBody informe informe) {
        informe guardado = service.save(informe);
        return ResponseEntity.created(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class).obtenerInformePorIdHateoas(guardado.getId())).toUri()
        ).build();
    }

    @Operation(
        summary = "Eliminar informe por ID (HATEOAS)",
        description = "Elimina un informe por su ID y devuelve una respuesta vacía."
    )
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Informe eliminado correctamente"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarInformeHateoas(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
