package Texto.Texto.assembler;

import Texto.Texto.controller.TextoControllerV2;
import Texto.Texto.model.Texto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class TextoAssembler implements RepresentationModelAssembler<Texto, EntityModel<Texto>> {

    @Override
    public EntityModel<Texto> toModel(Texto texto) {
        return EntityModel.of(texto,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TextoControllerV2.class)
                        .obtenerTextoPorIdHateoas(texto.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TextoControllerV2.class)
                        .listarTextosHateoas()).withRel("todos-los-textos"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TextoControllerV2.class)
                        .eliminarTextoHateoas(texto.getId())).withRel("eliminar")
        );
    }
}