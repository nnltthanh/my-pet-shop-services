package ct553.backend.order.control;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.antlr.v4.runtime.atn.SemanticContext.AND;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ct553.backend.order.entity.Order;
import ct553.backend.statistic.StatisticDetail;

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

    @Query("SELECT COALESCE(SUM(o.total), 0) FROM ct553.backend.order.entity.Order o WHERE o.status = 'FINISHED' AND o.createDate >= :currentMonthStart AND o.createDate <= :currentMonthEnd")
    BigDecimal sumOfFinishedOrdersInCurrentMonth(@Param("currentMonthStart") LocalDate currentMonthStart,
            @Param("currentMonthEnd") LocalDate currentMonthEnd);

    @Query("""
               SELECT COUNT(o) FROM ct553.backend.order.entity.Order o
               LEFT JOIN OrderDetail od on o.id = od.order.id
               WHERE
                (
                    od.productDetail.product.id IN (
                        SELECT pet.id FROM PetProduct pet
                    )
                ) AND o.createDate >= :currentMonthStart AND o.createDate <= :currentMonthEnd
            """)
    Long countOrdersInCurrentMonth(@Param("currentMonthStart") LocalDate currentMonthStart,
            @Param("currentMonthEnd") LocalDate currentMonthEnd);

    @Query("""
                SELECT COUNT(DISTINCT od.productDetail.product.id)
                FROM Order o
                LEFT JOIN OrderDetail od ON o.id = od.order.id
                WHERE od.productDetail.product.id IN (
                    SELECT DISTINCT pet.id FROM PetProduct pet
                )
                AND o.createDate >= :currentMonthStart AND o.createDate <= :currentMonthEnd
            """)
    Long countNonServiceProductsInCurrentMonth(@Param("currentMonthStart") LocalDate currentMonthStart,
            @Param("currentMonthEnd") LocalDate currentMonthEnd);

    @Query("""
               SELECT COUNT(DISTINCT pcsp.id)
               FROM PetCustomerServiceProduct pcsp
               JOIN ct553.backend.order.entity.Order o ON o.id = pcsp.order.id
               WHERE o.createDate >= :currentMonthStart AND o.createDate <= :currentMonthEnd
            """)
    Long countServiceProductsInCurrentMonth(@Param("currentMonthStart") LocalDate currentMonthStart,
            @Param("currentMonthEnd") LocalDate currentMonthEnd);


}
