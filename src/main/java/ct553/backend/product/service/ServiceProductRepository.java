package ct553.backend.product.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface ServiceProductRepository extends JpaRepository<ServiceProduct, Long> {

    List<ServiceProduct> findAllByType(ServiceProductType type);

}