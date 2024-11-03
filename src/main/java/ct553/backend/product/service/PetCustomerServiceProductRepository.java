package ct553.backend.product.service;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PetCustomerServiceProductRepository extends JpaRepository<PetCustomerServiceProduct, Long> {

    List<PetCustomerServiceProduct> findAllByServeFor_Customer_Id(Long id);

}
