package fr.spinget.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class VectorStoreTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static VectorStore getVectorStoreSample1() {
        return new VectorStore()
            .id(UUID.fromString("23d8dc04-a48b-45d9-a01d-4b728f0ad4aa"))
            .content("content1")
            .commune("commune1")
            .codePostal(1)
            .codeDepartement(1)
            .typeLocal("typeLocal1")
            .superficieCarrez(1.1f)
            .superficieTerrain(1.2f)
            .nbPieces(1);
    }

    public static VectorStore getVectorStoreSample2() {
        return new VectorStore()
            .id(UUID.fromString("ad79f240-3727-46c3-b89f-2cf6ebd74367"))
            .content("content2")
            .commune("commune2")
            .codePostal(2)
            .codeDepartement(2)
            .typeLocal("typeLocal2")
            .superficieCarrez(2.1f)
            .superficieTerrain(2.2f)
            .nbPieces(2);
    }

    public static VectorStore getVectorStoreRandomSampleGenerator() {
        return new VectorStore()
            .id(UUID.randomUUID())
            .content(UUID.randomUUID().toString())
            .commune(UUID.randomUUID().toString())
            .codePostal(intCount.incrementAndGet())
            .codeDepartement(intCount.incrementAndGet())
            .typeLocal(UUID.randomUUID().toString())
            .superficieCarrez((float) intCount.incrementAndGet())
            .superficieTerrain((float) intCount.incrementAndGet())
            .nbPieces(intCount.incrementAndGet());
    }
}
