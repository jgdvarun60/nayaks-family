package com.nayakfamily.familytree.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PersonTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Person getPersonSample1() {
        return new Person()
            .id(1L)
            .name("name1")
            .about("about1")
            .fathersName("fathersName1")
            .phoneNumber1("phoneNumber11")
            .phoneNumber2("phoneNumber21")
            .whatsAppNo("whatsAppNo1")
            .email("email1")
            .currentLocation("currentLocation1");
    }

    public static Person getPersonSample2() {
        return new Person()
            .id(2L)
            .name("name2")
            .about("about2")
            .fathersName("fathersName2")
            .phoneNumber1("phoneNumber12")
            .phoneNumber2("phoneNumber22")
            .whatsAppNo("whatsAppNo2")
            .email("email2")
            .currentLocation("currentLocation2");
    }

    public static Person getPersonRandomSampleGenerator() {
        return new Person()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .about(UUID.randomUUID().toString())
            .fathersName(UUID.randomUUID().toString())
            .phoneNumber1(UUID.randomUUID().toString())
            .phoneNumber2(UUID.randomUUID().toString())
            .whatsAppNo(UUID.randomUUID().toString())
            .email(UUID.randomUUID().toString())
            .currentLocation(UUID.randomUUID().toString());
    }
}
