package ct553.backend.product.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.auth.RoleName;
import ct553.backend.product.productdetail.ProductDetail;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products/services")
@Slf4j
public class ServiceProductResource {

    @Autowired
    ServiceProductService serviceProductService;

    // @GetMapping({ "/", "", "/search", "search" })
    // @ResponseStatus(value = HttpStatus.OK)
    // public PetProductOverviewResponse findAll(
    //         @RequestParam(value = "page", required = false, defaultValue = "0") int numberOfPage,
    //         @RequestParam(value = "pageSize", required = false, defaultValue = "1000000000") int pageSize,
    //         @Valid ProductSearchingCriteria productSearchingCriteria,
    //         @Valid ProductSortingCriteria productSortingCriteria) {
    //     return this.petService.findPetProductOverviewResponseBy(
    //             productSortingCriteria,
    //             productSearchingCriteria,
    //             PageRequest.of(numberOfPage, pageSize));
    // }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        ServiceProduct Pet = serviceProductService.findById(id);
        if (Pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Pet, HttpStatus.OK);
    }

    @GetMapping
    public List<ServiceProduct> findAll() {
        return this.serviceProductService.findAll();
    }

    @GetMapping("/types/{type}")
    public List<ServiceProduct> findAllByType(@PathVariable ServiceProductType type) {
        return this.serviceProductService.findAllByType(type)
                    .stream().filter(p -> p.getValidTo() == null)
                    .toList();
    }
    

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.ADMIN})
    public ResponseEntity<ServiceProduct> add(@Valid 
            @RequestPart(value = "serviceProduct") ServiceProduct serviceProduct,
            @RequestPart(value = "productDetails") List<ProductDetail> productDetails,
            @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(this.serviceProductService.add(serviceProduct, productDetails, multipartFile), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}" ,consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.ADMIN})
    public ResponseEntity<ServiceProduct> update(
            @PathVariable Long id,
            @RequestPart(value = "serviceProduct") ServiceProduct serviceProduct,
            @RequestPart(value = "productDetails") List<ProductDetail> productDetails,
            @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(this.serviceProductService.update(id, serviceProduct, productDetails, multipartFile), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed({RoleName.RECEPTIONIST, RoleName.SERVICE_STAFF, RoleName.ADMIN})
    public ResponseEntity<?> deleteProductById(@PathVariable Long id) {
        this.serviceProductService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
