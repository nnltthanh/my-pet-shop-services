package ct553.backend.cart.control;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ct553.backend.cart.entity.Cart;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    
    Optional<Cart> findByCustomer_Id(Long customerId);

    @Query("""
            SELECT COUNT(cd) FROM Cart c
            LEFT JOIN CartDetail cd on c.id = cd.cart.id
            WHERE
            c.customer.id = :customerId
        """)
    Long countCartDetailsByCustomerId(Long customerId);
    
}
