package com.bancoxyz.api_cuentas.controller;

import com.bancoxyz.api_cuentas.dto.CuentaResumenDTO;
import com.bancoxyz.api_cuentas.service.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    private final CuentaService service;

    @Autowired
    public CuentaController(CuentaService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CuentaResumenDTO>> obtenerTodas() {
        List<CuentaResumenDTO> cuentas = service.obtenerTodas();
        return new ResponseEntity<>(cuentas, HttpStatus.OK);
    }

    @GetMapping("/{cuentaId}")
    public ResponseEntity<CuentaResumenDTO> obtenerPorId(@PathVariable Long cuentaId) {
        CuentaResumenDTO cuenta = service.obtenerPorId(cuentaId);
        return new ResponseEntity<>(cuenta, HttpStatus.OK);
    }
}