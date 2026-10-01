package com.mydevelopment.gimnasio.controller;

import com.mydevelopment.gimnasio.model.Entrenador;
import com.mydevelopment.gimnasio.service.EntrenadorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Entrenadores", description = "Operaciones sobre entrenadores del gimnasio")
@RestController
public class EntrenadorController {

    private final EntrenadorService entrenadorService;

    public EntrenadorController(EntrenadorService entrenadorService) {
        this.entrenadorService = entrenadorService;
    }

    @Operation(summary = "Crea un nuevo entrenador")
    @PostMapping("/entrenadores")
    public ResponseEntity<Entrenador> crearEntrenador (@Valid @RequestBody Entrenador entrenador){
        Entrenador guardado = entrenadorService.crearEntrenador(entrenador);
        URI ubicacion = URI.create("/entrenadores/" + guardado.getId());

        return ResponseEntity.created(ubicacion).body(guardado);
    }

    @Operation(summary = "Obtiene información de los entrenadores")
    @GetMapping("/entrenadores")
    public ResponseEntity<List<Entrenador>> obtenerEntrenadores (){

        List<Entrenador> entrenadores = entrenadorService.obtenerEntrenadores();
        return ResponseEntity.ok(entrenadores);
    }

    @Operation(summary = "Modifica los datos de un entrenador")
    @PutMapping("/entrenadores/{id}")
    public ResponseEntity<Entrenador> modificarEntrenador(@PathVariable Long id, @Valid @RequestBody Entrenador datosNuevos){

        Entrenador entrenadorActualizado = entrenadorService.modificarEntrenador(id, datosNuevos);

        return ResponseEntity.ok(entrenadorActualizado);

    }

    @Operation(summary = "Elimina un entrenador")
    @DeleteMapping("/entrenadores/{id}")
    public ResponseEntity<Void> borrarEntrenador(@PathVariable Long id){

        entrenadorService.eliminarEntrenador(id);
        return ResponseEntity.noContent().build();

    }
}