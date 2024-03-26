package fr.spinget.service.dto;

import java.util.Arrays;
import lombok.Builder;

@Builder
public record DeepChatRequestBodyDTO(DeepChatRequestMessageDTO[] messages, Boolean stream) {
    public String getPrompt() {
        return this.messages()[this.messages.length - 1].text();
    }
}
