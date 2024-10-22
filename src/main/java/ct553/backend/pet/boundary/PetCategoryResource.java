package ct553.backend.pet.boundary;

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

import ct553.backend.pet.entity.PetCategory;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pet-categories")
public class PetCategoryResource {
    
    @Autowired
    PetCategoryService petCategoryService;

    @GetMapping
    public ArrayList<PetCategory> getAll() {
        return this.petCategoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        PetCategory Pet = petCategoryService.findById(id);
        if (Pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Pet, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody PetCategory Pet) {
        this.petCategoryService.add(Pet);
        return new ResponseEntity<>(Pet, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        PetCategory Pet = petCategoryService.findById(id);
        if (Pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.petCategoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
