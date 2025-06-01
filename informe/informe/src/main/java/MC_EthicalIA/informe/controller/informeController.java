package MC_EthicalIA.informe.controller;

import MC_EthicalIA.informe.model.informe;
import MC_EthicalIA.informe.service.informeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/api/v1/informes")
public class informeController {

    @Autowired
    private informeService service;


    @GetMapping
    public ResponseEntity<List<informe>> listar() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<informe> obtenerPorId(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/texto/{textoId}")
    public ResponseEntity<List<informe>> listarPorTexto(@PathVariable Long textoId) {
        return ResponseEntity.ok(service.findByTextoId(textoId));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<informe>> buscarPorResumen(@RequestParam String palabra) {
        return ResponseEntity.ok(service.buscarPorResumen(palabra));
    }

    @GetMapping("/recientes")
    public ResponseEntity<List<informe>> recientes() {
        return ResponseEntity.ok(service.listarRecientes());
    }

    @PostMapping
    public ResponseEntity<informe> crear(@RequestBody informe informe) {
        return ResponseEntity.ok(service.save(informe));
    }

    @PutMapping("/{id}")
    public ResponseEntity<informe> actualizar(@PathVariable Long id, @RequestBody informe actualizado) {
        return service.findById(id)
                .map(i -> {
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
