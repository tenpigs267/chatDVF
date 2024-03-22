package fr.spinget.config;

import fr.spinget.repository.VectorStoreRepository;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.OpenAiEmbeddingClient;
import org.springframework.ai.openai.OpenAiEmbeddingOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.PgVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.retry.support.RetryTemplate;

@Configuration
public class AIConfig {

    @Bean
    public OpenAiApi openAiApi(@Value("${openai.api.key}") String apiKey) {
        return new OpenAiApi(apiKey);
    }

    @Bean
    public ChatClient chatClient(OpenAiApi openAiApi) {
        return new OpenAiChatClient(
            openAiApi,
            OpenAiChatOptions.builder().withModel("gpt-35-turbo").withTemperature(0.4f).withMaxTokens(200).build()
        );
    }

    @Bean
    public EmbeddingClient embeddingClient(OpenAiApi openAiApi) {
        return new OpenAiEmbeddingClient(
            openAiApi,
            MetadataMode.EMBED,
            OpenAiEmbeddingOptions.builder().withModel("text-embedding-3-small").build(),
            RetryTemplate.defaultInstance()
        );
    }

    @Bean
    org.springframework.ai.vectorstore.VectorStore pgVectorStore(EmbeddingClient embeddingClient, JdbcTemplate jdbcTemplate) {
        return new PgVectorStore(jdbcTemplate, embeddingClient);
    }

    @Autowired
    private VectorStoreRepository vectorStoreRepository;

    @Bean
    @Description("Pour connaitre le prix au m² dans une commune")
    public Function<String, Float> prixPourCommune() {
        return commune -> vectorStoreRepository.prixMoyenAuM2PourCommune(commune);
    }

    @Bean
    @Description("Pour connaitre le prix au m² dans une commune désignée par son code postal numérique")
    public Function<Integer, Float> prixPourCodePostal() {
        return commune -> vectorStoreRepository.prixMoyenAuM2PourCodePostal(commune);
    }

    @Bean
    @Description("Pour connaitre le prix au m² dans un département")
    public Function<String, Float> prixPourDepartement() {
        return commune -> vectorStoreRepository.prixMoyenAuM2PourDepartement(commune);
    }

    @Bean
    @Description("Pour connaitre le prix au m² pour tous les départements")
    public Supplier<Float> prixParDepartement() {
        return () -> vectorStoreRepository.prixMoyenAuM2ParDepartement();
    }
}
