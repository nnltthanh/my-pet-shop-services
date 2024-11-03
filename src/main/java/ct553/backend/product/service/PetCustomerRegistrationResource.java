package ct553.backend.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;

import ct553.backend.product.productdetail.ProductDetail;
import lombok.extern.slf4j.Slf4j;


@RestController
@RequestMapping("/pet-customers/registration")
@Slf4j
public class PetCustomerRegistrationResource {
    
    @Autowired
    PetCustomerRegistrationService petCustomerRegistrationService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PetCustomerServiceProduct> add(
        @RequestPart(value = "registration") PetCustomerServiceProduct petCustomerServiceProduct, 
        @RequestPart(value = "productDetails") ArrayList<ProductDetail> productDetails) {
        return new ResponseEntity<>(petCustomerRegistrationService.reserve(petCustomerServiceProduct, productDetails), HttpStatus.CREATED);
    }

    @GetMapping("/{customerId}")
    public List<PetCustomerServiceProduct> findAll(@PathVariable Long customerId) {
        return this.petCustomerRegistrationService.findAllByCustomerId(customerId);
    }


}


