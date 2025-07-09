package Usuario.Usuario.assembler;

import Usuario.Usuario.controller.UsuarioControllerV2;
import Usuario.Usuario.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class UsuarioAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class)
                        .obtenerUsuarioPorIdHateoas(usuario.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class)
                        .listarUsuariosHateoas()).withRel("todos-los-usuarios"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControllerV2.class)
                        .eliminarUsuarioHateoas(usuario.getId())).withRel("eliminar")
        );
    }
}
