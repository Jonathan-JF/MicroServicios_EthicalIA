package MicroServcios_EthicalIA.Etica.controller;

import MicroServcios_EthicalIA.Etica.model.Etica;
import MicroServcios_EthicalIA.Etica.service.EticaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/etica")
public class EticaController {

    private final EticaService eticaService;

    @Autowired
    public EticaController(EticaService eticaService) {
        this.eticaService = eticaService;
    }

    @PostMapping
    public Etica saveEtica(@RequestBody Etica etica) {
        return eticaService.saveEtica(etica);
    }

    @GetMapping
    public List<Etica> getAllEticas() {
        return eticaService.getAllEticas();
    }

    @GetMapping("/{id}")
    public Optional<Etica> getEticaById(@PathVariable Long id) {
        return eticaService.getEticaById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteEtica(@PathVariable Long id) {
        eticaService.deleteEtica(id);
    }

    @GetMapping("/buscar")
    public List<Etica> searchEticas(@RequestParam String texto) {
        return eticaService.searchEticasByTexto(texto);
    }

    @GetMapping("/recientes")
    public List<Etica> getRecentEticas() {
        return eticaService.getRecentEticas();
    }

    @GetMapping("/filtro")
    public List<Etica> filterEticas(
            @RequestParam(required = false) Boolean lenguajeInclusivo,
            @RequestParam(required = false) Boolean lenguajeOfensivo) {
        if (lenguajeInclusivo != null && lenguajeOfensivo != null) {
            return eticaService.getEticasByLenguajeInclusivoAndOfensivo(lenguajeInclusivo, lenguajeOfensivo);
        } else if (lenguajeInclusivo != null) {
            return eticaService.getEticasByLenguajeInclusivo(lenguajeInclusivo);
        } else if (lenguajeOfensivo != null) {
            return eticaService.getEticasByLenguajeOfensivo(lenguajeOfensivo);
        } else {
            return eticaService.getAllEticas();
        }
    }
}
