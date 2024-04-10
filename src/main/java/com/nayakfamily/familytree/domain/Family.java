package com.nayakfamily.familytree.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Family.
 */
@Entity
@Table(name = "family")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Family implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "sr_no")
    private String srNo;

    @Column(name = "mother_maiden_name")
    private String motherMaidenName;

    @Column(name = "marriage_date")
    private Instant marriageDate;

    @Lob
    @Column(name = "family_photo")
    private byte[] familyPhoto;

    @Column(name = "family_photo_content_type")
    private String familyPhotoContentType;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "family")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "family" }, allowSetters = true)
    private Set<Person> childrens = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "family" }, allowSetters = true)
    private Person father;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "family" }, allowSetters = true)
    private Person mother;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Family id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSrNo() {
        return this.srNo;
    }

    public Family srNo(String srNo) {
        this.setSrNo(srNo);
        return this;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getMotherMaidenName() {
        return this.motherMaidenName;
    }

    public Family motherMaidenName(String motherMaidenName) {
        this.setMotherMaidenName(motherMaidenName);
        return this;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public Instant getMarriageDate() {
        return this.marriageDate;
    }

    public Family marriageDate(Instant marriageDate) {
        this.setMarriageDate(marriageDate);
        return this;
    }

    public void setMarriageDate(Instant marriageDate) {
        this.marriageDate = marriageDate;
    }

    public byte[] getFamilyPhoto() {
        return this.familyPhoto;
    }

    public Family familyPhoto(byte[] familyPhoto) {
        this.setFamilyPhoto(familyPhoto);
        return this;
    }

    public void setFamilyPhoto(byte[] familyPhoto) {
        this.familyPhoto = familyPhoto;
    }

    public String getFamilyPhotoContentType() {
        return this.familyPhotoContentType;
    }

    public Family familyPhotoContentType(String familyPhotoContentType) {
        this.familyPhotoContentType = familyPhotoContentType;
        return this;
    }

    public void setFamilyPhotoContentType(String familyPhotoContentType) {
        this.familyPhotoContentType = familyPhotoContentType;
    }

    public Set<Person> getChildrens() {
        return this.childrens;
    }

    public void setChildrens(Set<Person> people) {
        if (this.childrens != null) {
            this.childrens.forEach(i -> i.setFamily(null));
        }
        if (people != null) {
            people.forEach(i -> i.setFamily(this));
        }
        this.childrens = people;
    }

    public Family childrens(Set<Person> people) {
        this.setChildrens(people);
        return this;
    }

    public Family addChildrens(Person person) {
        this.childrens.add(person);
        person.setFamily(this);
        return this;
    }

    public Family removeChildrens(Person person) {
        this.childrens.remove(person);
        person.setFamily(null);
        return this;
    }

    public Person getFather() {
        return this.father;
    }

    public void setFather(Person person) {
        this.father = person;
    }

    public Family father(Person person) {
        this.setFather(person);
        return this;
    }

    public Person getMother() {
        return this.mother;
    }

    public void setMother(Person person) {
        this.mother = person;
    }

    public Family mother(Person person) {
        this.setMother(person);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Family)) {
            return false;
        }
        return getId() != null && getId().equals(((Family) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Family{" +
            "id=" + getId() +
            ", srNo='" + getSrNo() + "'" +
            ", motherMaidenName='" + getMotherMaidenName() + "'" +
            ", marriageDate='" + getMarriageDate() + "'" +
            ", familyPhoto='" + getFamilyPhoto() + "'" +
            ", familyPhotoContentType='" + getFamilyPhotoContentType() + "'" +
            "}";
    }
}
