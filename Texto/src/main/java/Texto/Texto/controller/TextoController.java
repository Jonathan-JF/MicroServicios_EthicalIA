package Texto.Texto.controller;

import Texto.Texto.model.Texto;
import Texto.Texto.service.TextoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/textos")
public class TextoController {

    private final TextoService service;

    public TextoController(TextoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Texto>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Texto> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Texto> crear(@RequestBody Texto nuevo) {
        return ResponseEntity.ok(service.save(nuevo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Texto> actualizar(@PathVariable Long id, @RequestBody Texto actualizado) {
        return service.findById(id)
                .map(t -> {
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
