package com.mydevelopment.gimnasio.service;

import com.mydevelopment.gimnasio.exception.ResourceNotFoundException;
import com.mydevelopment.gimnasio.model.Cliente;
import com.mydevelopment.gimnasio.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void deberiaCrearUnCliente(){
        Cliente clienteEntrada = new Cliente(null, "Jose Alonso", "jose@gmail.com", 30);
        Cliente clienteGuardado = new Cliente(1L, "Jose Alonso", "jose@gmail.com", 30);

        when(clienteRepository.save(clienteEntrada)).thenReturn(clienteGuardado);

        Cliente resultado = clienteService.crearCliente(clienteEntrada);

        assertEquals(1L, resultado.getId());
        assertEquals("Jose Alonso", resultado.getNombre());
        assertEquals("jose@gmail.com", resultado.getEmail());
        assertEquals(30, resultado.getEdad());
    }

    @Test
    void deberiaLanzarExcepcionSiClienteNoExiste(){

        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.modificarCliente(999L, new Cliente()));
    }

    @Test
    void deberiaObtenerTodosLosClientes(){
        List<Cliente> listaSimulada = List.of(
                new Cliente(1L, "Ana", "ana@gmail.com", 25),
                new Cliente(2L, "Luis", "luis@gmail.com", 30)
        );

        when(clienteRepository.findAll()).thenReturn(listaSimulada);

        List<Cliente> resultado = clienteService.obtenerClientes();

        assertEquals(2, resultado.size());
        assertEquals("Ana", resultado.get(0).getNombre());
    }

    @Test
    void deberiaModificarUnCliente(){
        Cliente clienteExistente = new Cliente(1L, "Jose Alonso", "jose@gmail.com", 30);
        Cliente datosNuevos = new Cliente(null, "Jose Actualizado", "jose.nuevo@gmail.com", 32);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.save(clienteExistente)).thenReturn(clienteExistente);

        Cliente resultado = clienteService.modificarCliente(1L, datosNuevos);

        assertEquals("Jose Actualizado", resultado.getNombre());
        assertEquals("jose.nuevo@gmail.com", resultado.getEmail());
        assertEquals(32, resultado.getEdad());
    }

    @Test
    void deberiaEliminarUnCliente(){
        Cliente cliente = new Cliente(1L, "Ana", "ana@email.com", 25);

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.eliminarCliente(1L);

        verify(clienteRepository).delete(cliente);
    }

    @Test
    void deberiaLanzarExcepcionAlEliminarClienteInexistente(){
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> clienteService.eliminarCliente(999L));
    }
}
