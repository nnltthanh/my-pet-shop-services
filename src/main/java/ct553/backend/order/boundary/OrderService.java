package ct553.backend.order.boundary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.cart.boundary.CartService;
import ct553.backend.customer.Customer;
import ct553.backend.customer.CustomerService;
import ct553.backend.order.control.OrderDetailRepository;
import ct553.backend.order.control.OrderRepository;
import ct553.backend.order.entity.Order;
import ct553.backend.order.entity.OrderDetail;
import ct553.backend.product.boundary.ProductDetailService;
import ct553.backend.product.entity.InventoryStatus;
import ct553.backend.product.entity.ProductDetail;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductDetailService productDetailService;

    void addOrder(Long customerId, Order order) {
        Customer customer = this.customerService.findById(customerId);
        order.setStatus(order.getStatus());
        order.setCustomer(customer);

        this.orderRepository.save(order);
    }

    ArrayList<Order> findAllOrders(Long customerId) {
        ArrayList<Order> ordersDB = this.orderRepository.findByCustomer_Id(customerId);
        ArrayList<Order> ordersReverse = new ArrayList<>(ordersDB);
        Collections.reverse(ordersReverse);
        return ordersReverse;
    }

    ArrayList<Order> findAll() {
        return (ArrayList<Order>) this.orderRepository.findAll();
    }

    public Order findOrderById(Long id) {
        return this.orderRepository.findById(id).orElse(null);
    }

    void cancelOrder(Long orderId) {
        this.orderRepository.deleteById(orderId);
    }

    ArrayList<OrderDetail> addOrderDetailsToOrder(Long orderId, Long[] cartDetailsIdList) {

        Arrays.stream(cartDetailsIdList).forEach(id -> {
            OrderDetail orderDetail = new OrderDetail(this.cartService.findCartDetailById(id));
            orderDetail.setOrder(this.findOrderById(orderId));
            this.cartService.deleteCartDetail(id);
            this.orderDetailRepository.save(orderDetail);
            
            ProductDetail productDetail = this.productDetailService.findProductDetailById(orderDetail.getProductDetail().getId());
            productDetail.setSold(productDetail.getSold() + orderDetail.getQuantity());
            if (productDetail.getSold() == productDetail.getQuantity()) {
                productDetail.setInventoryStatus(InventoryStatus.SOLD_OUT);
            }
            this.productDetailService.updateProductDetail(productDetail);
        });

        return this.findAllOrderDetailsByOrder(orderId);
    }

    ArrayList<OrderDetail> findAllOrderDetailsByOrder(Long orderId) {
        return (ArrayList<OrderDetail>) this.orderDetailRepository.findByOrder_Id(orderId);
    }

    public OrderDetail findOrderDetailById(Long id) {
        return this.orderDetailRepository.findById(id).orElse(null);
    }

    public Order updateOrder(Long orderId, Order order) {
        Order orderDB = this.findOrderById(orderId);

        orderDB.setOrderDetails(order.getOrderDetails());
        orderDB.setStatus(order.getStatus());
        // orderDB.setCoupon(order.getCoupon());
        // if (order.getStaff() != null) {
        //     orderDB.setStaff(order.getStaff());
        // }
        if (order.getPayment() != null) {
            orderDB.setPayment(order.getPayment());
        }
        if (order.getShipment() != null) {
            orderDB.setShipment(order.getShipment());
        }
        orderDB.setTotal(order.getTotal());

        System.out.println("New order detail saved in DB: " + orderDB);
        return this.orderRepository.save(orderDB);
    }
}