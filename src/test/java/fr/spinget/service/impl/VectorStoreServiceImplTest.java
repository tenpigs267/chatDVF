package fr.spinget.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.spinget.service.ai.QueryCompressingService;
import fr.spinget.service.dto.DeepChatRequestBodyDTO;
import fr.spinget.service.dto.DeepChatRequestMessageDTO;
import fr.spinget.service.dto.DeepChatTextResponseDTO;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;

@ExtendWith(MockitoExtension.class)
class VectorStoreServiceImplTest {

    @Mock
    ChatClient chatClient;

    @Mock
    QueryCompressingService queryCompressingService;

    @Mock
    VectorStore vectorStore;

    @InjectMocks
    VectorStoreServiceImpl vectorStoreService;

    @Test
    void shouldChat() {
        //Given
        DeepChatRequestMessageDTO[] deepChatRequestMessageDTOS = new DeepChatRequestMessageDTO[1];
        deepChatRequestMessageDTOS[0] = DeepChatRequestMessageDTO.builder().role("User").text("0").build();
        DeepChatRequestBodyDTO deepChatRequestBodyDTO = DeepChatRequestBodyDTO.builder().messages(deepChatRequestMessageDTOS).build();

        when(queryCompressingService.compressUserPrompt(any())).thenReturn("compress");
        when(vectorStore.similaritySearch(any(SearchRequest.class))).thenReturn(List.of(new Document("document")));
        String leResultat = "le resultat";
        when(chatClient.call(any(Prompt.class))).thenReturn(new ChatResponse(List.of(new Generation(leResultat))));

        //When
        DeepChatTextResponseDTO textResponseDTO = vectorStoreService.chat(deepChatRequestBodyDTO);

        //Then
        assertThat(textResponseDTO)
            .usingRecursiveComparison()
            .ignoringFields("embeddingIDs")
            .isEqualTo(DeepChatTextResponseDTO.builder().text(leResultat).embeddingIDs(List.of()).overwrite(false).build());
        assertThat(textResponseDTO.getEmbeddingIDs().size()).isEqualTo(1);
        verify(queryCompressingService).compressUserPrompt(deepChatRequestBodyDTO);
        verify(vectorStore).similaritySearch(SearchRequest.query("compress").withTopK(5).withSimilarityThreshold(0.7));
        verify(chatClient).call(
            new Prompt(
                """
                Tu es un assistant qui va discuter sur un ton professionnel à propos de la valeur de biens immobiliers.
                Utilises les infos de la section DOCUMENTS pour fournir des réponses précises en fournissant les UUID des biens si tu les as.
                Des fonctions fournies en plus peuvent te permettre de connaitre certaines moyennes sur les prixs par commune ou département.
                Tu ne dois te baser QUE sur les DOCUMENTS ou les fonctions fournies pour répondre à l'utilisateur, si cela ne permet pas de
                répondre expliques simplement que tu n'as pas les informations à disposition.

                QUESTION:
                compress

                DOCUMENTS:
                document
                """,
                OpenAiChatOptions.builder()
                    .withFunctions(Set.of("prixPourCommune", "prixPourCodePostal", "prixPourDepartement", "prixParDepartement"))
                    .build()
            )
        );
    }
}
