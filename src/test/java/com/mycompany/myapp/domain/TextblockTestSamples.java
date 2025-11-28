package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TextblockTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Textblock getTextblockSample1() {
        return new Textblock().id(1L).describer("describer1").nr("nr1").text("text1");
    }

    public static Textblock getTextblockSample2() {
        return new Textblock().id(2L).describer("describer2").nr("nr2").text("text2");
    }

    public static Textblock getTextblockRandomSampleGenerator() {
        return new Textblock()
            .id(longCount.incrementAndGet())
            .describer(UUID.randomUUID().toString())
            .nr(UUID.randomUUID().toString())
            .text(UUID.randomUUID().toString());
    }
}
