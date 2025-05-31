package MC_EthicalIA.informe.service;

import MC_EthicalIA.informe.model.informe;
import MC_EthicalIA.informe.repository.informeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class informeService {

    @Autowired
    private final informeRepository repository;



    public List<informe> findAll() {
        return repository.findAll();
    }

    public Optional<informe> findById(Long id) {
        return repository.findById(id);
    }

    public List<informe> findByTextoId(Long textoId) {
        return repository.findByTextoId(textoId);
    }

    public List<informe> buscarPorResumen(String palabra) {
        return repository.findByResumenContainingIgnoreCase(palabra);
    }

    public List<informe> listarRecientes() {
        return repository.listarinformesRecientes();
    }

    public informe save(informe informe) {
        return repository.save(informe);
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}

