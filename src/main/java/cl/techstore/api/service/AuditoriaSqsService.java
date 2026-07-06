package cl.techstore.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import java.time.Instant;

@Service
public class AuditoriaSqsService {

    @Autowired
    private SqsClient sqsClient;

    // URL de tu cola en AWS
    private final String queueUrl = "https://sqs.us-east-1.amazonaws.com/781667028802/techstore-audit-queue";

    // Agregamos el parámetro 'usuarioEmail' para cumplir el requisito de la rúbrica
    public void enviarEventoAuditoria(String accion, Long productoId, String nombreProducto, String usuarioEmail) {
        try {
            // Generamos automáticamente la fecha actual en formato ISO 8601 (ej: 2026-07-05T03:40:00Z)
            String fechaIso = Instant.now().toString();

            // Armamos el JSON final con los 5 campos obligatorios exigidos por el documento
            String jsonMensaje = String.format(
                "{\"accion\": \"%s\", \"productoId\": %d, \"nombre\": \"%s\", \"usuario\": \"%s\", \"fecha\": \"%s\"}",
                accion, productoId, nombreProducto, usuarioEmail, fechaIso
            );

            // Armamos la petición para AWS
            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(jsonMensaje)
                    .build();

            // Enviamos a la cola SQS
            sqsClient.sendMessage(request);
            System.out.println("======> Evento de auditoría enviado a SQS con éxito: " + jsonMensaje);

        } catch (Exception e) {
            System.err.println("======> Error al enviar auditoría a SQS: " + e.getMessage());
        }
    }
}