package uq.sistemagestionsolicitudes.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uq.sistemagestionsolicitudes.repository.HistorialSolicitudRepository;
import uq.sistemagestionsolicitudes.service.*;
import uq.sistemagestionsolicitudes.service.impl.IAServiceFallbackImpl;
import uq.sistemagestionsolicitudes.service.impl.IAServiceOpenAIImpl;

@Slf4j
@Configuration
public class IAServiceConfig {

    @Value("${ia.provider:fallback}")
    private String provider;

    @Bean
    public IAService iaService(
            HistorialSolicitudRepository historialRepo,
            ChatClient.Builder chatClientBuilder
    ) {

        IAServiceFallbackImpl fallback =
                new IAServiceFallbackImpl(historialRepo);

        if ("openai".equalsIgnoreCase(provider)) {
            ChatClient chatClient = chatClientBuilder.build();

            return new IAServiceOpenAIImpl(
                    chatClient,
                    historialRepo,
                    fallback
            );
        }

        return fallback;
    }
}