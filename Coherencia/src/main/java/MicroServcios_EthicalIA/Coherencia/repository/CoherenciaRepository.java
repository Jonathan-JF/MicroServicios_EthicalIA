package MicroServcios_EthicalIA.Coherencia.repository;

import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoherenciaRepository extends JpaRepository<Coherencia, Long> {

    List<Coherencia> findByTextoContainingIgnoreCase(String palabraClave);

    List<Coherencia> findByPuntuacionCoherenciaGreaterThanEqual(double puntuacion);

    List<Coherencia> findByFechaAnalisisBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Coherencia> findBySugerenciasContainingIgnoreCase(String palabraClave);

    List<Coherencia> findTop5ByOrderByFechaAnalisisDesc();

    List<Coherencia> findByPuntuacionCoherenciaGreaterThanEqualAndFechaAnalisisAfter(double puntuacion, LocalDateTime fechaAnalisis);
}
