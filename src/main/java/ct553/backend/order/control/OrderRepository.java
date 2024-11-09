package ct553.backend.order.control;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ct553.backend.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    @Query("""
        select distinct o 
        from ct553.backend.order.entity.Order o
        join OrderDetail od on od.order.id = o.id
        where o.status is not null
        group by o
    """)
    ArrayList<Order> findAll();
    
    ArrayList<Order> findByCustomer_Id(Long customerId);

    @Query("""
        select distinct o 
        from ct553.backend.order.entity.Order o
        join OrderDetail od on od.order.id = o.id
        join ServiceProduct sp on sp.id = od.productDetail.product.id
        join PetCustomerServiceProduct pcsp on pcsp.serviceProduct.id = sp.id
        where od.order.customer.id = :customerId
        group by o
    """)

    ArrayList<Order> findAllServiceProductOrdersByCustomer(Long customerId);

    @Query("""
        select distinct o 
        from ct553.backend.order.entity.Order o
        join OrderDetail od on od.order.id = o.id
        join ServiceProduct sp on sp.id = od.productDetail.product.id
        join PetCustomerServiceProduct pcsp on pcsp.serviceProduct.id = sp.id
        where od.order.customer.id = :customerId and sp.id = :productId
        group by o
    """)

    ArrayList<Order> findAllServiceProductOrdersByCustomerAndProduct(Long customerId, Long productId);
    
}

