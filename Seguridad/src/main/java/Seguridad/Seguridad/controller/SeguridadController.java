package Seguridad.Seguridad.controller;

import Seguridad.Seguridad.model.Seguridad;
import Seguridad.Seguridad.service.SeguridadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/seguridad")
public class SeguridadController {

    private SeguridadService service;

    public SeguridadController(SeguridadService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Seguridad>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Seguridad> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Seguridad> crear(@RequestBody Seguridad nuevo) {
        return ResponseEntity.ok(service.save(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seguridad> actualizar(@PathVariable Long id, @RequestBody Seguridad actualizado) {
        return service.findById(id)
                .map(s -> {
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
