package com.bancoxyz.notificaciones_service.consumer;

import com.bancoxyz.notificaciones_service.event.TransaccionEvento;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransaccionEventConsumer {

    @KafkaListener(topics = "transacciones-eventos", groupId = "notificaciones-group")
    public void escucharEvento(TransaccionEvento evento) {
        System.out.println("=== NOTIFICACIÓN RECIBIDA ===");
        System.out.println("Cuenta ID: " + evento.getCuentaId());
        System.out.println("Tipo de evento: " + evento.getTipoEvento());
        System.out.println("Procesando notificación para la cuenta " + evento.getCuentaId() + "...");
        System.out.println("==============================");
    }
}