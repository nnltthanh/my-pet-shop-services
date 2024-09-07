package ct553.backend.product.control;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ct553.backend.product.entity.Product;
import ct553.backend.product.entity.ProductSearchingCriteria;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            SELECT product FROM Product product WHERE
            (product.id IN 
                (SELECT pet.id FROM PetProduct pet WHERE pet.category.breed IN (:#{#criteria.breeds})) 
                AND product.price BETWEEN :#{#criteria.priceFrom} AND :#{#criteria.priceTo})
            OR 
            (product.id IN 
                (SELECT accessory.id FROM AccessoryProduct accessory WHERE accessory.subCategory.category IN (:#{#criteria.accessoryCategories})) 
                AND product.price BETWEEN :#{#criteria.priceFrom} AND :#{#criteria.priceTo})
        """)
    Page<Product> findAllBy(@Param("criteria") ProductSearchingCriteria searchingCriteria, Pageable pageable);

}
