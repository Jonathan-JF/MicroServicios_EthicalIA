package MicroServcios_EthicalIA.Coherencia.model;

import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Entity
@Table(name = "coherencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

