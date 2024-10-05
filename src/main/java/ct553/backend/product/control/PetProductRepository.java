package ct553.backend.product.control;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ct553.backend.product.entity.PetProduct;
import ct553.backend.product.entity.ProductSearchingCriteria;

@Repository
public interface PetProductRepository extends JpaRepository<PetProduct, Long> {

    @Query(value = """
            SELECT new ct553.backend.product.entity.PetProduct(
                pet, 
                CASE 
                    WHEN COUNT(CASE WHEN detail.inventoryStatus = ct553.backend.product.entity.InventoryStatus.ON_HAND THEN 1 END) > 0 THEN ct553.backend.product.entity.InventoryStatus.ON_HAND
                    WHEN COUNT(CASE WHEN detail.inventoryStatus = ct553.backend.product.entity.InventoryStatus.INCOMING THEN 1 END) > 0 THEN ct553.backend.product.entity.InventoryStatus.INCOMING
                    ELSE ct553.backend.product.entity.InventoryStatus.SOLD_OUT
                END,
                SUM(detail.sold)
            )
            FROM PetProduct pet 
            JOIN ProductDetail detail ON pet.id = detail.product.id
            WHERE pet.category.breed IN (:#{#criteria.breeds}) 
            AND pet.price BETWEEN :#{#criteria.priceFrom} AND :#{#criteria.priceTo}
            GROUP BY pet
        """)
    Page<PetProduct> findAllBy(@Param("criteria") ProductSearchingCriteria searchingCriteria, Pageable pageable);


}