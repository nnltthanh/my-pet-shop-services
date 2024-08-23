package ct553.backend.product.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.product.entity.PetProduct;

@Repository
public interface PetProductRepository extends JpaRepository<PetProduct, Long> {


}