package com.banking.notification.kafka;

import java.util.function.Consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.banking.notification.event.TransferenciaCompletadaEvent;

@Component
public class TransferenciaConsumer {

    private static final Logger log = LoggerFactory.getLogger(TransferenciaConsumer.class);

    @KafkaListener(
        topics = "transferencia-completada",
        groupId = "notification-service"
    )
    public void consumir(TransferenciaCompletadaEvent evento) {
        log.info("Transferencia recibida: id={}, origen={}, destino={}, monto={}, estado={}",
            evento.transferenciaId(),
            evento.cuentaOrigenId(),
            evento.cuentaDestinoId(),
            evento.monto(),
            evento.estado());
    }
}