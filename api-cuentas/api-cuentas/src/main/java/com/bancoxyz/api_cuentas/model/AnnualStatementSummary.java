package com.bancoxyz.api_cuentas.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "annual_statement_summary")
public class AnnualStatementSummary {

    @Id
    @Column(name = "cuenta_id")
    private Long cuentaId;

    @Column(name = "total_movimientos")
    private Integer totalMovimientos;

    @Column(name = "total_ingresos")
    private BigDecimal totalIngresos;

    @Column(name = "total_retiros")
    private BigDecimal totalRetiros;

    @Column(name = "saldo_neto_anual")
    private BigDecimal saldoNetoAnual;

    public AnnualStatementSummary() {}

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