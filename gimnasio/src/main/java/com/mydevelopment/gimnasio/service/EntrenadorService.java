package com.mydevelopment.gimnasio.service;

import com.mydevelopment.gimnasio.exception.ResourceNotFoundException;
import com.mydevelopment.gimnasio.model.Entrenador;
import com.mydevelopment.gimnasio.repository.EntrenadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EntrenadorService {

    private final EntrenadorRepository entrenadorRepository;

    public EntrenadorService(EntrenadorRepository entrenadorRepository) {
        this.entrenadorRepository = entrenadorRepository;
    }

    public Entrenador crearEntrenador(Entrenador entrenador){

        return entrenadorRepository.save(entrenador);
    }

    public List<Entrenador> obtenerEntrenadores (){

        return entrenadorRepository.findAll();
    }

    public Entrenador modificarEntrenador (Long id, Entrenador datosNuevos){

        Entrenador entrenador = entrenadorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("El entrenador no existe"));

        entrenador.setNombre(datosNuevos.getNombre());
        entrenador.setEspecialidad(datosNuevos.getEspecialidad());
        entrenador.setAniosExperiencia(datosNuevos.getAniosExperiencia());

        return entrenadorRepository.save(entrenador);
    }

    public void eliminarEntrenador(Long id){

        Entrenador entrenador = entrenadorRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No existe el entrenador"));

        entrenadorRepository.delete(entrenador);
    }
}
