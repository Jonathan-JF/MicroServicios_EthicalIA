package Historial.Historial.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "historial")

public class Historial {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long textoId;

    @Column(nullable = false)
    private LocalDateTime fechaAnalisis;

    @Column(nullable = true)
    private String descripcion;

    @Column(nullable = false)
    private boolean consentimiento;
}
