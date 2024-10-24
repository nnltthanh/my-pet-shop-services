package ct553.backend.imagedata;

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

// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/image-data")
public class ImageDataResource {

    @Autowired
    ImageDataService imageDataService;

    @GetMapping
    public ResponseEntity<?> getAllImageData() {
        return new ResponseEntity<>(this.imageDataService.findAll(), HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageDataById(@PathVariable Long id) {
        ImageData imageData = this.imageDataService.findById(id);
        if (imageData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(imageData, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImageDataById(@PathVariable Long id) {
        ImageData imageData = this.imageDataService.findById(id);
        if (imageData == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.imageDataService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateImageDataById(@PathVariable Long id, @RequestBody ImageData imageData) {
        if (this.imageDataService.update(id, imageData) != null) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping
    public void addImageData(@RequestBody ImageData imageData) {
        imageDataService.addImageData(imageData);
    }

}
