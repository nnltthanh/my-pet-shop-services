package ct553.backend.product.boundary;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ct553.backend.product.entity.AccessorySubCategory;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/accessory-categories")
public class AccessorySubCategoryResource {
    
    @Autowired
    AccessorySubCategoryService accessoryCategoryService;

    @GetMapping
    public ArrayList<AccessorySubCategory> getAll() {
        return this.accessoryCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        AccessorySubCategory accessory = accessoryCategoryService.findById(id);
        if (accessory == null) {
            return new ResponseEntity<>("This accessory category is not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(accessory, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody AccessorySubCategory accessory) {
        this.accessoryCategoryService.add(accessory);
        return new ResponseEntity<>(accessory, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        AccessorySubCategory accessory = accessoryCategoryService.findById(id);
        if (accessory == null) {
            return new ResponseEntity<>("This accessory category is not exist", HttpStatus.NOT_FOUND);
        }
        this.accessoryCategoryService.deleteById(id);
        return new ResponseEntity<>("A accessory category with id =" + id + " is deleted successfully", HttpStatus.OK);
    }

}
