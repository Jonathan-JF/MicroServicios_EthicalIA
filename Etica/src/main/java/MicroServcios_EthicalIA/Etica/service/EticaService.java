package MicroServcios_EthicalIA.Etica.service;

import MicroServcios_EthicalIA.Etica.model.Etica;
import MicroServcios_EthicalIA.Etica.repository.EticaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EticaService {

    private final EticaRepository eticaRepository;

    @Autowired
    public EticaService(EticaRepository eticaRepository) {
        this.eticaRepository = eticaRepository;
    }

    public Etica saveEtica(Etica etica) {
        return eticaRepository.save(etica);
    }

    public List<Etica> getAllEticas() {
        return eticaRepository.findAll();
    }

    public Optional<Etica> getEticaById(Long id) {
        return eticaRepository.findById(id);
    }

    public void deleteEtica(Long id) {
        eticaRepository.deleteById(id);
    }

    public List<Etica> searchEticasByTexto(String palabraClave) {
        return eticaRepository.findByTextoContainingIgnoreCase(palabraClave);
    }

    public List<Etica> getEticasByLenguajeInclusivo(boolean lenguajeInclusivo) {
        return eticaRepository.findByLenguajeInclusivo(lenguajeInclusivo);
    }

    public List<Etica> getEticasByLenguajeOfensivo(boolean lenguajeOfensivo) {
        return eticaRepository.findByLenguajeOfensivo(lenguajeOfensivo);
    }

    public List<Etica> getEticasByFechaAnalisisBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return eticaRepository.findByFechaAnalisisBetween(fechaInicio, fechaFin);
    }

    public List<Etica> searchEticasByObservaciones(String palabraClave) {
        return eticaRepository.findByObservacionesContainingIgnoreCase(palabraClave);
    }

    public List<Etica> getRecentEticas() {
        return eticaRepository.listarEticasRecientes();
    }

    public List<Etica> getEticasByLenguajeInclusivoAndOfensivo(boolean lenguajeInclusivo, boolean lenguajeOfensivo) {
        return eticaRepository.findByLenguajeInclusivoAndLenguajeOfensivo(lenguajeInclusivo, lenguajeOfensivo);
    }
}
