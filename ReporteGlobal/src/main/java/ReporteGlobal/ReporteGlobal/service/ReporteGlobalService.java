package ReporteGlobal.ReporteGlobal.service;

import com.ethicaia.model.ReporteGlobal;
import com.ethicaia.repository.ReporteGlobalRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReporteGlobalService {

    private final ReporteGlobalRepository repository;

    public ReporteGlobalService(ReporteGlobalRepository repository) {
        this.repository = repository;
    }

    public List<ReporteGlobal> findAll() {
        return repository.findAll();
    }

    public Optional<ReporteGlobal> findById(Long id) {
        return repository.findById(id);
    }

    public ReporteGlobal save(ReporteGlobal reporte) {
        return repository.save(reporte);
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}

