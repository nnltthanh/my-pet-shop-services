package ct553.backend.product.accessory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessorySubCategoryRepository extends JpaRepository<AccessorySubCategory, Long> {

}