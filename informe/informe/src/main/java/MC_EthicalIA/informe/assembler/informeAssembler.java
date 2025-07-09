package MC_EthicalIA.informe.assembler;

import MC_EthicalIA.informe.controller.informeControllerv2;
import MC_EthicalIA.informe.model.informe;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;


@Component
public class informeAssembler implements RepresentationModelAssembler<informe, EntityModel<informe>> {
    
    @Override
    public EntityModel<informe> toModel(informe inf) {
        return EntityModel.of(inf,
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class)
                        .obtenerInformePorIdHateoas(inf.getId())).withSelfRel(),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class)
                        .listarInformesHateoas()).withRel("todos-los-informes"),
                WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(informeControllerv2.class)
                        .eliminarInformeHateoas(inf.getId())).withRel("eliminar")
        );
    }
}
