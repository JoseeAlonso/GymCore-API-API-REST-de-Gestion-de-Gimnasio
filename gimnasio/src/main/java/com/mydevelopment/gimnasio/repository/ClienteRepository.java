package com.mydevelopment.gimnasio.repository;

import com.mydevelopment.gimnasio.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
