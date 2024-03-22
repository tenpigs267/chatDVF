package fr.spinget.service.ai;

import fr.spinget.service.dto.DeepChatRequestBodyDTO;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.stereotype.Service;

@Service
public class QueryCompressingService {

    private String promptCompressionTemplate =
        """
        Ci dessous une conversation entre l'utilisateur et l'IA. Analyse la dernière demande de l'utilisateur.
        Identifie tous les détails, les termes et le contexte puis reformule le dernier message de l'utilisateur en un seul message contenant toutes les informations pertinentes pour lui répondre.

        Conversation:
        {{chatMemory}}

        message de l'utilisateur: {{query}}
        """;

    private final ChatClient chatClient;

    public QueryCompressingService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String compressUserPrompt(DeepChatRequestBodyDTO deepChatRequestBodyDTO) {
        if (deepChatRequestBodyDTO.messages().length == 1) {
            return deepChatRequestBodyDTO.messages()[0].text();
        }

        PromptTemplate promptTemplateCompression = new PromptTemplate(promptCompressionTemplate);
        Map<String, Object> promptCompressionParameters = new HashMap<>();
        promptCompressionParameters.put("query", deepChatRequestBodyDTO.getPrompt());
        promptCompressionParameters.put(
            "chatMemory",
            Stream.of(deepChatRequestBodyDTO.messages())
                .skip(Math.max(0, deepChatRequestBodyDTO.messages().length - 5)) // Saute tous les éléments sauf les 5 derniers
                .map(message -> "Role : " + message.role() + " - Message : " + message.text())
                .collect(Collectors.joining(", "))
        );
        Prompt promptCompression = promptTemplateCompression.create(promptCompressionParameters);

        return chatClient.call(promptCompression).getResult().getOutput().getContent();
    }
}
