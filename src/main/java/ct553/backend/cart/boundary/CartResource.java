package ct553.backend.cart.boundary;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ct553.backend.auth.RoleName;
import ct553.backend.cart.entity.CartDetail;
import jakarta.annotation.security.RolesAllowed;

@RestController
@RequestMapping("/customers/{customerId}/cart")
public class CartResource {

    @Autowired
    CartService cartService;

    @PostMapping
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.CUSTOMER, RoleName.ADMIN})
    public ResponseEntity<?> addProductDetailToCart(@PathVariable Long customerId,
             @RequestBody CartDetail cartDetail) {
        CartDetail cartDetailDB = this.cartService.addProductDetailToCart(customerId, cartDetail);
        if (cartDetailDB == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cartDetailDB, HttpStatus.CREATED);
    }

    @GetMapping
    @RolesAllowed({RoleName.CUSTOMER})
    public ResponseEntity<?> getCart(@PathVariable Long customerId) {
        ArrayList<CartDetail> cartDetails = this.cartService.getAllCartDetails(customerId);
        return new ResponseEntity<>(cartDetails, HttpStatus.OK);
    }

    @GetMapping("/{cartDetailId}")
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.CUSTOMER, RoleName.ADMIN})
    public ResponseEntity<?> getCartDetail(@PathVariable Long cartDetailId) {
        CartDetail cartDetail = this.cartService.findCartDetailById(cartDetailId);
        if (cartDetail != null) {
            return new ResponseEntity<>(cartDetail, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.CUSTOMER, RoleName.ADMIN})
    public void deleteCartDetail(@PathVariable(value = "customerId") Long customerId, @PathVariable(value = "id") Long id) {
        this.cartService.deleteCartDetail(id);
    }

    @PutMapping
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.CUSTOMER, RoleName.ADMIN})
    public ResponseEntity<?> updateCartDetail(@RequestBody CartDetail cartDetail) {
        CartDetail updatedCartDetail = this.cartService.updateCartDetail(cartDetail);
        return new ResponseEntity<>(updatedCartDetail, HttpStatus.OK);
    }

}
