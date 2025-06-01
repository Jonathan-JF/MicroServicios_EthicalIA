package MC_EthicalIA.informe.repository;

import MC_EthicalIA.informe.model.informe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface informeRepository extends JpaRepository<informe, Long> {

    List<informe> findByTextoId(Long textoId);

    List<informe> findByResumenContainingIgnoreCase(String palabraClave);

    @Query("SELECT i FROM informe i ORDER BY i.fechaGeneracion DESC")
    List<informe> listarinformesRecientes();
}

