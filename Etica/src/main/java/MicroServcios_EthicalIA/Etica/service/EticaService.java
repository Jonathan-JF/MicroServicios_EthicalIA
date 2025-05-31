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

    // Crear o actualizar una Etica
    public Etica saveEtica(Etica etica) {
        return eticaRepository.save(etica);
    }

    // Obtener todas las Eticas
    public List<Etica> getAllEticas() {
        return eticaRepository.findAll();
    }

    // Obtener una Etica por ID
    public Optional<Etica> getEticaById(Long id) {
        return eticaRepository.findById(id);
    }

    // Eliminar una Etica por ID
    public void deleteEtica(Long id) {
        eticaRepository.deleteById(id);
    }

    // Buscar Eticas por palabra clave en texto
    public List<Etica> searchEticasByTexto(String palabraClave) {
        return eticaRepository.findByTextoContainingIgnoreCase(palabraClave);
    }

    // Buscar Eticas por lenguaje inclusivo
    public List<Etica> getEticasByLenguajeInclusivo(boolean lenguajeInclusivo) {
        return eticaRepository.findByLenguajeInclusivo(lenguajeInclusivo);
    }

    // Buscar Eticas por lenguaje ofensivo
    public List<Etica> getEticasByLenguajeOfensivo(boolean lenguajeOfensivo) {
        return eticaRepository.findByLenguajeOfensivo(lenguajeOfensivo);
    }

    // Buscar Eticas en un rango de fechas
    public List<Etica> getEticasByFechaAnalisisBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return eticaRepository.findByFechaAnalisisBetween(fechaInicio, fechaFin);
    }

    // Buscar Eticas por palabra clave en observaciones
    public List<Etica> searchEticasByObservaciones(String palabraClave) {
        return eticaRepository.findByObservacionesContainingIgnoreCase(palabraClave);
    }

    // Obtener Eticas más recientes
    public List<Etica> getRecentEticas() {
        return eticaRepository.listarEticasRecientes();
    }

    // Buscar Eticas con lenguaje inclusivo y ofensivo a la vez
    public List<Etica> getEticasByLenguajeInclusivoAndOfensivo(boolean lenguajeInclusivo, boolean lenguajeOfensivo) {
        return eticaRepository.findByLenguajeInclusivoAndLenguajeOfensivo(lenguajeInclusivo, lenguajeOfensivo);
    }
}
