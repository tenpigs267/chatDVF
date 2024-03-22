package fr.spinget.service.ai;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import fr.spinget.service.dto.DeepChatRequestBodyDTO;
import fr.spinget.service.dto.DeepChatRequestMessageDTO;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.prompt.Prompt;

@ExtendWith(MockitoExtension.class)
class QueryCompressingServiceTest {

    @Mock
    ChatClient chatClient;

    @InjectMocks
    QueryCompressingService queryCompressingService;

    @Test
    void shouldCompress() {
        //Given
        String leResultat = "le resultat";
        when(chatClient.call(any(Prompt.class))).thenReturn(new ChatResponse(List.of(new Generation(leResultat))));
        DeepChatRequestMessageDTO[] deepChatRequestMessageDTOS = new DeepChatRequestMessageDTO[8];
        deepChatRequestMessageDTOS[0] = DeepChatRequestMessageDTO.builder().role("IA").text("0").build();
        deepChatRequestMessageDTOS[1] = DeepChatRequestMessageDTO.builder().role("User").text("1").build();
        deepChatRequestMessageDTOS[2] = DeepChatRequestMessageDTO.builder().role("IA").text("2").build();
        deepChatRequestMessageDTOS[3] = DeepChatRequestMessageDTO.builder().role("User").text("3").build();
        deepChatRequestMessageDTOS[4] = DeepChatRequestMessageDTO.builder().role("IA").text("4").build();
        deepChatRequestMessageDTOS[5] = DeepChatRequestMessageDTO.builder().role("User").text("5").build();
        deepChatRequestMessageDTOS[6] = DeepChatRequestMessageDTO.builder().role("IA").text("6").build();
        deepChatRequestMessageDTOS[7] = DeepChatRequestMessageDTO.builder().role("User").text("7").build();

        //When
        String compressedUserPrompt = queryCompressingService.compressUserPrompt(
            DeepChatRequestBodyDTO.builder().messages(deepChatRequestMessageDTOS).build()
        );

        //Then
        assertThat(compressedUserPrompt).isEqualTo(leResultat);
        verify(chatClient).call(
            new Prompt(
                "Role : User - Message : 3, " +
                "Role : IA - Message : 4, Role : User - Message : 5, " +
                "Role : IA - Message : 6, Role : User - Message : 7, "
            )
        );
    }

    @Test
    void shouldCompressWithLessThan5Historic() {
        //Given
        String leResultat = "le resultat";
        when(chatClient.call(any(Prompt.class))).thenReturn(new ChatResponse(List.of(new Generation(leResultat))));
        DeepChatRequestMessageDTO[] deepChatRequestMessageDTOS = new DeepChatRequestMessageDTO[2];
        deepChatRequestMessageDTOS[0] = DeepChatRequestMessageDTO.builder().role("IA").text("0").build();
        deepChatRequestMessageDTOS[1] = DeepChatRequestMessageDTO.builder().role("User").text("1").build();

        //When
        String compressedUserPrompt = queryCompressingService.compressUserPrompt(
            DeepChatRequestBodyDTO.builder().messages(deepChatRequestMessageDTOS).build()
        );

        //Then
        assertThat(compressedUserPrompt).isEqualTo(leResultat);
        verify(chatClient).call(new Prompt("Role : IA - Message : 0, Role : User - Message : 1"));
    }

    @Test
    void shouldReturnWhenOneMessage() {
        //Given
        DeepChatRequestMessageDTO[] deepChatRequestMessageDTOS = new DeepChatRequestMessageDTO[1];
        deepChatRequestMessageDTOS[0] = DeepChatRequestMessageDTO.builder().role("User").text("0").build();

        //When
        String compressedUserPrompt = queryCompressingService.compressUserPrompt(
            DeepChatRequestBodyDTO.builder().messages(deepChatRequestMessageDTOS).build()
        );

        //Then
        assertThat(compressedUserPrompt).isEqualTo("0");
    }

    @Test
    void shouldThrowIfNoConversationHistory() {
        //Given
        DeepChatRequestMessageDTO[] deepChatRequestMessageDTOS = new DeepChatRequestMessageDTO[0];

        //When
        assertThrows(
            NoSuchElementException.class,
            () -> queryCompressingService.compressUserPrompt(DeepChatRequestBodyDTO.builder().messages(deepChatRequestMessageDTOS).build())
        );
    }
}
