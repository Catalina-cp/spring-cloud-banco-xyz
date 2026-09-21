package com.bancoxyz.api_cuentas.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cuentas")
public class Cuenta {

    @Id
    private Long cuentaId;

    private String nombre;

    private Double saldoFinal;

    public Cuenta() {}

    public Long getCuentaId() { return cuentaId; }
    public void setCuentaId(Long cuentaId) { this.cuentaId = cuentaId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Double getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(Double saldoFinal) { this.saldoFinal = saldoFinal; }
}