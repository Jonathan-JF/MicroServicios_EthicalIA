package MicroServcios_EthicalIA.Etica.repository;

import MicroServcios_EthicalIA.Etica.model.Etica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EticaRepository extends JpaRepository<Etica, Long> {

    // Buscar por texto que contenga una palabra clave (sin importar mayúsculas/minúsculas)
    List<Etica> findByTextoContainingIgnoreCase(String palabraClave);

    // Buscar por si el lenguaje es inclusivo
    List<Etica> findByLenguajeInclusivo(boolean lenguajeInclusivo);

    // Buscar por si el lenguaje es ofensivo
    List<Etica> findByLenguajeOfensivo(boolean lenguajeOfensivo);

    // Buscar por rango de fechas de análisis
    List<Etica> findByFechaAnalisisBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    // Buscar por observaciones que contengan una palabra clave (sin importar mayúsculas/minúsculas)
    List<Etica> findByObservacionesContainingIgnoreCase(String palabraClave);

    // Consultar las entradas más recientes por fecha de análisis
    @Query("SELECT e FROM Etica e ORDER BY e.fechaAnalisis DESC")
    List<Etica> listarEticasRecientes();

    // Buscar por el estado del lenguaje inclusivo y ofensivo a la vez
    List<Etica> findByLenguajeInclusivoAndLenguajeOfensivo(boolean lenguajeInclusivo, boolean lenguajeOfensivo);
}
