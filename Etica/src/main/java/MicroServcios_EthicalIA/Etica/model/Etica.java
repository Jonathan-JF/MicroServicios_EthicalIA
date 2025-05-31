package MicroServcios_EthicalIA.Etica.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "etica")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Etica {

        @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String texto;

    @Column(nullable = false)
    private boolean lenguajeInclusivo;

    @Column(nullable = false)
    private boolean lenguajeOfensivo;

    @Column(nullable = true)
    private String observaciones;

    @Column(nullable = false)
    private LocalDateTime fechaAnalisis;
}
