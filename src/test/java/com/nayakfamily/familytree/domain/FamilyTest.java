package com.nayakfamily.familytree.domain;

import static com.nayakfamily.familytree.domain.FamilyTestSamples.*;
import static com.nayakfamily.familytree.domain.PersonTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.nayakfamily.familytree.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class FamilyTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Family.class);
        Family family1 = getFamilySample1();
        Family family2 = new Family();
        assertThat(family1).isNotEqualTo(family2);

        family2.setId(family1.getId());
        assertThat(family1).isEqualTo(family2);

        family2 = getFamilySample2();
        assertThat(family1).isNotEqualTo(family2);
    }

    @Test
    void childrensTest() throws Exception {
        Family family = getFamilyRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        family.addChildrens(personBack);
        assertThat(family.getChildrens()).containsOnly(personBack);
        assertThat(personBack.getFamily()).isEqualTo(family);

        family.removeChildrens(personBack);
        assertThat(family.getChildrens()).doesNotContain(personBack);
        assertThat(personBack.getFamily()).isNull();

        family.childrens(new HashSet<>(Set.of(personBack)));
        assertThat(family.getChildrens()).containsOnly(personBack);
        assertThat(personBack.getFamily()).isEqualTo(family);

        family.setChildrens(new HashSet<>());
        assertThat(family.getChildrens()).doesNotContain(personBack);
        assertThat(personBack.getFamily()).isNull();
    }

    @Test
    void fatherTest() throws Exception {
        Family family = getFamilyRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        family.setFather(personBack);
        assertThat(family.getFather()).isEqualTo(personBack);

        family.father(null);
        assertThat(family.getFather()).isNull();
    }

    @Test
    void motherTest() throws Exception {
        Family family = getFamilyRandomSampleGenerator();
        Person personBack = getPersonRandomSampleGenerator();

        family.setMother(personBack);
        assertThat(family.getMother()).isEqualTo(personBack);

        family.mother(null);
        assertThat(family.getMother()).isNull();
    }
}
