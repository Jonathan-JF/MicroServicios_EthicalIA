package ReporteGlobal.ReporteGlobal.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reporte_global")

public class ReporteGlobal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int cantidadAnalisis;

    @Column(nullable = false)
    private int erroresSistema;

    @Column(nullable = false)
    private LocalDateTime fechaReporte;

    @Column(nullable = true)
    private String observaciones;
}
