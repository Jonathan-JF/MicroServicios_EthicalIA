package MC_EthicalIA.originalidad.controller;

import MC_EthicalIA.originalidad.model.Originalidad;
import MC_EthicalIA.originalidad.service.OriginalidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/originalidad")
public class OriginalidadController {

    private final OriginalidadService service;

    public OriginalidadController(OriginalidadService service) {
        this.service = service;
    }

    // 📄 Listar todos
    @GetMapping
    public ResponseEntity<List<Originalidad>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    // 🔍 Buscar por texto
    @GetMapping("/buscar")
    public ResponseEntity<List<Originalidad>> buscarPorTexto(@RequestParam String texto) {
        return ResponseEntity.ok(service.findByTexto(texto));
    }

    // ⚠️ Buscar por porcentaje de plagio mayor a
    @GetMapping("/plagio")
    public ResponseEntity<List<Originalidad>> buscarPorPlagio(@RequestParam double min) {
        return ResponseEntity.ok(service.findByPlagioMayorA(min));
    }

    // 🧠 Buscar por plagio y palabra
    @GetMapping("/filtrado")
    public ResponseEntity<List<Originalidad>> buscarAvanzado(
            @RequestParam double limite,
            @RequestParam String palabra) {
        return ResponseEntity.ok(service.buscarPorPlagioYTexto(limite, palabra));
    }

    // ➕ Crear nuevo
    @PostMapping
    public ResponseEntity<Originalidad> crear(@RequestBody Originalidad resultado) {
        return ResponseEntity.ok(service.save(resultado));
    }

    // 📝 Actualizar por ID
    @PutMapping("/{id}")
    public ResponseEntity<Originalidad> actualizar(
            @PathVariable Long id,
            @RequestBody Originalidad actualizado) {
        return service.findById(id)
                .map(r -> {
                    actualizado.setId(id);
                    return ResponseEntity.ok(service.save(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ❌ Eliminar por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (service.deleteById(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
