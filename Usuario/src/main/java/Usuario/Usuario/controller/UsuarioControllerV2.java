package Usuario.Usuario.controller;

import Usuario.Usuario.model.Usuario;
import Usuario.Usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Tag(name = "Usuario v2", description = "Operaciones sobre usuarios con HATEOAS")
@RestController
@RequestMapping("/api/v2/usuarios")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService service;

    @Operation(
        summary = "Obtener usuario por ID (HATEOAS)",
        description = "Devuelve un usuario con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado con enlaces HATEOAS"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/hateoas/{id}")
    public ResponseEntity<EntityModel<Usuario>> obtenerUsuarioPorIdHateoas(@PathVariable Long id) {
        Optional<Usuario> usuarioOpt = service.findById(id);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Usuario usuario = usuarioOpt.get();
        EntityModel<Usuario> recurso = EntityModel.of(usuario,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).obtenerUsuarioPorIdHateoas(id)).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).listarUsuariosHateoas()).withRel("todos-los-usuarios"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).eliminarUsuarioHateoas(id)).withRel("eliminar")
        );
        return ResponseEntity.ok(recurso);
    }

    @Operation(
        summary = "Listar todos los usuarios (HATEOAS)",
        description = "Devuelve una colección de usuarios con enlaces HATEOAS para navegación RESTful."
    )
    @ApiResponses(@ApiResponse(responseCode = "200", description = "Lista de usuarios con enlaces HATEOAS"))
    @GetMapping("/hateoas")
    public CollectionModel<EntityModel<Usuario>> listarUsuariosHateoas() {
        List<EntityModel<Usuario>> usuarios = service.findAll().stream()
                .map(u -> EntityModel.of(u,
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).obtenerUsuarioPorIdHateoas(u.getId())).withRel("ver"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).eliminarUsuarioHateoas(u.getId())).withRel("eliminar")
                ))
                .collect(Collectors.toList());
        return CollectionModel.of(usuarios,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).listarUsuariosHateoas()).withSelfRel());
    }

    @Operation(
        summary = "Crear usuario (HATEOAS)",
        description = "Guarda un usuario y devuelve la ubicación del recurso creado con enlaces HATEOAS."
    )
    @ApiResponses(@ApiResponse(responseCode = "201", description = "Usuario guardado correctamente"))
    @PostMapping("/hateoas")
    public ResponseEntity<?> guardarUsuarioHateoas(@RequestBody Usuario usuario) {
        Usuario guardado = service.save(usuario);
        return ResponseEntity.created(
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class).obtenerUsuarioPorIdHateoas(guardado.getId())).toUri()
        ).build();
    }

    @Operation(
        summary = "Eliminar usuario por ID (HATEOAS)",
        description = "Elimina un usuario por su ID y devuelve una respuesta vacía."
    )
    @ApiResponses(@ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente"))
    @DeleteMapping("/hateoas/{id}")
    public ResponseEntity<?> eliminarUsuarioHateoas(@PathVariable Long id) {
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
