package MicroServcios_EthicalIA.Coherencia.service;


import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import MicroServcios_EthicalIA.Coherencia.repository.CoherenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CoherenciaService {
    
    @Autowired
    private  CoherenciaRepository coherenciaRepository;

    public Coherencia saveCoherencia(Coherencia coherencia) {
        return coherenciaRepository.save(coherencia);
    }

    public List<Coherencia> getAllCoherencias() {
        return coherenciaRepository.findAll();
    }

    public Optional<Coherencia> getCoherenciaById(Long id) {
        return coherenciaRepository.findById(id);
    }

    public void deleteCoherencia(Long id) {
        coherenciaRepository.deleteById(id);
    }

    public List<Coherencia> searchCoherenciasByTexto(String palabraClave) {
        return coherenciaRepository.findByTextoContainingIgnoreCase(palabraClave);
    }

    public List<Coherencia> getCoherenciasByPuntuacion(double puntuacion) {
        return coherenciaRepository.findByPuntuacionCoherenciaGreaterThanEqual(puntuacion);
    }

    public List<Coherencia> getCoherenciasByFechaAnalisisBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return coherenciaRepository.findByFechaAnalisisBetween(fechaInicio, fechaFin);
    }

    public List<Coherencia> searchCoherenciasBySugerencias(String palabraClave) {
        return coherenciaRepository.findBySugerenciasContainingIgnoreCase(palabraClave);
    }

    public List<Coherencia> getRecentCoherencias() {
        return coherenciaRepository.findTop5ByOrderByFechaAnalisisDesc();
    }

    public List<Coherencia> getCoherenciasByPuntuacionAndFechaAnalisis(double puntuacion, LocalDateTime fechaAnalisis) {
        return coherenciaRepository.findByPuntuacionCoherenciaGreaterThanEqualAndFechaAnalisisAfter(puntuacion, fechaAnalisis);
    }
}
