package ct553.backend.product.boundary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataService;
import ct553.backend.imagedata.ImageDataType;
import ct553.backend.product.entity.ProductDetail;
import ct553.backend.product.entity.ProductDetailDTO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/product-details")
@Slf4j
public class ProductDetailResource {
    
    @Autowired
    ProductDetailService productService;

    @Autowired
    ImageDataService imageDataService;

    @GetMapping(value = { "", "/" })
    public List<ProductDetail> getAllProductDetails() {
        return this.productService.getListProductDetails();
    }

    @GetMapping(value = "/find-by-product/{productId}")
    public List<ProductDetailDTO> getAllProductDetails(@PathVariable Long productId) {
        return this.productService.getAllProductDetails(productId);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> getProductDetailById(@PathVariable Long id) {
        ProductDetail productDetail = this.productService.findProductDetailById(id);
        if (productDetail == null) {
            return new ResponseEntity<>("This product detail is not exist",
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productDetail, HttpStatus.OK);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductDetail> addProductDetail(@Valid @RequestPart(value = "productDetail") ProductDetail productDetail,
            @RequestPart(value = "images", required = false) List<MultipartFile> files) throws IOException {
        ImageData imageData = this.imageDataService.buildImageData(files, ImageDataType.PRODUCT_DETAIL);
        this.productService.add(productDetail, imageData);
        return new ResponseEntity<>(productDetail, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProductDetail(@PathVariable Long id, @RequestBody ProductDetail updatedProductDetailInfo) {
        ProductDetail existingProductDetail = this.productService.findProductDetailById(id);
        if (existingProductDetail == null) {
            return new ResponseEntity<>("Can not find product detail to update", HttpStatus.NOT_FOUND);
        }

        ProductDetail updatedProductDetail = this.productService.updateProductDetail(id, updatedProductDetailInfo);
        return new ResponseEntity<>(updatedProductDetail, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}/upload")
    public ResponseEntity<?> upload(@PathVariable Long productId, @PathVariable Long id,
            @RequestParam("images") List<MultipartFile> files) throws IOException {
        ImageData imageData = this.imageDataService.buildImageData(files, ImageDataType.PRODUCT_DETAIL);
        ProductDetail detail = this.productService.updateProductDetailImageData(id, imageData);
        return new ResponseEntity<>(detail, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductDetailById(@PathVariable Long id) {
        ProductDetail productDetail = this.productService.findProductDetailById(id);
        if (productDetail == null) {
            return new ResponseEntity<>("Can not find product detail to delete", HttpStatus.NOT_FOUND);
        }

        this.productService.deleteProductDetailById(id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}
