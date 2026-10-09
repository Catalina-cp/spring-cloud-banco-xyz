package com.bancoxyz.pagos_service.controller;

import com.bancoxyz.pagos_service.dto.PagoRequest;
import com.bancoxyz.pagos_service.model.Pago;
import com.bancoxyz.pagos_service.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagos")
public class PagoController {

    private final PagoService service;

    @Autowired
    public PagoController(PagoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Pago>> obtenerTodos() {
        return new ResponseEntity<>(service.obtenerTodos(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pago> obtenerPorId(@PathVariable Long id) {
        return new ResponseEntity<>(service.obtenerPorId(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pago> procesar(@Valid @RequestBody PagoRequest request) {
        return new ResponseEntity<>(service.procesar(request), HttpStatus.CREATED);
    }
}