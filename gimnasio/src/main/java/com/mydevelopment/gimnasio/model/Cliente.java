package com.mydevelopment.gimnasio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @Email @NotBlank(message = "Debe ser un email válido y este no puede estar vacío")
    private String email;
    @Min(value = 16, message = "La edad debe ser mayor a 16 años") @Max(value = 130, message = "Debes introducir una edad real")
    private Integer edad;
}
