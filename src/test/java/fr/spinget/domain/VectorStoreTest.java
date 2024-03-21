package fr.spinget.domain;

import static fr.spinget.domain.VectorStoreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import fr.spinget.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class VectorStoreTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(VectorStore.class);
        VectorStore vectorStore1 = getVectorStoreSample1();
        VectorStore vectorStore2 = new VectorStore();
        assertThat(vectorStore1).isNotEqualTo(vectorStore2);

        vectorStore2.setId(vectorStore1.getId());
        assertThat(vectorStore1).isEqualTo(vectorStore2);

        vectorStore2 = getVectorStoreSample2();
        assertThat(vectorStore1).isNotEqualTo(vectorStore2);
    }
}
