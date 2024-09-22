package ct553.backend.pet.control;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.pet.entity.PetBreed;
import ct553.backend.pet.entity.PetCategory;

@Repository
public interface PetCategoryRepository extends JpaRepository<PetCategory, Long> {

    Optional<PetCategory> findByNameIgnoreCaseAndBreed(String name, PetBreed breed);
}