package ct553.backend.product.boundary;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.product.entity.PetProduct;
import ct553.backend.product.entity.ProductSortingCriteria;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products/pets")
@Slf4j
public class PetProductResource {
    
    @Autowired
    PetProductService petService;

    @GetMapping({"/", ""})
    public ArrayList<PetProduct> getAllPets(
                                @RequestParam(value = "updatedAt", required = false) Sort.Direction updatedAtOrder,
                                @RequestParam(value = "rating", required = false) Sort.Direction ratingOrder,
                                @RequestParam(value = "price", required = false) Sort.Direction priceOrder,
                                @RequestParam(value = "page", required = false, defaultValue = "0") int numberOfPage,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize) {
        return this.petService.findAllBy(new ProductSortingCriteria(updatedAtOrder, ratingOrder, priceOrder), PageRequest.of(numberOfPage, pageSize));
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
    public ResponseEntity<PetProduct> add(@Valid @RequestPart(value = "petProduct") PetProduct petProduct, @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(this.petService.add(petProduct, multipartFile), HttpStatus.CREATED);
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
