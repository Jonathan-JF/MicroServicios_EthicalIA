package MC_EthicalIA.originalidad.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "originalidad")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Originalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, length = 13, nullable = false)
    private String texto;

    @Column(nullable = false)
    private double porcentajePlagio;

    @Column
    private String fuentesDetectadas;

    @Column(nullable = false)
    private LocalDateTime fechaAnalisis;
}
