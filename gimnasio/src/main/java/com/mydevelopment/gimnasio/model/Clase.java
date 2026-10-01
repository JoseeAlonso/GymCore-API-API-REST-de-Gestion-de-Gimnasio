package com.mydevelopment.gimnasio.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
public class Clase {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacío")
    private String nombre;
    @NotBlank(message = "El horario no puede estar vacío")
    private String horario;

    @NotNull(message = "Debe asignarse un entrenador")
    @ManyToOne
    @JoinColumn(name = "entrenador_id")
    private Entrenador entrenador;

    @NotEmpty(message = "La clase debe tener al menos un cliente apuntado")
    @ManyToMany
    @JoinTable(
            name = "clase_cliente",
            joinColumns = @JoinColumn(name = "clase_id"),
            inverseJoinColumns = @JoinColumn(name = "cliente_id")
    )
    private List<Cliente> clientes;
}
