package ct553.backend.pet.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.pet.entity.Pet;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

}