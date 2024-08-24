package ct553.backend.product.control;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByPriceGreaterThanEqual(BigDecimal fromPrice);

    List<Product> findByPriceLessThanEqual(BigDecimal toPrice);

    List<Product> findAllByPriceBetween(BigDecimal fromPrice, BigDecimal toPrice, Pageable pageable);

    Long countAllByPriceBetween(BigDecimal fromPrice, BigDecimal toPrice);

}
