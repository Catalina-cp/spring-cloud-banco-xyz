package com.bancoxyz.clientes_service.service;

import com.bancoxyz.clientes_service.dto.ClienteRequest;
import com.bancoxyz.clientes_service.exception.ClienteDuplicadoException;
import com.bancoxyz.clientes_service.exception.ClienteNoEncontradoException;
import com.bancoxyz.clientes_service.model.Cliente;
import com.bancoxyz.clientes_service.repository.ClienteRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    @Autowired
    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @CircuitBreaker(name = "clienteService", fallbackMethod = "fallbackObtenerTodos")
    @Retry(name = "clienteService")
    public List<Cliente> obtenerTodos() {
        return repository.findAll();
    }

    public List<Cliente> fallbackObtenerTodos(Exception ex) {
        return List.of(); // lista vacía como respaldo si el circuito está abierto o falla la BD
    }

    public Cliente obtenerPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ClienteNoEncontradoException(id));
    }

    public Cliente crear(ClienteRequest request) {
        if (repository.existsByRut(request.rut())) {
            throw new ClienteDuplicadoException(request.rut());
        }
        Cliente cliente = new Cliente();
        cliente.setNombre(request.nombre());
        cliente.setRut(request.rut());
        cliente.setEmail(request.email());
        cliente.setTelefono(request.telefono());
        return repository.save(cliente);
    }

    public Cliente actualizar(Long id, ClienteRequest request) {
        Cliente cliente = obtenerPorId(id);
        cliente.setNombre(request.nombre());
        cliente.setEmail(request.email());
        cliente.setTelefono(request.telefono());
        return repository.save(cliente);
    }

    public void eliminar(Long id) {
        Cliente cliente = obtenerPorId(id);
        repository.delete(cliente);
    }
}