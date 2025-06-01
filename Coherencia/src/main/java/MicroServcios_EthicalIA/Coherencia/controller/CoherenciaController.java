package MicroServcios_EthicalIA.Coherencia.controller;

import MicroServcios_EthicalIA.Coherencia.model.Coherencia;
import MicroServcios_EthicalIA.Coherencia.service.CoherenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/coherencia")
public class CoherenciaController {
    
    @Autowired
    private  CoherenciaService coherenciaService;

    
    @PostMapping
    public Coherencia saveCoherencia(@RequestBody Coherencia coherencia) {
        return coherenciaService.saveCoherencia(coherencia);
    }

    @GetMapping
    public List<Coherencia> getAllCoherencias() {
        return coherenciaService.getAllCoherencias();
    }

    @GetMapping("/{id}")
    public Optional<Coherencia> getCoherenciaById(@PathVariable Long id) {
        return coherenciaService.getCoherenciaById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteCoherencia(@PathVariable Long id) {
        coherenciaService.deleteCoherencia(id);
    }

    @GetMapping("/buscar")
    public List<Coherencia> searchCoherencias(@RequestParam String texto) {
        return coherenciaService.searchCoherenciasByTexto(texto);
    }

    @GetMapping("/filtro")
    public List<Coherencia> filterCoherencias(
            @RequestParam(required = false) Double puntuacion,
            @RequestParam(required = false) LocalDateTime fechaInicio,
            @RequestParam(required = false) LocalDateTime fechaFin) {

        if (puntuacion != null) {
            return coherenciaService.getCoherenciasByPuntuacion(puntuacion);
        }

        if (fechaInicio != null && fechaFin != null) {
            return coherenciaService.getCoherenciasByFechaAnalisisBetween(fechaInicio, fechaFin);
        }

        return coherenciaService.getAllCoherencias();
    }

    @GetMapping("/recientes")
    public List<Coherencia> getRecentCoherencias() {
        return coherenciaService.getRecentCoherencias();
    }
}
