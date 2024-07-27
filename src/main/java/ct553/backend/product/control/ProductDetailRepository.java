package ct553.backend.product.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.product.entity.ProductDetail;

import java.util.ArrayList;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    ArrayList<ProductDetail> findByProduct_Id(Long productId);
    
}
