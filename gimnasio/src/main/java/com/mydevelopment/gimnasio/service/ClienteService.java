package com.mydevelopment.gimnasio.service;

import com.mydevelopment.gimnasio.exception.ResourceNotFoundException;
import com.mydevelopment.gimnasio.model.Cliente;
import com.mydevelopment.gimnasio.repository.ClienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository){
        this.clienteRepository = clienteRepository;
    }

    public Cliente crearCliente (Cliente cliente){
        return clienteRepository.save(cliente);
    }

    public List<Cliente> obtenerClientes(){
        return clienteRepository.findAll();
    }

    public Cliente modificarCliente(Long id, Cliente datosNuevos){

        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El cliente no existe"));

        cliente.setNombre(datosNuevos.getNombre());
        cliente.setEmail(datosNuevos.getEmail());
        cliente.setEdad(datosNuevos.getEdad());

        return clienteRepository.save(cliente);

    }

    public void eliminarCliente (Long id){

        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El cliente no existe"));

        clienteRepository.delete(cliente);
    }

}
