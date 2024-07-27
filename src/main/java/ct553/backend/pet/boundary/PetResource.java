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

import ct553.backend.pet.entity.Pet;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pets")
public class PetResource {
    
    @Autowired
    PetService petService;

    @GetMapping
    public ArrayList<Pet> getAllPets() {
        return this.petService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Long id) {
        Pet Pet = petService.findById(id);
        if (Pet == null) {
            return new ResponseEntity<>("This Pet is not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Pet, HttpStatus.FOUND);
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody Pet Pet) {
        this.petService.add(Pet);
        return new ResponseEntity<>(Pet, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Pet Pet = petService.findById(id);
        if (Pet == null) {
            return new ResponseEntity<>("This pet is not exist", HttpStatus.NOT_FOUND);
        }
        this.petService.deleteById(id);
        return new ResponseEntity<>("A pet with id =" + id + " is deleted successfully", HttpStatus.OK);
    }

}
