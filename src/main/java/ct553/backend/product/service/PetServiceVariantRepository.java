package ct553.backend.product.service;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.product.productdetail.ProductDetail;

@Repository
public interface PetServiceVariantRepository extends JpaRepository<PetServiceVariant, Long> {


}
