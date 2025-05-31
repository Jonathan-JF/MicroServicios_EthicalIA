package MicroServcios_EthicalIA.Coherencia.service;


import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import MicroServcios_EthicalIA.Coherencia.repository.CoherenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CoherenciaService {

    private final CoherenciaRepository coherenciaRepository;

    @Autowired
    public CoherenciaService(CoherenciaRepository coherenciaRepository) {
        this.coherenciaRepository = coherenciaRepository;
    }

    // Crear o actualizar una Coherencia
    public Coherencia saveCoherencia(Coherencia coherencia) {
        return coherenciaRepository.save(coherencia);
    }

    // Obtener todas las Coherencias
    public List<Coherencia> getAllCoherencias() {
        return coherenciaRepository.findAll();
    }

    // Obtener una Coherencia por ID
    public Optional<Coherencia> getCoherenciaById(Long id) {
        return coherenciaRepository.findById(id);
    }

    // Eliminar una Coherencia por ID
    public void deleteCoherencia(Long id) {
        coherenciaRepository.deleteById(id);
    }

    // Buscar Coherencias por texto
    public List<Coherencia> searchCoherenciasByTexto(String palabraClave) {
        return coherenciaRepository.findByTextoContainingIgnoreCase(palabraClave);
    }

    // Buscar Coherencias por puntuación de coherencia
    public List<Coherencia> getCoherenciasByPuntuacion(double puntuacion) {
        return coherenciaRepository.findByPuntuacionCoherenciaGreaterThanEqual(puntuacion);
    }

    // Buscar Coherencias por rango de fechas
    public List<Coherencia> getCoherenciasByFechaAnalisisBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return coherenciaRepository.findByFechaAnalisisBetween(fechaInicio, fechaFin);
    }

    // Buscar Coherencias por sugerencias
    public List<Coherencia> searchCoherenciasBySugerencias(String palabraClave) {
        return coherenciaRepository.findBySugerenciasContainingIgnoreCase(palabraClave);
    }

    // Obtener las Coherencias más recientes
    public List<Coherencia> getRecentCoherencias() {
        return coherenciaRepository.findTop5ByOrderByFechaAnalisisDesc();
    }

    // Buscar coherencias con puntuación mayor o igual a un valor y fecha posterior a un determinado momento
    public List<Coherencia> getCoherenciasByPuntuacionAndFechaAnalisis(double puntuacion, LocalDateTime fechaAnalisis) {
        return coherenciaRepository.findByPuntuacionCoherenciaGreaterThanEqualAndFechaAnalisisAfter(puntuacion, fechaAnalisis);
    }
}
