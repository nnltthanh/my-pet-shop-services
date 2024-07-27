package ct553.backend.order.control;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ct553.backend.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    ArrayList<Order> findByCustomer_Id(Long customerId);
    
}

