package com.mydevelopment.gimnasio.service;

import com.mydevelopment.gimnasio.dto.ClaseResponseDTO;
import com.mydevelopment.gimnasio.exception.ResourceNotFoundException;
import com.mydevelopment.gimnasio.model.Clase;
import com.mydevelopment.gimnasio.model.Cliente;
import com.mydevelopment.gimnasio.model.Entrenador;
import com.mydevelopment.gimnasio.repository.ClaseRepository;
import com.mydevelopment.gimnasio.repository.ClienteRepository;
import com.mydevelopment.gimnasio.repository.EntrenadorRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClaseServiceTest {

    @Mock
    private ClaseRepository claseRepository;

    @Mock
    private EntrenadorRepository entrenadorRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClaseService claseService;

    @Test
    void deberiaCrearUnaClase(){

        Entrenador entrenador = new Entrenador(1L, "Roberto", "Yoga", 8);
        Cliente cliente1 = new Cliente(1L, "Ana", "ana@email.com", 25);
        Cliente cliente2 = new Cliente(2L, "Luis", "luis@email.com", 30);

        Clase claseEntrada = new Clase(null, "Yoga matutino", "Lunes 9:00", new Entrenador(1L, null, null, null), List.of(new Cliente(1L, null, null, null), new Cliente(2L, null, null, null)));

        Clase claseGuardada = new Clase(1L, "Yoga matutino", "Lunes 9:00", entrenador, List.of(cliente1, cliente2));

        when(entrenadorRepository.findById(1L)).thenReturn(Optional.of(entrenador));
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente1));
        when(clienteRepository.findById(2L)).thenReturn(Optional.of(cliente2));
        when(claseRepository.save(claseEntrada)).thenReturn(claseGuardada);

        ClaseResponseDTO resultado = claseService.crearClase(claseEntrada);

        assertEquals("Yoga matutino", resultado.getNombre());
        assertEquals("Roberto", resultado.getNombreEntrenador());
        assertEquals(2, resultado.getNombresClientes().size());

    }

    @Test
    void deberiaObtenerTodasLasClases(){
        Entrenador entrenador = new Entrenador(1L, "Roberto", "Yoga", 6);
         Cliente cliente1 = new Cliente(1L, "Jose", "jose@gmail.com", 30);
        Cliente cliente2 = new Cliente(2L, "Maria", "maria@gmail.com", 29);

        List<Cliente> clientes = List.of(cliente1,cliente2);

         Clase clase = new Clase(1L, "Yoga", "Lunes 9:00", entrenador, clientes);

         when(claseRepository.findAll()).thenReturn(List.of(clase));

         List<ClaseResponseDTO> resultado = claseService.obtenerClases();

         assertEquals(1, resultado.size());
         assertEquals("Yoga", resultado.get(0).getNombre());
         assertEquals("Roberto", resultado.get(0).getNombreEntrenador());
    }

    @Test
    void deberiaModificarUnaClase(){

        Entrenador entrenadorExistente = new Entrenador(1L, "Roberto", "Yoga", 12);
        Cliente clienteExistente1 = new Cliente(1L, "Jose", "jose@gmail.com", 30);
        Cliente clienteExistente2 = new Cliente(2L, "Maria", "maria@gmail.com", 29);

        List<Cliente> clientesExistentes = List.of(clienteExistente1,clienteExistente2);

        Clase claseExistente = new Clase(1L, "Yoga", "Lunes 9:00", entrenadorExistente, clientesExistentes);

        Entrenador entrenadorNuevo = new Entrenador(2L, "Maria", "Calistenia", 15);
        Cliente clienteNuevo1 = new Cliente(3L, "Jorge", "jorge@gmail.com", 35);
        Cliente clienteNuevo2 = new Cliente(4L, "Sara", "sara@gmail.com", 32);

        List<Cliente> clientesNuevos = List.of(clienteNuevo1,clienteNuevo2);

        Clase claseNueva = new Clase(null, "Calistenia", "Martes 9:00", entrenadorNuevo, clientesNuevos);

        when(claseRepository.findById(1L)).thenReturn(Optional.of(claseExistente));
        when(entrenadorRepository.findById(2L)).thenReturn(Optional.of(entrenadorNuevo));
        when(clienteRepository.findById(3L)).thenReturn(Optional.of(clienteNuevo1));
        when(clienteRepository.findById(4L)).thenReturn(Optional.of(clienteNuevo2));
        when(claseRepository.save(claseExistente))
                .thenReturn(claseExistente);

        ClaseResponseDTO claseResponseDTOResultado = claseService.modificarClase(1L, claseNueva);

        assertEquals("Calistenia", claseResponseDTOResultado.getNombre());
        assertEquals("Martes 9:00", claseResponseDTOResultado.getHorario());
        assertEquals("Jorge", claseResponseDTOResultado.getNombresClientes().get(0));
        assertEquals("Maria", claseResponseDTOResultado.getNombreEntrenador());
    }

    @Test
    void deberiaEliminarUnaClase(){

        Entrenador entrenador = new Entrenador(1L, "Roberto", "Yoga", 12);
        Cliente cliente1 = new Cliente(1L, "Jose", "jose@gmail.com", 30);
        Cliente cliente2 = new Cliente(2L, "Maria", "maria@gmail.com", 29);

        List<Cliente> clientes = List.of(cliente1,cliente2);

        Clase clase = new Clase(1L, "Yoga", "Lunes 9:00", entrenador, clientes);

        when(claseRepository.findById(1L)).thenReturn(Optional.of(clase));

        claseService.eliminarClase(1L);

        verify(claseRepository).findById(1L);
        verify(claseRepository).delete(clase);
        verifyNoMoreInteractions(claseRepository);

    }

    @Test
    void deberiaLanzarExcepcionSiLaClaseNoExiste() {

        when(claseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> claseService.eliminarClase(1L));

        verify(claseRepository, never()).delete(any());
    }

    @Test
    void deberiaLanzarExcepcionSiAlModificarClaseNoExiste(){

    Clase datosNuevos = new Clase(null, "Cualquier Nombre", "Cualquier Horario", null, null);

    when(claseRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> claseService.modificarClase(1L, datosNuevos));

    verify(claseRepository, never()).save(any());

    }
}
