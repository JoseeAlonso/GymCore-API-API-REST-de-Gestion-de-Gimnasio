package com.mydevelopment.gimnasio.service;

import com.mydevelopment.gimnasio.dto.ClaseResponseDTO;
import com.mydevelopment.gimnasio.exception.ResourceNotFoundException;
import com.mydevelopment.gimnasio.model.Clase;
import com.mydevelopment.gimnasio.model.Cliente;
import com.mydevelopment.gimnasio.model.Entrenador;
import com.mydevelopment.gimnasio.repository.ClaseRepository;
import com.mydevelopment.gimnasio.repository.ClienteRepository;
import com.mydevelopment.gimnasio.repository.EntrenadorRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClaseService {

    private final ClaseRepository claseRepository;
    private final ClienteRepository clienteRepository;
    private final EntrenadorRepository entrenadorRepository;

    public ClaseService(ClaseRepository claseRepository, ClienteRepository clienteRepository, EntrenadorRepository entrenadorRepository) {
        this.claseRepository = claseRepository;
        this.clienteRepository = clienteRepository;
        this.entrenadorRepository = entrenadorRepository;
    }

    public ClaseResponseDTO crearClase(Clase clase) {
        Entrenador entrenadorExistente = entrenadorRepository.findById(clase.getEntrenador().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El entrenador no existe"));

        List<Cliente> clientesExistentes = clase.getClientes().stream()
                .map(cliente -> clienteRepository.findById(cliente.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("El cliente no existe")))
                .collect(Collectors.toCollection(ArrayList::new));

        clase.setEntrenador(entrenadorExistente);
        clase.setClientes(clientesExistentes);

        Clase guardada = claseRepository.save(clase);
        return convertirADTO(guardada);
    }

    public List<ClaseResponseDTO> obtenerClases() {
        return claseRepository.findAll().stream()
                .map(this::convertirADTO)
                .toList();
    }

    public ClaseResponseDTO obtenerClasePorId(Long id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la clase"));

        return convertirADTO(clase);
    }

    public ClaseResponseDTO modificarClase(Long id, Clase datosNuevos) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encuentra la clase"));

        clase.setNombre(datosNuevos.getNombre());
        clase.setHorario(datosNuevos.getHorario());

        Entrenador entrenadorExistente = entrenadorRepository.findById(datosNuevos.getEntrenador().getId())
                .orElseThrow(() -> new ResourceNotFoundException("El entrenador no existe"));

        List<Cliente> clientesExistentes = datosNuevos.getClientes().stream()
                .map(cliente -> clienteRepository.findById(cliente.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("El cliente no existe")))
                .collect(Collectors.toCollection(ArrayList::new));

        clase.setEntrenador(entrenadorExistente);
        clase.setClientes(clientesExistentes);

        Clase actualizada = claseRepository.save(clase);
        return convertirADTO(actualizada);
    }

    public void eliminarClase(Long id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe la clase"));

        claseRepository.delete(clase);
    }

    private ClaseResponseDTO convertirADTO(Clase clase) {
        String nombreEntrenador = clase.getEntrenador().getNombre();
        List<String> nombresClientes = clase.getClientes().stream()
                .map(Cliente::getNombre)
                .toList();

        return new ClaseResponseDTO(clase.getId(), clase.getNombre(), clase.getHorario(), nombreEntrenador, nombresClientes);
    }
}