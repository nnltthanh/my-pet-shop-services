package ct553.backend.product.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.product.entity.AccessoryProduct;

@Repository
public interface AccessoryProductRepository extends JpaRepository<AccessoryProduct, Long> {

}