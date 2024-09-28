package ct553.backend.product.boundary;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataService;
import ct553.backend.imagedata.ImageDataType;
import ct553.backend.product.entity.Product;
import ct553.backend.product.entity.ProductOverviewResponse;
import ct553.backend.product.entity.ProductSearchingCriteria;
import ct553.backend.product.entity.ProductSortingCriteria;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductResource {

    @Autowired
    ProductService productService;

    @Autowired
    ImageDataService imageDataService;

    @GetMapping({ "/", "", "/search", "search" })
    @ResponseStatus(value = HttpStatus.OK)
    public ProductOverviewResponse findAll(
            @RequestParam(value = "page", required = false, defaultValue = "0") int numberOfPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "1000000000") int pageSize,
            @Valid ProductSearchingCriteria productSearchingCriteria,
            @Valid ProductSortingCriteria productSortingCriteria) {
        return this.productService.findProductOverviewResponseBy(
                productSortingCriteria,
                productSearchingCriteria,
                PageRequest.of(numberOfPage, pageSize));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Product product = this.productService.findProductById(id);
        if (product == null) {
            return new ResponseEntity<>("This product is not exist",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updatedProductInfo) {
        Product existingProduct = this.productService.findProductById(id);
        if (existingProduct == null) {
            return new ResponseEntity<>("Can not find product to update",
                    HttpStatus.NOT_FOUND);
        }

        Product updatedProduct = this.productService.updateProduct(id,
                updatedProductInfo);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping(value = "/{productId}/upload")
    public ResponseEntity<?> uploadProductImage(@PathVariable Long productId,
            @RequestParam("images") List<MultipartFile> files) throws IOException {
        ImageData imageData = this.imageDataService.buildImageData(files, ImageDataType.PRODUCT);
        Product product = this.productService.updateProductImages(productId, imageData);
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProductById(@PathVariable Long id) {
        Product product = this.productService.findProductById(id);
        if (product == null) {
            return new ResponseEntity<>("Can not find product to delete",
                    HttpStatus.NOT_FOUND);
        }

        this.productService.deleteProductById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}