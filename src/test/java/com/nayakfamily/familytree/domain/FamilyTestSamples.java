package com.nayakfamily.familytree.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class FamilyTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Family getFamilySample1() {
        return new Family().id(1L).srNo("srNo1").motherMaidenName("motherMaidenName1");
    }

    public static Family getFamilySample2() {
        return new Family().id(2L).srNo("srNo2").motherMaidenName("motherMaidenName2");
    }

    public static Family getFamilyRandomSampleGenerator() {
        return new Family()
            .id(longCount.incrementAndGet())
            .srNo(UUID.randomUUID().toString())
            .motherMaidenName(UUID.randomUUID().toString());
    }
}
