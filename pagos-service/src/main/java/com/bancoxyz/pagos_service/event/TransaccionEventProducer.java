package com.bancoxyz.pagos_service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransaccionEventProducer {

    private static final Logger log = LoggerFactory.getLogger(TransaccionEventProducer.class);
    private static final String TOPIC = "transacciones-eventos";

    private final KafkaTemplate<String, TransaccionEvento> kafkaTemplate;

    @Autowired
    public TransaccionEventProducer(KafkaTemplate<String, TransaccionEvento> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarEvento(TransaccionEvento evento) {
        try {
            kafkaTemplate.send(TOPIC, evento.getCuentaId().toString(), evento);
        } catch (Exception ex) {
            log.warn("No se pudo publicar el evento en Kafka: {}", ex.getMessage());
        }
    }
}