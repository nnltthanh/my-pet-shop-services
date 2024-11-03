package ct553.backend.product.productdetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {

    @Query("SELECT pd FROM ProductDetail pd LEFT JOIN FETCH pd.petServiceVariant WHERE pd.product.id = :productId")
    ArrayList<ProductDetail> findByProduct_Id(Long productId);

    @Query("SELECT sum(pd.sold) from ProductDetail pd where pd.product.id = :productId")
    Long countSoldByProduct_Id(Long productId);

}
