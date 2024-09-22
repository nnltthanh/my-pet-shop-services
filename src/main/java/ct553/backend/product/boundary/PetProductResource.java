package ct553.backend.product.boundary;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.product.entity.PetProduct;
import ct553.backend.product.entity.PetProductOverviewResponse;
import ct553.backend.product.entity.ProductSearchingCriteria;
import ct553.backend.product.entity.ProductSortingCriteria;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products/pets")
@Slf4j
public class PetProductResource {

    @Autowired
    PetProductService petService;

    @GetMapping({ "/", "", "/search", "search" })
    @ResponseStatus(value = HttpStatus.OK)
    public PetProductOverviewResponse findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int numberOfPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "1000000000") int pageSize,
            @Valid ProductSearchingCriteria productSearchingCriteria,
            @Valid ProductSortingCriteria productSortingCriteria) {
        return this.petService.findPetProductOverviewResponseBy(
                productSortingCriteria,
                productSearchingCriteria,
                PageRequest.of(numberOfPage, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Long id) {
        PetProduct Pet = petService.findById(id);
        if (Pet == null) {
            return new ResponseEntity<>("This Pet is not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Pet, HttpStatus.FOUND);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<PetProduct> add(@Valid @RequestPart(value = "petProduct") PetProduct petProduct,
            @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(this.petService.add(petProduct, multipartFile), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> updateProduct(@PathVariable Long id, 
                            @Valid @RequestPart(value = "petProduct") PetProduct updatedProductInfo,
                            @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        PetProduct existingProduct = this.petService.findById(id);
        if (existingProduct == null) {
            return new ResponseEntity<>("Can not find product to update", HttpStatus.NOT_FOUND);
        }

        PetProduct updatedProduct = this.petService.updateProduct(id, updatedProductInfo, multipartFile);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        PetProduct Pet = petService.findById(id);
        if (Pet == null) {
            return new ResponseEntity<>("This pet is not exist", HttpStatus.NOT_FOUND);
        }
        this.petService.deleteById(id);
        return new ResponseEntity<>("A pet with id =" + id + " is deleted successfully", HttpStatus.OK);
    }

}
