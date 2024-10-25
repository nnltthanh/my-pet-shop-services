package ct553.backend.pet.boundary;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.pet.entity.PetCustomer;

@RestController
@RequestMapping("/my-pets")
public class PetCustomerResource {

    @Autowired
    PetCustomerService petCustomerService;

    @GetMapping("/customers/{id}")
    public List<PetCustomer> findAllByCustomer(@PathVariable Long id) {
        return this.petCustomerService.findAllByCustomer(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Long id) {
        PetCustomer Pet = petCustomerService.findById(id);
        if (Pet == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Pet, HttpStatus.OK);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> add(@RequestPart(value = "petCustomer") PetCustomer pet,
    @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        this.petCustomerService.add(pet, multipartFile);
        return new ResponseEntity<>(pet, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> update(@PathVariable Long id, 
                    @RequestPart(value = "petCustomer") PetCustomer pet,
                    @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        this.petCustomerService.update(id, pet, multipartFile);
        return new ResponseEntity<>(pet, HttpStatus.CREATED);
    }

}
