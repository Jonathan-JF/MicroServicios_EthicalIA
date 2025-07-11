package Seguridad.Seguridad.assembler;

import Seguridad.Seguridad.controller.SeguridadControllerV2;
import Seguridad.Seguridad.model.Seguridad;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class SeguridadAssembler implements RepresentationModelAssembler<Seguridad, EntityModel<Seguridad>> {

    @Override
    public EntityModel<Seguridad> toModel(Seguridad seguridad) {
        return EntityModel.of(seguridad,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SeguridadControllerV2.class)
                        .obtenerPorIdHateoas(seguridad.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SeguridadControllerV2.class)
                        .listarHateoas()).withRel("todos-los-registros"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(SeguridadControllerV2.class)
                        .eliminarHateoas(seguridad.getId())).withRel("eliminar")
        );
    }
}
