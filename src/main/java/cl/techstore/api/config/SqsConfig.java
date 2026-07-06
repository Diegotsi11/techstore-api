package cl.techstore.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfig {

    @Bean
    public SqsClient sqsClient() {
        // Al no ponerle rutas manuales, el SDK de AWS busca sólito en la ruta por defecto del sistema
        return SqsClient.builder()
                .region(Region.US_EAST_1)
                .build();
    }
}