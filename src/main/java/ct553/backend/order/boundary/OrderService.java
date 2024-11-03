package ct553.backend.order.boundary;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.cart.boundary.CartService;
import ct553.backend.order.control.OrderDetailRepository;
import ct553.backend.order.control.OrderRepository;
import ct553.backend.order.entity.Order;
import ct553.backend.order.entity.OrderCreationRequest;
import ct553.backend.order.entity.OrderDetail;
import ct553.backend.pet.boundary.PetCustomerService;
import ct553.backend.product.InventoryStatus;
import ct553.backend.product.pet.PetProduct;
import ct553.backend.product.productdetail.ProductDetail;
import ct553.backend.product.productdetail.ProductDetailService;
import ct553.backend.user.User;
import ct553.backend.user.UserService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class OrderService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderDetailRepository orderDetailRepository;

    @Autowired
    private UserService customerService;

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private PetCustomerService petCustomerService;

    public Order addOrder(Long customerId, OrderCreationRequest orderRequest) {
        User customer = this.customerService.findByIdCore(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not exist");
        }
        User basicCustomer = new User();
        basicCustomer.setId(customerId);
        Order order = Order.from(orderRequest, customer);
        
        this.orderRepository.save(order);
        this.addOrderDetailsToOrder(order.getId(), orderRequest.getCartDetails(), customer);
        return order;
    }

    public Order addOrder(Long customerId, Order order) {
        User customer = this.customerService.findByIdCore(customerId);
        order.setStatus(order.getStatus());
        order.setCustomer(customer);

        return this.orderRepository.save(order);
    }

    ArrayList<Order> findAllOrders(Long customerId) {
        ArrayList<Order> ordersDB = this.orderRepository.findByCustomer_Id(customerId);
        ArrayList<Order> ordersReverse = new ArrayList<>(ordersDB);
        Collections.reverse(ordersReverse);
        return new ArrayList<>(ordersReverse.stream().filter(
            o -> o.getOrderDetails() != null && !o.getOrderDetails().isEmpty() && o.getOrderDetails().get(0).getProductDetail().getPetServiceVariant() == null
        ).toList());
    }

    public ArrayList<Order> findAllServiceProductOrdersByCustomer(Long customerId) {
        ArrayList<Order> ordersDB = this.orderRepository.findAllServiceProductOrdersByCustomer(customerId);
        ArrayList<Order> ordersReverse = new ArrayList<>(ordersDB);
        Collections.reverse(ordersReverse);
        return ordersReverse;
    }

    public ArrayList<Order> findAllServiceProductOrdersByCustomer(Long customerId, Long productId, LocalDateTime serveFrom, LocalDateTime serveTo) {
        ArrayList<Order> ordersDB = this.orderRepository.findAllServiceProductOrdersByCustomerAndProduct(customerId, productId);
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

    ArrayList<OrderDetail> addOrderDetailsToOrder(Long orderId, List<Long> cartDetailsIdList, User customer) {

        cartDetailsIdList.stream().forEach(id -> {
            OrderDetail orderDetail = new OrderDetail(this.cartService.findCartDetailById(id));
            orderDetail.setOrder(this.findOrderById(orderId));
            this.cartService.deleteCartDetail(id);
            this.orderDetailRepository.save(orderDetail);
            
            ProductDetail productDetail = this.productDetailService.findProductDetailById(orderDetail.getProductDetail().getId());
            productDetail.setSold(productDetail.getSold() + orderDetail.getQuantity());
            if (productDetail.getSold() == productDetail.getQuantity()) {
                productDetail.setInventoryStatus(InventoryStatus.SOLD_OUT);
            }

            if (productDetail.getProduct() instanceof PetProduct) {
                this.petCustomerService.addFromPetProduct((PetProduct) productDetail.getProduct(), customer);
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

        // orderDB.setOrderDetails(order.getOrderDetails());
        orderDB.setStatus(order.getStatus());
        // orderDB.setCoupon(order.getCoupon());
        // if (order.getStaff() != null) {
        //     orderDB.setStaff(order.getStaff());
        // }
        if (order.getPayment() != null) {
            orderDB.setPayment(order.getPayment());
        }
        // if (order.getShipment() != null) {
        //     orderDB.setShipment(order.getShipment());
        // }
        // orderDB.setTotal(order.getTotal());

        return this.orderRepository.save(orderDB);
    }
}