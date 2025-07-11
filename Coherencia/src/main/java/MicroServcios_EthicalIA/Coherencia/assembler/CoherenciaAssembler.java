package MicroServcios_EthicalIA.Coherencia.assembler;

import MicroServcios_EthicalIA.Coherencia.controller.CoherenciaControllerV2;
import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class CoherenciaAssembler implements RepresentationModelAssembler<Coherencia, EntityModel<Coherencia>> {

    @Override
    public EntityModel<Coherencia> toModel(Coherencia coherencia) {
        return EntityModel.of(coherencia,
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaControllerV2.class)
                .obtenerPorIdHateoas(coherencia.getId())).withSelfRel(),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaControllerV2.class)
                .listarHateoas()).withRel("todas-las-coherencias"),
            WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaControllerV2.class)
                .eliminarHateoas(coherencia.getId())).withRel("eliminar")
        );
    }

    public URI getSelfLinkUri(Long id) {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(CoherenciaControllerV2.class)
            .obtenerPorIdHateoas(id)).toUri();
    }
}
