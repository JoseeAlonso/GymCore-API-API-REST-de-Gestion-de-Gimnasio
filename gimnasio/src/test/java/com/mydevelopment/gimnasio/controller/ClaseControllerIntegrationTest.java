package com.mydevelopment.gimnasio.controller;

import com.mydevelopment.gimnasio.model.Clase;
import com.mydevelopment.gimnasio.model.Cliente;
import com.mydevelopment.gimnasio.model.Entrenador;
import com.mydevelopment.gimnasio.repository.ClaseRepository;
import com.mydevelopment.gimnasio.repository.ClienteRepository;
import com.mydevelopment.gimnasio.repository.EntrenadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ClaseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClaseRepository claseRepository;

    private Entrenador entrenadorGuardado;
    private Cliente clienteGuardado;
    private Clase claseGuardada;

    @BeforeEach
    void setUp() {

        entrenadorGuardado = entrenadorRepository.save(new Entrenador(null, "Roberto", "Yoga", 12));

        clienteGuardado = clienteRepository.save(new Cliente(null, "Jose", "jose@gmail.com", 25));

        claseGuardada = claseRepository.save(new Clase(null, "Yoga", "Lunes 9:00", entrenadorGuardado, List.of(clienteGuardado)));
    }

    @Test
    void deberiaCrearUnaClase() throws Exception {

        Clase claseNueva = new Clase(null, "Yoga", "Lunes 9:00", entrenadorGuardado, List.of(clienteGuardado));

        String jsonBody = objectMapper.writeValueAsString(claseNueva);

        mockMvc.perform(post("/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Yoga")).andExpect(jsonPath("$.horario").value("Lunes 9:00"))
                .andExpect(jsonPath("$.nombreEntrenador").value("Roberto"))
                .andExpect(jsonPath("$.nombresClientes").value("Jose"));
    }

    @Test
    void deberiaLanzarExcepcionPorIdEntrenadorNoExistente() throws Exception {

        Clase claseNueva = new Clase(null, "Yoga", "Lunes 9:00", new Entrenador(999L, "Roberto", "Yoga", 12), List.of(clienteGuardado));

        String jsonBody = objectMapper.writeValueAsString(claseNueva);

        mockMvc.perform(post("/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("El entrenador no existe"));

    }

    @Test
    void deberiaObtenerUnaClasePorId() throws Exception {
        mockMvc.perform(get("/clases/" + claseGuardada.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Yoga"));
    }

    @Test
    void deberiaLanzarExcepcionPorIdClaseNoExistente() throws Exception {

        mockMvc.perform(get("/clases/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No se encuentra la clase"));
    }

    @Test
    void deberiaModificarUnaClase() throws Exception {

        Clase claseNueva = new Clase(null, "Calistenia", "Martes 11:00", entrenadorGuardado, List.of(clienteGuardado));

        String jsonBody = objectMapper.writeValueAsString(claseNueva);

        mockMvc.perform(put("/clases/" + claseGuardada.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Calistenia"))
                .andExpect(jsonPath("$.horario").value("Martes 11:00"))
                .andExpect(jsonPath("$.nombreEntrenador").value("Roberto"));
    }

    @Test
    void deberiaEliminarUnaClase() throws Exception{
        mockMvc.perform(delete("/clases/" + claseGuardada.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deberiaLanzarExcepcionSiLaClaseAEliminarNoExiste() throws Exception{
        mockMvc.perform(delete("/clases/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe la clase"));
    }

    @Test
    void deberiaFallarValidacionSiNombreEstaVacio() throws Exception {
        Clase claseInvalida = new Clase(null, "", "Lunes 9:00", entrenadorGuardado, List.of(clienteGuardado));

        String jsonBody = objectMapper.writeValueAsString(claseInvalida);

        mockMvc.perform(post("/clases")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre no puede estar vacío"));
    }
}

