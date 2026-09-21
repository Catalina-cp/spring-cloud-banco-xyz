package com.bancoxyz.api_cuentas.dto;

import java.math.BigDecimal;

public class CuentaResumenDTO {

    private Long cuentaId;
    private Integer totalMovimientos;
    private BigDecimal totalIngresos;
    private BigDecimal totalRetiros;
    private BigDecimal saldoNetoAnual;

    public CuentaResumenDTO() {}

    public CuentaResumenDTO(Long cuentaId, Integer totalMovimientos, BigDecimal totalIngresos,
                             BigDecimal totalRetiros, BigDecimal saldoNetoAnual) {
        this.cuentaId = cuentaId;
        this.totalMovimientos = totalMovimientos;
        this.totalIngresos = totalIngresos;
        this.totalRetiros = totalRetiros;
        this.saldoNetoAnual = saldoNetoAnual;
    }

    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public Integer getTotalMovimientos() { return totalMovimientos; }
    public void setTotalMovimientos(Integer totalMovimientos) { this.totalMovimientos = totalMovimientos; }

    public BigDecimal getTotalIngresos() { return totalIngresos; }
    public void setTotalIngresos(BigDecimal totalIngresos) { this.totalIngresos = totalIngresos; }

    public BigDecimal getTotalRetiros() { return totalRetiros; }
    public void setTotalRetiros(BigDecimal totalRetiros) { this.totalRetiros = totalRetiros; }

    public BigDecimal getSaldoNetoAnual() { return saldoNetoAnual; }
    public void setSaldoNetoAnual(BigDecimal saldoNetoAnual) { this.saldoNetoAnual = saldoNetoAnual; }
}