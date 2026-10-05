package com.bancoxyz.api_cuentas.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class TransaccionEventProducer {

    private static final String TOPIC = "transacciones-eventos";

    private final KafkaTemplate<String, TransaccionEvento> kafkaTemplate;

    @Autowired
    public TransaccionEventProducer(KafkaTemplate<String, TransaccionEvento> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicarEvento(TransaccionEvento evento) {
        kafkaTemplate.send(TOPIC, evento.getCuentaId().toString(), evento);
    }
}