package ct553.backend.product.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.order.boundary.OrderService;
import ct553.backend.order.entity.Order;
import ct553.backend.order.entity.OrderDetail;
import ct553.backend.product.productdetail.ProductDetail;
import ct553.backend.user.User;
import ct553.backend.user.UserService;
import jakarta.transaction.Transactional;

@Service
public class PetCustomerRegistrationService {
    
    @Autowired
    PetCustomerServiceProductRepository petCustomerServiceProductRepository;

    @Autowired
    OrderService orderService;

    @Autowired
    UserService userService;

    @Transactional
    PetCustomerServiceProduct reserve(PetCustomerServiceProduct petCustomerServiceProduct, List<ProductDetail> productDetails) {
        Order order = new Order();
        order.setCustomer(new User(userService.getLoggedInUser().getId()));
        List<OrderDetail> orderDetails = new ArrayList<>();
        BigDecimal sum = BigDecimal.ZERO.add(petCustomerServiceProduct.getServiceProduct().getPrice());

        for( ProductDetail pd : productDetails) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setProductDetail(pd);
            orderDetail.setTotal(pd.getPetServiceVariant().getAddPrice());
            sum = sum.add(orderDetail.getTotal());
            orderDetail.setOrder(order);
            orderDetails.add(orderDetail);  
        }
    
        order.setOrderDetails(orderDetails);
        order.setTotal(sum);
        petCustomerServiceProduct.setOrder(order);
        orderService.addOrder(userService.getLoggedInUser().getId(), order);
        return this.petCustomerServiceProductRepository.save(petCustomerServiceProduct);
    }

    public List<PetCustomerServiceProduct> findAllByCustomerId(Long id) {
        return this.petCustomerServiceProductRepository.findAllByServeFor_Customer_Id(id)
                    .stream().sorted(Comparator.comparing(PetCustomerServiceProduct::getId).reversed())
                    .toList();
        // List<Long> productIds = list.stream().map(l -> l.getServiceProduct().getId()).toList();

    }

    

}
