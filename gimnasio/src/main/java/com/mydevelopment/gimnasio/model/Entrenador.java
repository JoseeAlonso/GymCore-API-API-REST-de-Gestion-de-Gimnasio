package com.mydevelopment.gimnasio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Entrenador {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "La especialidad no puede estar vacía")
    private String especialidad;
    @Min(value = 0, message = "La experiencia mínima debe ser de 0 años") @Max(value = 110, message = "La experiencia máxima usual no puede exceder de 110 años")
    private Integer aniosExperiencia;
}
