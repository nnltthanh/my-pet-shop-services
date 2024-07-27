package ct553.backend.order.boundary;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ct553.backend.order.entity.Order;
import ct553.backend.order.entity.OrderDetail;

@RestController
@RequestMapping("/OrderForEmployee") // need to merge to order resource
public class OrderResourceForEmployee {
    @Autowired
    OrderService orderService;

    @GetMapping
    public ResponseEntity<ArrayList<Order>> getAllOrders() {
        return new ResponseEntity<>(this.orderService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}/details")
    public ResponseEntity<ArrayList<OrderDetail>> getAllOrderDetails(@PathVariable Long id) {
        return new ResponseEntity<>(this.orderService.findAllOrderDetailsByOrder(id), HttpStatus.OK);
    }
}
