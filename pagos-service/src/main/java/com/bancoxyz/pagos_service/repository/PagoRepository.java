package com.bancoxyz.pagos_service.repository;

import com.bancoxyz.pagos_service.model.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
}