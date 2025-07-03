package Seguridad.Seguridad.service;

import Seguridad.Seguridad.model.Seguridad;
import Seguridad.Seguridad.repository.SeguridadRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SeguridadService {

    private final SeguridadRepository repository;

    public SeguridadService(SeguridadRepository repository) {
        this.repository = repository;
    }

    public List<Seguridad> findAll() {
        return repository.findAll();
    }

    public Optional<Seguridad> findById(Long id) {
        return repository.findById(id);
    }

    public Seguridad save(Seguridad seguridad) {
        return repository.save(seguridad);
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
