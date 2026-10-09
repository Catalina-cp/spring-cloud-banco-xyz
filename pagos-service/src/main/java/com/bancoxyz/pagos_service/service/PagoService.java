package com.bancoxyz.pagos_service.service;

import com.bancoxyz.pagos_service.dto.PagoRequest;
import com.bancoxyz.pagos_service.event.TransaccionEvento;
import com.bancoxyz.pagos_service.event.TransaccionEventProducer;
import com.bancoxyz.pagos_service.exception.PagoInvalidoException;
import com.bancoxyz.pagos_service.exception.PagoNoEncontradoException;
import com.bancoxyz.pagos_service.model.Pago;
import com.bancoxyz.pagos_service.repository.PagoRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PagoService {

    private final PagoRepository repository;
    private final TransaccionEventProducer eventProducer;

    @Autowired
    public PagoService(PagoRepository repository, TransaccionEventProducer eventProducer) {
        this.repository = repository;
        this.eventProducer = eventProducer;
    }

    @CircuitBreaker(name = "pagoService", fallbackMethod = "fallbackObtenerTodos")
    @Retry(name = "pagoService")
    public List<Pago> obtenerTodos() {
        return repository.findAll();
    }

    public List<Pago> fallbackObtenerTodos(Exception ex) {
        return List.of(); // lista vacía como respaldo si el circuito está abierto o falla la BD
    }

    public Pago obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PagoNoEncontradoException(id));
    }

    public Pago procesar(PagoRequest request) {
        if ("TRANSFERENCIA".equals(request.tipo()) && request.cuentaDestinoId() == null) {
            throw new PagoInvalidoException("Una transferencia requiere cuenta de destino");
        }
        if ("TRANSFERENCIA".equals(request.tipo())
                && request.cuentaOrigenId().equals(request.cuentaDestinoId())) {
            throw new PagoInvalidoException("La cuenta de origen y destino no pueden ser la misma");
        }

        Pago pago = new Pago();
        pago.setCuentaOrigenId(request.cuentaOrigenId());
        pago.setCuentaDestinoId(request.cuentaDestinoId());
        pago.setTipo(request.tipo());
        pago.setMonto(request.monto());
        pago.setEstado("COMPLETADO");
        pago.setFecha(LocalDateTime.now());
        Pago guardado = repository.save(pago);

        eventProducer.publicarEvento(
                new TransaccionEvento(guardado.getCuentaOrigenId(), "PAGO_COMPLETADO"));
        return guardado;
    }
}