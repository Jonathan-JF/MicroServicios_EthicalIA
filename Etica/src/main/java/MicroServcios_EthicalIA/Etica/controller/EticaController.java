package MicroServcios_EthicalIA.Etica.controller;

import MicroServcios_EthicalIA.Etica.model.Etica;
import MicroServcios_EthicalIA.Etica.service.EticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/etica")
@Tag(name = "Ética", description = "Operaciones sobre análisis ético de textos")
public class EticaController {

    private EticaService eticaService;

    @Autowired
    public EticaController(EticaService eticaService) {
        this.eticaService = eticaService;
    }

    @Operation(summary = "Guardar análisis ético")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Análisis guardado correctamente"))
    @PostMapping
    public Etica saveEtica(@RequestBody Etica etica) {
        return eticaService.saveEtica(etica);
    }

    @Operation(summary = "Listar todos los análisis éticos")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis éticos"))
    @GetMapping
    public List<Etica> getAllEticas() {
        return eticaService.getAllEticas();
    }

    @Operation(summary = "Buscar análisis ético por ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Análisis encontrado"),
        @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/{id}")
    public Optional<Etica> getEticaById(@PathVariable Long id) {
        return eticaService.getEticaById(id);
    }

    @Operation(summary = "Eliminar análisis ético por ID")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Análisis eliminado"))
    @DeleteMapping("/{id}")
    public void deleteEtica(@PathVariable Long id) {
        eticaService.deleteEtica(id);
    }

    @Operation(summary = "Buscar análisis éticos por texto")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis encontrados"))
    @GetMapping("/buscar")
    public List<Etica> searchEticas(@RequestParam String texto) {
        return eticaService.searchEticasByTexto(texto);
    }

    @Operation(summary = "Obtener análisis éticos recientes")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis recientes"))
    @GetMapping("/recientes")
    public List<Etica> getRecentEticas() {
        return eticaService.getRecentEticas();
    }

    @Operation(summary = "Filtrar análisis éticos por lenguaje inclusivo/ofensivo")
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis filtrados"))
    @GetMapping("/filtro")
    public List<Etica> filterEticas(
            @Parameter(description = "¿Lenguaje inclusivo?") @RequestParam(required = false) Boolean lenguajeInclusivo,
            @Parameter(description = "¿Lenguaje ofensivo?") @RequestParam(required = false) Boolean lenguajeOfensivo) {
        if (lenguajeInclusivo != null && lenguajeOfensivo != null) {
            return eticaService.getEticasByLenguajeInclusivoAndOfensivo(lenguajeInclusivo, lenguajeOfensivo);
        } else if (lenguajeInclusivo != null) {
            return eticaService.getEticasByLenguajeInclusivo(lenguajeInclusivo);
        } else if (lenguajeOfensivo != null) {
            return eticaService.getEticasByLenguajeOfensivo(lenguajeOfensivo);
        } else {
            return eticaService.getAllEticas();
        }
    }


    @Operation(
        summary = "Obtener análisis ético por ID (HATEOAS)",
        description = "Devuelve un análisis ético con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Análisis encontrado con enlaces HATEOAS"),
        @ApiResponse(responseCode = "404", description = "No encontrado")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Etica>> obtenerEticaPorIdHateoas(@PathVariable Long id) {
        Optional<Etica> eticaOpt = eticaService.getEticaById(id);
        if (eticaOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Etica etica = eticaOpt.get();
        EntityModel<Etica> recurso = EntityModel.of(etica,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EticaController.class).obtenerEticaPorIdHateoas(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EticaController.class).listarEticasHateoas()).withRel("todas-las-eticas"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EticaController.class).eliminarEticaHateoas(id)).withRel("eliminar")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(
        summary = "Listar todos los análisis éticos (HATEOAS)",
        description = "Devuelve una colección de análisis éticos con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de análisis con enlaces HATEOAS"))
    @GetMapping("/hateoas")
    public CollectionModel<EntityModel<Etica>> listarEticasHateoas() {
        List<EntityModel<Etica>> eticas = eticaService.getAllEticas().stream()
                .map(e -> EntityModel.of(e,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EticaController.class).obtenerEticaPorIdHateoas(e.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EticaController.class).eliminarEticaHateoas(e.getId())).withRel("eliminar")
                ))
                .toList();
        return CollectionModel.of(eticas,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EticaController.class).listarEticasHateoas()).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EticaController.class).guardarEticaHateoas(null)).withRel("crear")
        );
    }

    @Operation(
        summary = "Guardar análisis ético (HATEOAS)",
        description = "Guarda un análisis ético y devuelve la ubicación del recurso creado con enlaces HATEOAS."
    )
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Análisis guardado correctamente"))
    @PostMapping("/hateoas")
    public ResponseEntity<?> guardarEticaHateoas(@RequestBody Etica etica) {
        Etica guardada = eticaService.saveEtica(etica);
        return ResponseEntity.created(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(EticaController.class).obtenerEticaPorIdHateoas(guardada.getId())).toUri()
        ).build();
    }

    @Operation(
        summary = "Eliminar análisis ético por ID (HATEOAS)",
        description = "Elimina un análisis ético por su ID y devuelve una respuesta vacía."
    )
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Análisis eliminado correctamente"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarEticaHateoas(@PathVariable Long id) {
        eticaService.deleteEtica(id);
        return ResponseEntity.noContent().build();
    }
}
