package fr.spinget.service.dto;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class DeepChatTextResponseDTO {

    private String text;
    private String html;
    private Boolean overwrite = false;
    private List<String> embeddingIDs;
}
