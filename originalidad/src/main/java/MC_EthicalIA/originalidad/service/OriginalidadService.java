package MC_EthicalIA.originalidad.service;

import MC_EthicalIA.originalidad.model.Originalidad;
import MC_EthicalIA.originalidad.repository.OriginalidadRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OriginalidadService {

    private final OriginalidadRepository repository;

    @Autowired
    public OriginalidadService(OriginalidadRepository repository) {
        this.repository = repository;
    }

    public List<Originalidad> findAll() {
        return repository.findAll();
    }

    public Optional<Originalidad> findById(Long id) {
        return repository.findById(id);
    }

    public List<Originalidad> findByTexto(String texto) {
        return repository.findByTextoContainingIgnoreCase(texto);
    }

    public List<Originalidad> findByPlagioMayorA(double porcentaje) {
        return repository.findByPorcentajePlagioGreaterThan(porcentaje);
    }

    public List<Originalidad> buscarPorPlagioYTexto(double limite, String palabra) {
        return repository.buscarPorPlagioYTexto(limite, palabra);
    }

    public Originalidad save(Originalidad resultado) {
        return repository.save(resultado);
    }

    public boolean deleteById(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}