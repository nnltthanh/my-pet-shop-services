package ct553.backend.product.pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ct553.backend.product.ProductSearchingCriteria;

@Repository
public interface PetProductRepository extends JpaRepository<PetProduct, Long> {

    @Query(value = """
        SELECT new ct553.backend.product.pet.PetProduct(
            pet, 
            CASE 
                WHEN COUNT(detail.inventoryStatus) FILTER (WHERE detail.inventoryStatus = ct553.backend.product.InventoryStatus.ON_HAND) > 0 THEN ct553.backend.product.InventoryStatus.ON_HAND
                WHEN COUNT(detail.inventoryStatus) FILTER (WHERE detail.inventoryStatus = ct553.backend.product.InventoryStatus.INCOMING) > 0 THEN ct553.backend.product.InventoryStatus.INCOMING
                ELSE ct553.backend.product.InventoryStatus.SOLD_OUT
            END,
            SUM(COALESCE(detail.sold, 0)),
            AVG(COALESCE(review.rate, 0)),
            COUNT(review.id)
        )
        FROM PetProduct pet 
        LEFT JOIN ProductDetail detail ON pet.id = detail.product.id
        LEFT JOIN Review review ON pet.id = review.orderDetail.productDetail.product.id
        JOIN PetCategory category ON pet.category.id = category.id
        WHERE pet.category.breed IN (:#{#criteria.breeds}) 
        AND pet.price BETWEEN :#{#criteria.priceFrom} AND :#{#criteria.priceTo}
        GROUP BY pet, category
        """)
    Page<PetProduct> findAllBy(@Param("criteria") ProductSearchingCriteria searchingCriteria, Pageable pageable);


}