package ReporteGlobal.ReporteGlobal.controller;

import com.ethicaia.model.ReporteGlobal;
import com.ethicaia.service.ReporteGlobalService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reportes")
public class ReporteGlobalController {

    private final ReporteGlobalService service;

    public ReporteGlobalController(ReporteGlobalService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<ReporteGlobal>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteGlobal> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ReporteGlobal> crear(@RequestBody ReporteGlobal nuevo) {
        return ResponseEntity.ok(service.save(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReporteGlobal> actualizar(@PathVariable Long id, @RequestBody ReporteGlobal actualizado) {
        return service.findById(id)
                .map(r -> {
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
