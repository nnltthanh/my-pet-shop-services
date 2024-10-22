package ct553.backend.importer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/import")
public class PetProductImporterResource {
    
    @Autowired
    PetProductImporterService petProductImporterService;

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<?> importProduct(@RequestParam(name = "productType", required = false) String productType, 
    @RequestPart(value = "importFile", required = true) MultipartFile file) throws IOException {
        this.petProductImporterService.importProducts(file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
