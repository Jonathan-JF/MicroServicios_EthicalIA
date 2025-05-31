package MicroServcios_EthicalIA.Coherencia.repository;

import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CoherenciaRepository extends JpaRepository<Coherencia, Long> {

    // Buscar coherencias por texto que contenga una palabra clave (sin importar mayúsculas/minúsculas)
    List<Coherencia> findByTextoContainingIgnoreCase(String palabraClave);

    // Buscar coherencias por puntuación (ejemplo: coherencias con puntuación mayor a 7)
    List<Coherencia> findByPuntuacionCoherenciaGreaterThanEqual(double puntuacion);

    // Buscar coherencias por rango de fechas
    List<Coherencia> findByFechaAnalisisBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar coherencias por sugerencias
    List<Coherencia> findBySugerenciasContainingIgnoreCase(String palabraClave);

    // Obtener las coherencias más recientes (ordenadas por fecha de análisis descendente)
    List<Coherencia> findTop5ByOrderByFechaAnalisisDesc();

    // Buscar coherencias con puntuación mayor o igual a un valor y fecha posterior a un determinado momento
    List<Coherencia> findByPuntuacionCoherenciaGreaterThanEqualAndFechaAnalisisAfter(double puntuacion, LocalDateTime fechaAnalisis);
}
