package ct553.backend.pet.control;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.pet.entity.PetCustomer;

@Repository
public interface PetCustomerRepository extends JpaRepository<PetCustomer, Long> {
    
    List<PetCustomer> findAllByCustomer_Id(Long id);

}
