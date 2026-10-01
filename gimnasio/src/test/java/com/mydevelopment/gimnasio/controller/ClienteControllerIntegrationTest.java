package com.mydevelopment.gimnasio.controller;

import com.mydevelopment.gimnasio.model.Cliente;
import com.mydevelopment.gimnasio.repository.ClienteRepository;
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
public class ClienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ClienteRepository clienteRepository;

    private Cliente clienteGuardado;

    @BeforeEach
    void setUp() {
        clienteGuardado = clienteRepository.save(new Cliente(null, "Jose", "jose@gmail.com", 25));
    }

    @Test
    void deberiaCrearUnCliente() throws Exception {
        Cliente clienteNuevo = new Cliente(null, "Ana", "ana@gmail.com", 28);
        String jsonBody = objectMapper.writeValueAsString(clienteNuevo);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Ana"))
                .andExpect(jsonPath("$.email").value("ana@gmail.com"));
    }

    @Test
    void deberiaObtenerLosClientes() throws Exception {
        mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[?(@.id == " + clienteGuardado.getId() + ")].nombre")
                        .value("Jose"));
    }

    // ALTERNATIVA POR SINTAXIS
    /* Esta manera es igual pero utiliza sintaxis JAVA mientras que la anterior utiliza sintaxis JsonPath
    @Test
    void deberiaObtenerLosClientes() throws Exception {
        String responseBody = mockMvc.perform(get("/clientes"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<Cliente> clientes = objectMapper.readValue(responseBody, new TypeReference<List<Cliente>>() {});

        boolean existeJose = clientes.stream()
                .anyMatch(c -> c.getId().equals(clienteGuardado.getId()) && c.getNombre().equals("Jose"));

        assertTrue(existeJose);
}
*/

    @Test
    void deberiaModificarUnCliente() throws Exception {
        Cliente datosNuevos = new Cliente(null, "Jose Actualizado", "jose.nuevo@gmail.com", 31);
        String jsonBody = objectMapper.writeValueAsString(datosNuevos);

        mockMvc.perform(put("/clientes/" + clienteGuardado.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Jose Actualizado"))
                .andExpect(jsonPath("$.email").value("jose.nuevo@gmail.com"));
    }

    @Test
    void deberiaLanzarExcepcionSiClienteAModificarNoExiste() throws Exception {
        Cliente datosNuevos = new Cliente(null, "Cualquiera", "cualquiera@gmail.com", 20);
        String jsonBody = objectMapper.writeValueAsString(datosNuevos);

        mockMvc.perform(put("/clientes/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("El cliente no existe"));
    }

    @Test
    void deberiaEliminarUnCliente() throws Exception {
        mockMvc.perform(delete("/clientes/" + clienteGuardado.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deberiaLanzarExcepcionSiClienteAEliminarNoExiste() throws Exception {
        mockMvc.perform(delete("/clientes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("El cliente no existe"));
    }

    @Test
    void deberiaFallarValidacionSiNombreEstaVacio() throws Exception {
        Cliente clienteInvalido = new Cliente(null, "", "test@gmail.com", 25);
        String jsonBody = objectMapper.writeValueAsString(clienteInvalido);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombre").value("El nombre no puede estar vacío"));
    }

    @Test
    void deberiaFallarValidacionSiEmailNoEsValido() throws Exception {
        Cliente clienteInvalido = new Cliente(null, "Carlos", "correo-invalido", 25);
        String jsonBody = objectMapper.writeValueAsString(clienteInvalido);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").exists());
    }
}