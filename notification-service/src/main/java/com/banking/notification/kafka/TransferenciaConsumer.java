package com.banking.notification.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.banking.notification.event.TransferenciaCompletadaEvent;

@Component
public class TransferenciaConsumer {

    @KafkaListener(
            topics = "transferencia-completada",
            groupId = "notification-service"
    )
    public void consumir(TransferenciaCompletadaEvent evento) {

        System.out.println("=================================");
        System.out.println("Transferencia recibida");
        System.out.println("ID: " + evento.transferenciaId());
        System.out.println("Origen: " + evento.cuentaOrigenId());
        System.out.println("Destino: " + evento.cuentaDestinoId());
        System.out.println("Monto: $" + evento.monto());
        System.out.println("Estado: " + evento.estado());
        System.out.println("=================================");
    }
}