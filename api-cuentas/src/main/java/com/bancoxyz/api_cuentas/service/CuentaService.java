package com.bancoxyz.api_cuentas.service;

import com.bancoxyz.api_cuentas.dto.CuentaResumenDTO;
import com.bancoxyz.api_cuentas.exception.CuentaNoEncontradaException;
import com.bancoxyz.api_cuentas.model.AnnualStatementSummary;
import com.bancoxyz.api_cuentas.repository.AnnualStatementSummaryRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CuentaService {

    private final AnnualStatementSummaryRepository repository;

    @Autowired
    public CuentaService(AnnualStatementSummaryRepository repository) {
        this.repository = repository;
    }

    @CircuitBreaker(name = "cuentaService", fallbackMethod = "fallbackObtenerTodas")
    @Retry(name = "cuentaService")
    public List<CuentaResumenDTO> obtenerTodas() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CuentaResumenDTO> fallbackObtenerTodas(Exception ex) {
        return List.of(); // lista vacía como respaldo si el circuito está abierto o falla la BD
    }

    public CuentaResumenDTO obtenerPorId(Long cuentaId) {
        AnnualStatementSummary entity = repository.findById(cuentaId)
                .orElseThrow(() -> new CuentaNoEncontradaException(cuentaId));
        return toDTO(entity);
    }

    private CuentaResumenDTO toDTO(AnnualStatementSummary entity) {
        return new CuentaResumenDTO(
                entity.getCuentaId(),
                entity.getTotalMovimientos(),
                entity.getTotalIngresos(),
                entity.getTotalRetiros(),
                entity.getSaldoNetoAnual()
        );
    }
}