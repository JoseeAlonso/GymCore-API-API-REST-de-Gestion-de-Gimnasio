package com.mydevelopment.gimnasio.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.net.ssl.SSLSession;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

public class ClaseResponseDTO {

    private Long id;
    private String nombre;
    private String horario;
    private String nombreEntrenador;
    private List<String> nombresClientes;
}