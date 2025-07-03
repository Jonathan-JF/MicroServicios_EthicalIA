package Historial.Historial.service;

import Historial.Historial.model.Historial;
import Historial.Historial.repository.HistorialRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class HistorialService {

    private final HistorialRepository repository;

    public HistorialService(HistorialRepository repository) {
        this.repository = repository;
    }

    public List<Historial> findAll() {
        return repository.findAll();
    }

    public Optional<Historial> findById(Long id) {
        return repository.findById(id);
    }

    public Historial save(Historial historial) {
        return repository.save(historial);
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}