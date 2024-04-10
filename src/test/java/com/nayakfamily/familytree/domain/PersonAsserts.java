package com.nayakfamily.familytree.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PersonAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonAllPropertiesEquals(Person expected, Person actual) {
        assertPersonAutoGeneratedPropertiesEquals(expected, actual);
        assertPersonAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonAllUpdatablePropertiesEquals(Person expected, Person actual) {
        assertPersonUpdatableFieldsEquals(expected, actual);
        assertPersonUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonAutoGeneratedPropertiesEquals(Person expected, Person actual) {
        assertThat(expected)
            .as("Verify Person auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonUpdatableFieldsEquals(Person expected, Person actual) {
        assertThat(expected)
            .as("Verify Person relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getGender()).as("check gender").isEqualTo(actual.getGender()))
            .satisfies(e -> assertThat(e.getAbout()).as("check about").isEqualTo(actual.getAbout()))
            .satisfies(e -> assertThat(e.getFathersName()).as("check fathersName").isEqualTo(actual.getFathersName()))
            .satisfies(e -> assertThat(e.getDateOfBirth()).as("check dateOfBirth").isEqualTo(actual.getDateOfBirth()))
            .satisfies(e -> assertThat(e.getPhoneNumber1()).as("check phoneNumber1").isEqualTo(actual.getPhoneNumber1()))
            .satisfies(e -> assertThat(e.getPhoneNumber2()).as("check phoneNumber2").isEqualTo(actual.getPhoneNumber2()))
            .satisfies(e -> assertThat(e.getWhatsAppNo()).as("check whatsAppNo").isEqualTo(actual.getWhatsAppNo()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getCurrentLocation()).as("check currentLocation").isEqualTo(actual.getCurrentLocation()))
            .satisfies(e -> assertThat(e.getPhoto()).as("check photo").isEqualTo(actual.getPhoto()))
            .satisfies(e -> assertThat(e.getPhotoContentType()).as("check photo contenty type").isEqualTo(actual.getPhotoContentType()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPersonUpdatableRelationshipsEquals(Person expected, Person actual) {
        assertThat(expected)
            .as("Verify Person relationships")
            .satisfies(e -> assertThat(e.getFamily()).as("check family").isEqualTo(actual.getFamily()));
    }
}
