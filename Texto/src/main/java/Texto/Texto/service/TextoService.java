package Texto.Texto.service;

import Texto.Texto.model.Texto;
import Texto.Texto.repository.TextoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TextoService {

    private final TextoRepository repository;

    public TextoService(TextoRepository repository) {
        this.repository = repository;
    }

    public List<Texto> findAll() {
        return repository.findAll();
    }

    public Optional<Texto> findById(Long id) {
        return repository.findById(id);
    }

    public Texto save(Texto texto) {
        return repository.save(texto);
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
