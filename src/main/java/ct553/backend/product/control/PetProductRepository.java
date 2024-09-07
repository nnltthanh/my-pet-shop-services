package ct553.backend.product.control;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.pet.entity.PetBreed;
import ct553.backend.product.entity.PetProduct;

@Repository
public interface PetProductRepository extends JpaRepository<PetProduct, Long> {

    Page<PetProduct> findAllByPriceBetweenAndCategoryBreedIn(BigDecimal fromPrice, BigDecimal toPrice,
            List<PetBreed> breeds, Pageable pageable);

}