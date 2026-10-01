package com.mydevelopment.gimnasio.repository;

import com.mydevelopment.gimnasio.model.Clase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClaseRepository extends JpaRepository<Clase, Long> {
}
