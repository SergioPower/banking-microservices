package com.banking.transfer.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.banking.transfer.event.TransferenciaCompletadaEvent;

@Component 
public class TransferenciaProducer {
    private final KafkaTemplate<String, TransferenciaCompletadaEvent> kafkaTemplate;

    public TransferenciaProducer(
            KafkaTemplate<String, TransferenciaCompletadaEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publicar(TransferenciaCompletadaEvent evento) {
        kafkaTemplate.send("transferencia-completada", evento);
    }
    
}
