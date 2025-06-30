package Historial.Historial.controller;

import com.ethicaia.model.Historial;
import com.ethicaia.service.HistorialService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/historial")
public class HistorialController {

    private final HistorialService service;

    public HistorialController(HistorialService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Historial>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Historial> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Historial> crear(@RequestBody Historial nuevo) {
        return ResponseEntity.ok(service.save(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Historial> actualizar(@PathVariable Long id, @RequestBody Historial actualizado) {
        return service.findById(id)
                .map(h -> {
                    actualizado.setId(id);
                    return ResponseEntity.ok(service.save(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        return service.deleteById(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
