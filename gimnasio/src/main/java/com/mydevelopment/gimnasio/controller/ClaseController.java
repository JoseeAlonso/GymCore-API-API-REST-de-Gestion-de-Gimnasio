package com.mydevelopment.gimnasio.controller;

import com.mydevelopment.gimnasio.dto.ClaseResponseDTO;
import com.mydevelopment.gimnasio.model.Clase;
import com.mydevelopment.gimnasio.service.ClaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// El "Tag" permite agrupar visualmente los endpoints por controlador con un nombre descriptivo
@Tag(name = "Clases", description = "Operaciones sobre clases del gimnasio")
@RestController
public class ClaseController {

    private final ClaseService claseService;

    public ClaseController(ClaseService claseService) {
        this.claseService = claseService;
    }

    //"Operation" Añade una descripción corta de qué hace este endpoint
    @Operation(summary = "Crea una nueva clase")
    @PostMapping("/clases")
    public ResponseEntity<ClaseResponseDTO> crearClase(@Valid @RequestBody Clase clase) {

        ClaseResponseDTO guardada = claseService.crearClase(clase);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardada);

    }

    @Operation(summary = "Obtiene información sobre las clases")
    @GetMapping("/clases")
    public ResponseEntity<List<ClaseResponseDTO>> obtenerClases() {
        List<ClaseResponseDTO> clases = claseService.obtenerClases();
        return ResponseEntity.ok(clases);
    }

    @Operation(summary = "Obtiene una clase por su id")
    @GetMapping("/clases/{id}")
    public ResponseEntity<ClaseResponseDTO> obtenerClasePorId(@PathVariable Long id) {
        ClaseResponseDTO clase = claseService.obtenerClasePorId(id);
        return ResponseEntity.ok(clase);
    }

    @Operation(summary = "Modifica la información de la clase")
    @PutMapping("/clases/{id}")
    public ResponseEntity<ClaseResponseDTO> modificarClase(@PathVariable Long id, @Valid  @RequestBody Clase datosNuevos) {

        ClaseResponseDTO actualizada = claseService.modificarClase(id, datosNuevos);
        return ResponseEntity.ok(actualizada);

    }

    @Operation(summary = "Elimina una clase")
    @DeleteMapping("/clases/{id}")
    public ResponseEntity<Void> eliminarClase(@PathVariable Long id) {

        claseService.eliminarClase(id);
        return ResponseEntity.noContent().build();
    }
}