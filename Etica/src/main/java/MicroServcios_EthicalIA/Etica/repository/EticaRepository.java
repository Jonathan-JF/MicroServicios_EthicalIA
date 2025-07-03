package MicroServcios_EthicalIA.Etica.repository;

import MicroServcios_EthicalIA.Etica.model.Etica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EticaRepository extends JpaRepository<Etica, Long> {

    List<Etica> findByTextoContainingIgnoreCase(String palabraClave);

    List<Etica> findByLenguajeInclusivo(boolean lenguajeInclusivo);

    List<Etica> findByLenguajeOfensivo(boolean lenguajeOfensivo);

    List<Etica> findByFechaAnalisisBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Etica> findByObservacionesContainingIgnoreCase(String palabraClave);

    @Query("SELECT e FROM Etica e ORDER BY e.fechaAnalisis DESC")
    List<Etica> listarEticasRecientes();

    List<Etica> findByLenguajeInclusivoAndLenguajeOfensivo(boolean lenguajeInclusivo, boolean lenguajeOfensivo);
}
