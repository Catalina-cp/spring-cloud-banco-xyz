package com.bancoxyz.clientes_service.repository;

import com.bancoxyz.clientes_service.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByRut(String rut);
}