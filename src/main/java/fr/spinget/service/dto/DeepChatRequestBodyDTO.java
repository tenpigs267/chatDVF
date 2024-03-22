package fr.spinget.service.dto;

import java.util.Arrays;
import lombok.Builder;

@Builder
public record DeepChatRequestBodyDTO(DeepChatRequestMessageDTO[] messages, Boolean stream) {
    public String getPrompt() {
        return Arrays.stream(this.messages()).findFirst().orElseThrow().text();
    }
}
