package com.mydevelopment.gimnasio.controller;

import com.mydevelopment.gimnasio.model.Entrenador;
import com.mydevelopment.gimnasio.repository.EntrenadorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EntrenadorControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    private Entrenador entrenadorGuardado;

    @BeforeEach
    void setUp() {
        entrenadorGuardado = entrenadorRepository.save(new Entrenador(null, "Roberto", "Yoga", 12));
    }

    @Test
    void deberiaCrearUnEntrenador() throws Exception {
        Entrenador entrenadorNuevo = new Entrenador(null, "Maria", "Calistenia", 8);
        String jsonBody = objectMapper.writeValueAsString(entrenadorNuevo);

        mockMvc.perform(post("/entrenadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Maria"))
                .andExpect(jsonPath("$.especialidad").value("Calistenia"));
    }

    @Test
    void deberiaObtenerLosEntrenadores() throws Exception {
        mockMvc.perform(get("/entrenadores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + entrenadorGuardado.getId() + ")].nombre")
                        .value("Roberto"));
    }

    @Test
    void deberiaModificarUnEntrenador() throws Exception {
        Entrenador datosNuevos = new Entrenador(null, "Roberto Actualizado", "Pilates", 15);
        String jsonBody = objectMapper.writeValueAsString(datosNuevos);

        mockMvc.perform(put("/entrenadores/" + entrenadorGuardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Roberto Actualizado"))
                .andExpect(jsonPath("$.especialidad").value("Pilates"));
    }

    @Test
    void deberiaLanzarExcepcionSiEntrenadorAModificarNoExiste() throws Exception {
        Entrenador datosNuevos = new Entrenador(null, "Cualquiera", "Cualquiera", 5);
        String jsonBody = objectMapper.writeValueAsString(datosNuevos);

        mockMvc.perform(put("/entrenadores/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("El entrenador no existe"));
    }

    @Test
    void deberiaEliminarUnEntrenador() throws Exception {
        mockMvc.perform(delete("/entrenadores/" + entrenadorGuardado.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deberiaLanzarExcepcionSiEntrenadorAEliminarNoExiste() throws Exception {
        mockMvc.perform(delete("/entrenadores/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("No existe el entrenador"));
    }

    @Test
    void deberiaFallarValidacionSiAniosExperienciaEsNegativo() throws Exception {
        Entrenador entrenadorInvalido = new Entrenador(null, "Luis", "Boxeo", -1);
        String jsonBody = objectMapper.writeValueAsString(entrenadorInvalido);

        mockMvc.perform(post("/entrenadores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.aniosExperiencia").value("La experiencia mínima debe ser de 0 años"));
    }
}