package com.nayakfamily.familytree.repository;

import com.nayakfamily.familytree.domain.Family;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Family entity.
 */
@Repository
public interface FamilyRepository extends JpaRepository<Family, Long> {
    default Optional<Family> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Family> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Family> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select family from Family family left join fetch family.father left join fetch family.mother",
        countQuery = "select count(family) from Family family"
    )
    Page<Family> findAllWithToOneRelationships(Pageable pageable);

    @Query("select family from Family family left join fetch family.father left join fetch family.mother")
    List<Family> findAllWithToOneRelationships();

    @Query("select family from Family family left join fetch family.father left join fetch family.mother where family.id =:id")
    Optional<Family> findOneWithToOneRelationships(@Param("id") Long id);
}
