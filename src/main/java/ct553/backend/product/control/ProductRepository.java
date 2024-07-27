package ct553.backend.product.control;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.product.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
