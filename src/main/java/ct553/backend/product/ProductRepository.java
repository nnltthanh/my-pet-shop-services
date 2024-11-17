package ct553.backend.product;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
        SELECT DISTINCT new ct553.backend.product.Product(
        product,
        CASE 
            WHEN COUNT(detail.inventoryStatus) FILTER (WHERE detail.inventoryStatus = ct553.backend.product.InventoryStatus.ON_HAND) > 0 THEN ct553.backend.product.InventoryStatus.ON_HAND
            WHEN COUNT(detail.inventoryStatus) FILTER (WHERE detail.inventoryStatus = ct553.backend.product.InventoryStatus.INCOMING) > 0 THEN ct553.backend.product.InventoryStatus.INCOMING
            ELSE ct553.backend.product.InventoryStatus.SOLD_OUT
        END,
        SUM(detail.sold),
        AVG(COALESCE(review.rate, 0)),
        COUNT(review.id)
    ) 
    FROM Product product
    LEFT JOIN ProductDetail detail ON product.id = detail.product.id
    LEFT JOIN Review review ON product.id = review.orderDetail.productDetail.product.id
    WHERE 
        (
            product.id IN (
                SELECT pet.id FROM PetProduct pet 
                WHERE pet.category.breed IN (:#{#criteria.breeds}) 
                AND (LOWER(pet.category.name) LIKE LOWER(:#{#criteria.keyword}) OR LOWER(pet.name) LIKE LOWER(:#{#criteria.keyword}))
            )
            AND product.price BETWEEN :#{#criteria.priceFrom} AND :#{#criteria.priceTo}
            AND product.validTo IS NULL
        )
        OR
        (
            product.id IN (
                SELECT accessory.id FROM AccessoryProduct accessory 
                WHERE accessory.subCategory.category IN (:#{#criteria.accessoryCategories})
            )
            AND product.price BETWEEN :#{#criteria.priceFrom} AND :#{#criteria.priceTo}
            AND product.validTo IS NULL
        )
    GROUP BY product
    """)
    Page<Product> findAllBy(@Param("criteria") ProductSearchingCriteria searchingCriteria, Pageable pageable);

}
