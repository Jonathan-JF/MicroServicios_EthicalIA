package MC_EthicalIA.informe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "informe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class informe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long textoId;

    @Column(nullable = false)
    private String resumen;

    @Column(nullable = false)
    private String urlPdfGenerado;

    @Column(nullable = false)
    private LocalDateTime fechaGeneracion;

}



