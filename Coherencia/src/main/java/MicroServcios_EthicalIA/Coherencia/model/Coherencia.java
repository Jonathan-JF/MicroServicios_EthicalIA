package MicroServcios_EthicalIA.Coherencia.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "coherencia")
@data
@AllArgsConstructor
@NoArgsConstructor
public class Coherencia {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    private double puntuacionCoherencia;

    @Column(nullable = true)
    private String sugerencias;

    @Column(nullable = false)
    private LocalDateTime fechaAnalisis;

}
