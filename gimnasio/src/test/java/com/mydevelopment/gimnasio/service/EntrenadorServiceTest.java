package com.mydevelopment.gimnasio.service;

import com.mydevelopment.gimnasio.exception.ResourceNotFoundException;
import com.mydevelopment.gimnasio.model.Entrenador;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EntrenadorServiceTest {

    @Mock
    private EntrenadorRepository entrenadorRepository;

    @InjectMocks
    private EntrenadorService entrenadorService;

    @Test
    void deberiaCrearUnEntrenador(){

        Entrenador entrenadorEntrada = new Entrenador(null, "Javier Gómez", "Calistenia", 10);
        Entrenador entrenadorGuardado = new Entrenador(1L, "Javier Gómez", "Calistenia", 10);

        when(entrenadorRepository.save(entrenadorEntrada)).thenReturn(entrenadorGuardado);

        Entrenador resultado = entrenadorService.crearEntrenador(entrenadorEntrada);

        assertEquals(1L, resultado.getId());
        assertEquals("Javier Gómez", resultado.getNombre());
        assertEquals("Calistenia", resultado.getEspecialidad());
        assertEquals(10, resultado.getAniosExperiencia());
    }

    @Test
    void deberiaLanzarExcepcionSiEntrenadorNoExiste(){

        when(entrenadorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> entrenadorService.modificarEntrenador(999L, new Entrenador()));
    }

    @Test
    void deberiaObtenerTodosLosEntrenadores(){

        List<Entrenador> listaSimulada = List.of(
                new Entrenador(1L, "Maria", "Hipertrofia", 15),
                new Entrenador(2L, "Sergio", "Calistenia", 12)
        );

        when(entrenadorRepository.findAll()).thenReturn(listaSimulada);

        List<Entrenador> resultado = entrenadorService.obtenerEntrenadores();

        assertEquals(2, resultado.size());
        assertEquals("Sergio", resultado.get(1).getNombre());
    }

    @Test
    void deberiaModificarUnEntrenador(){
        Entrenador entrenadorExistente = new Entrenador(1L, "Javier Gómez", "Calistenia", 10);
        Entrenador datosNuevos = new Entrenador(null, "Javier Actualizado", "Yoga", 12);

        when(entrenadorRepository.findById(1L)).thenReturn(Optional.of(entrenadorExistente));
        when(entrenadorRepository.save(entrenadorExistente)).thenReturn(entrenadorExistente);

        Entrenador resultado = entrenadorService.modificarEntrenador(1L, datosNuevos);

        assertEquals("Javier Actualizado", resultado.getNombre());
        assertEquals("Yoga", resultado.getEspecialidad());
        assertEquals(12, resultado.getAniosExperiencia());
    }

    @Test
    void deberiaEliminarUnEntrenador(){

        Entrenador entrenador = new Entrenador(1L, "Maria", "Calistenia", 15);

        when(entrenadorRepository.findById(1L)).thenReturn(Optional.of(entrenador));

        entrenadorService.eliminarEntrenador(1L);

        verify(entrenadorRepository).delete(entrenador);

    }

    @Test
    void deberiaLanzarExcepcionAlEliminarEntrenadorInexistente(){

        when(entrenadorRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> entrenadorService.eliminarEntrenador(999L));

    }
}
