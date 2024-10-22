package ct553.backend.product.boundary;

import java.io.IOException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.product.entity.AccessoryProduct;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products/accessories")
@Slf4j
public class AccessoryProductResource {

    @Autowired
    AccessoryProductService accessoryService;

    @GetMapping
    public ArrayList<AccessoryProduct> getAll() {
        return this.accessoryService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAccessoryById(@PathVariable Long id) {
        AccessoryProduct Accessory = accessoryService.findById(id);
        if (Accessory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(Accessory, HttpStatus.FOUND);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<AccessoryProduct> add(@Valid @RequestPart(value = "accessoryProduct") AccessoryProduct accessoryProduct,
            @RequestPart(value = "image", required = false) MultipartFile multipartFile) throws IOException {
        return new ResponseEntity<>(this.accessoryService.add(accessoryProduct, multipartFile), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        AccessoryProduct Accessory = accessoryService.findById(id);
        if (Accessory == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        this.accessoryService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
