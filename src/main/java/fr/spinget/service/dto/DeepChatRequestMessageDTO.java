package fr.spinget.service.dto;

import lombok.Builder;

@Builder
public record DeepChatRequestMessageDTO(String role, String text) {}
