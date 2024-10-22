package ct553.backend.shipment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ct553.backend.order.boundary.OrderService;
import ct553.backend.order.entity.Order;

@RestController
@RequestMapping("/shipment")
public class ShipmentResource {

    @Autowired
    private ShipmentService shipmentService;

    @Autowired
    private OrderService orderService;

    @GetMapping
    public ResponseEntity<?> getAllShipment() {
        return new ResponseEntity<>(this.shipmentService.findAllShipments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getShipmentById(@PathVariable Long id) {
        Shipment shipment = this.shipmentService.findShipmentById(id);
        if (shipment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shipment, HttpStatus.FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteShipmentById(@PathVariable Long id) {
        Shipment shipment = this.shipmentService.findShipmentById(id);
        if (shipment == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.shipmentService.deleteShipment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateShipmentById(@PathVariable Long id, @RequestBody Shipment shipment) {
        if (this.shipmentService.updateShipment(id, shipment) != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{orderId}")
    public ResponseEntity<?> addShipment(@PathVariable Long orderId, @RequestBody Shipment shipment) {
        // Shipment isExistedShipment =
        // this.shipmentService.findShipmentById(shipment.getId());
        // if (isExistedShipment == null) {
        // this.shipmentService.addShipment(shipment);
        // return new ResponseEntity<>(shipment, HttpStatus.CREATED);
        // }
        // return new ResponseEntity<>("The shipment with id=" + shipment.getId() + "
        // existed. Try again!",
        // HttpStatus.BAD_REQUEST);
        Shipment newShipment = this.shipmentService.addShipment(shipment);

        Order order = this.orderService.findOrderById(orderId);
        order.setShipment(newShipment);
        this.orderService.updateOrder(orderId, order);
        return new ResponseEntity<>(shipment, HttpStatus.CREATED);
    }
}
