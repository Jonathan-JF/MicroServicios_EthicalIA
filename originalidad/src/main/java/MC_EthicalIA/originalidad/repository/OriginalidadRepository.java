package MC_EthicalIA.originalidad.repository;

import MC_EthicalIA.originalidad.model.Originalidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OriginalidadRepository extends JpaRepository<Originalidad, Long> {

    // Buscar por coincidencia de texto
    List<Originalidad> findByTextoContainingIgnoreCase(String texto);

    // Buscar los resultados con mayor nivel de plagio
    List<Originalidad> findByPorcentajePlagioGreaterThan(double porcentaje);

    // Consulta personalizada con @Query (JPQL)
    @Query("SELECT o FROM Originalidad o WHERE o.porcentajePlagio >= :limite AND o.texto LIKE %:palabra%")
    List<Originalidad> buscarPorPlagioYTexto(@Param("limite") double limite, @Param("palabra") String palabra);
}