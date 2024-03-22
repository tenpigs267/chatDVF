package fr.spinget.service.impl;

import fr.spinget.domain.VectorStore;
import fr.spinget.repository.VectorStoreRepository;
import fr.spinget.service.VectorStoreService;
import fr.spinget.service.ai.QueryCompressingService;
import fr.spinget.service.dto.DeepChatRequestBodyDTO;
import fr.spinget.service.dto.DeepChatTextResponseDTO;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.spinget.domain.VectorStore}.
 */
@Service
@Transactional
public class VectorStoreServiceImpl implements VectorStoreService {

    private final Logger log = LoggerFactory.getLogger(VectorStoreServiceImpl.class);

    private final VectorStoreRepository vectorStoreRepository;
    private final org.springframework.ai.vectorstore.VectorStore vectorStore;
    private final ChatClient chatClient;
    private final QueryCompressingService queryCompressingService;

    private String ragPromptTemplate =
        """
        Tu es un assistant qui va discuter sur un ton professionnel à propos de la valeur de biens immobiliers.
        Utilises les infos de la section DOCUMENTS pour fournir des réponses précises en fournissant les UUID des biens si tu les as.
        Des fonctions fournies en plus peuvent te permettre de connaitre certaines moyennes sur les prixs par commune ou département.
        Tu ne dois te baser QUE sur les DOCUMENTS ou les fonctions fournies pour répondre à l'utilisateur, si cela ne permet pas de
        répondre expliques simplement que tu n'as pas les informations à disposition.

        QUESTION:
        {input}

        DOCUMENTS:
        {documents}
        """;

    public VectorStoreServiceImpl(
        VectorStoreRepository vectorStoreRepository,
        org.springframework.ai.vectorstore.VectorStore vectorStore,
        ChatClient chatClient,
        QueryCompressingService queryCompressingService
    ) {
        this.vectorStoreRepository = vectorStoreRepository;
        this.vectorStore = vectorStore;
        this.chatClient = chatClient;
        this.queryCompressingService = queryCompressingService;
    }

    @Override
    public VectorStore save(VectorStore vectorStore) {
        log.debug("Request to save VectorStore : {}", vectorStore);
        return vectorStoreRepository.save(vectorStore);
    }

    @Override
    public VectorStore update(VectorStore vectorStore) {
        log.debug("Request to update VectorStore : {}", vectorStore);
        return vectorStoreRepository.save(vectorStore);
    }

    @Override
    public Optional<VectorStore> partialUpdate(VectorStore vectorStore) {
        log.debug("Request to partially update VectorStore : {}", vectorStore);

        return vectorStoreRepository
            .findById(vectorStore.getId())
            .map(existingVectorStore -> {
                if (vectorStore.getContent() != null) {
                    existingVectorStore.setContent(vectorStore.getContent());
                }
                if (vectorStore.getMetadata() != null) {
                    existingVectorStore.setMetadata(vectorStore.getMetadata());
                }
                if (vectorStore.getCommune() != null) {
                    existingVectorStore.setCommune(vectorStore.getCommune());
                }
                if (vectorStore.getCodePostal() != null) {
                    existingVectorStore.setCodePostal(vectorStore.getCodePostal());
                }
                if (vectorStore.getDepartement() != null) {
                    existingVectorStore.setDepartement(vectorStore.getDepartement());
                }
                if (vectorStore.getCodeDepartement() != null) {
                    existingVectorStore.setCodeDepartement(vectorStore.getCodeDepartement());
                }
                if (vectorStore.getTypeLocal() != null) {
                    existingVectorStore.setTypeLocal(vectorStore.getTypeLocal());
                }
                if (vectorStore.getSuperficieCarrez() != null) {
                    existingVectorStore.setSuperficieCarrez(vectorStore.getSuperficieCarrez());
                }
                if (vectorStore.getSuperficieTerrain() != null) {
                    existingVectorStore.setSuperficieTerrain(vectorStore.getSuperficieTerrain());
                }
                if (vectorStore.getNbPieces() != null) {
                    existingVectorStore.setNbPieces(vectorStore.getNbPieces());
                }
                if (vectorStore.getDateVente() != null) {
                    existingVectorStore.setDateVente(vectorStore.getDateVente());
                }
                if (vectorStore.getValeur() != null) {
                    existingVectorStore.setValeur(vectorStore.getValeur());
                }

                return existingVectorStore;
            })
            .map(vectorStoreRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VectorStore> findAll() {
        log.debug("Request to get all VectorStores");
        return vectorStoreRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<VectorStore> findOne(UUID id) {
        log.debug("Request to get VectorStore : {}", id);
        return vectorStoreRepository.findById(id);
    }

    @Override
    public void delete(UUID id) {
        log.debug("Request to delete VectorStore : {}", id);
        vectorStoreRepository.deleteById(id);
    }

    @Override
    public DeepChatTextResponseDTO chat(DeepChatRequestBodyDTO deepChatRequestBodyDTO) {
        String compressedUserPrompt = queryCompressingService.compressUserPrompt(deepChatRequestBodyDTO);

        List<Document> similarDocuments =
            this.vectorStore.similaritySearch(SearchRequest.query(compressedUserPrompt).withTopK(5).withSimilarityThreshold(0.7));
        List<String> contentList = similarDocuments.stream().map(Document::getContent).toList();
        List<String> uuids = similarDocuments.stream().map(Document::getId).toList();

        PromptTemplate promptTemplateRAG = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParametersRAG = new HashMap<>();
        promptParametersRAG.put("input", compressedUserPrompt);
        promptParametersRAG.put("documents", String.join("\n", contentList));
        Prompt promptRAG = promptTemplateRAG.create(promptParametersRAG);

        Prompt promptRAGAndTools = new Prompt(
            promptRAG.getContents(),
            OpenAiChatOptions.builder()
                .withFunctions(Set.of("prixPourCommune", "prixPourCodePostal", "prixPourDepartement", "prixParDepartement"))
                .build()
        );

        ChatResponse chatResponse = chatClient.call(promptRAGAndTools);

        return DeepChatTextResponseDTO.builder()
            .text(chatResponse.getResult().getOutput().getContent())
            .overwrite(false)
            .embeddingIDs(uuids)
            .build();
    }
}
