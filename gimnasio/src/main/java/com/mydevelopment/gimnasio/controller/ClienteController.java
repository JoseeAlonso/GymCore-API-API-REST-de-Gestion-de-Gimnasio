package com.mydevelopment.gimnasio.controller;

import com.mydevelopment.gimnasio.model.Cliente;
import com.mydevelopment.gimnasio.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Tag(name = "Clientes", description = "Operaciones sobre clientes del gimnasio")
@RestController
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @Operation(summary = "Crea un nuevo cliente")
    @PostMapping("/clientes")
    public ResponseEntity<Cliente> crearCliente (@Valid @RequestBody Cliente cliente){
        Cliente guardado = clienteService.crearCliente(cliente);
        URI ubicacion = URI.create("/clientes/" + guardado.getId());
        return ResponseEntity.created(ubicacion).body(guardado);
    }


    @Operation(summary = "Obtiene información de los clientes")
    @GetMapping("/clientes")
    public ResponseEntity<List<Cliente>> obtenerClientes (){

        List<Cliente> clientes = clienteService.obtenerClientes();
        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Modifica los datos de un cliente")
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long id, @Valid @RequestBody Cliente datosNuevos) {

        Cliente clienteActualizado = clienteService.modificarCliente(id, datosNuevos);

        return ResponseEntity.ok(clienteActualizado);
    }

    @Operation(summary = "Elimina un cliente")
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Void> eliminarCliente (@PathVariable Long id){

        clienteService.eliminarCliente(id);

        return ResponseEntity.noContent().build();

    }

}