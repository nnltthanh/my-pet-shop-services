package ct553.backend.product.boundary;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.CloudinaryService;
import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataService;
import ct553.backend.imagedata.ImageDataType;
import ct553.backend.product.entity.Product;
import ct553.backend.product.entity.ProductDetail;
import jakarta.validation.Valid;

// @CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    ProductService productService;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ImageDataService imageDataService;

    @GetMapping
    public ArrayList<Product> getAllProducts() {
        return this.productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Product product = this.productService.findProductById(id);
        if (product == null) {
            return new ResponseEntity<>("This product is not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@Valid @RequestBody Product product) {
        this.productService.addProduct(product);
        return new ResponseEntity<>(product, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updatedProductInfo) {
        Product existingProduct = this.productService.findProductById(id);
        if (existingProduct == null) {
            return new ResponseEntity<>("Can not find product to update", HttpStatus.NOT_FOUND);
        }

        Product updatedProduct = this.productService.updateProduct(id, updatedProductInfo);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }

    @PutMapping(value = "/{productId}/upload")
    public ResponseEntity<?> uploadProductImage(@PathVariable Long productId,
            @RequestParam("images") List<MultipartFile> files)
            throws IOException {
        StringBuilder imageUrls = new StringBuilder();

        for (MultipartFile multipartFile : files) {
            String imageUrl = this.cloudinaryService.uploadFile(multipartFile);

            imageUrls.append(imageUrl);

            if (multipartFile != files.get(files.size() - 1))
                imageUrls.append(", ");
        }

        ImageData data = new ImageData(null, imageUrls.toString(), ImageDataType.PRODUCT);
        data = this.imageDataService.addImageData(data);

        Product product = this.productService.updateProductImages(productId, data);

        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
        Product product = this.productService.findProductById(id);
        if (product == null) {
            return new ResponseEntity<>("Can not find product to delete", HttpStatus.NOT_FOUND);
        }

        this.productService.deleteProductById(id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }

    @GetMapping(value = "/details")
    public ArrayList<ProductDetail> getAllProductDetails() {
        return this.productService.getListProductDetails();
    }

    @GetMapping(value = "/{productId}/details")
    public ArrayList<ProductDetail> getAllProductDetails(@PathVariable Long productId) {
        return this.productService.getAllProductDetails(productId);
    }

    @GetMapping(value = "/{productId}/details/{id}")
    public ResponseEntity<?> getProductDetailById(@PathVariable Long id) {
        ProductDetail productDetail = this.productService.findProductDetailById(id);
        if (productDetail == null) {
            return new ResponseEntity<>("This product detail is not exist", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(productDetail, HttpStatus.OK);
    }

    @PostMapping(value = "/{productId}/details")
    public ResponseEntity<ProductDetail> addProductDetail(@PathVariable Long productId,
            @RequestBody ProductDetail productDetail) {
        this.productService.addProductDetail(productId, productDetail);
        return new ResponseEntity<>(productDetail, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}/details/{id}")
    public ResponseEntity<?> updateProductDetail(@PathVariable Long id,
            @RequestBody ProductDetail updatedProductDetailInfo) {
        ProductDetail existingProductDetail = this.productService.findProductDetailById(id);
        if (existingProductDetail == null) {
            return new ResponseEntity<>("Can not find product detail to update", HttpStatus.NOT_FOUND);
        }

        ProductDetail updatedProductDetail = this.productService.updateProductDetail(id, updatedProductDetailInfo);
        System.out.println(updatedProductDetail);
        return new ResponseEntity<>(updatedProductDetail, HttpStatus.OK);
    }

    @PutMapping(value = "/{productId}/details/{id}/upload")
    public ResponseEntity<?> upload(@PathVariable Long productId, @PathVariable Long id,
            @RequestParam("images") List<MultipartFile> files)
            throws IOException {
        StringBuilder imageUrls = new StringBuilder();

        for (MultipartFile multipartFile : files) {
            String imageUrl = this.cloudinaryService.uploadFile(multipartFile);

            imageUrls.append(imageUrl);

            if (multipartFile != files.get(files.size() - 1))
                imageUrls.append(", ");
        }

        ImageData imageData = new ImageData();
        imageData.setPath(imageUrls.toString());
        imageData.setType(ImageDataType.PRODUCT_DETAIL);

        ProductDetail detail = this.productService.updateProductDetailImageData(id, imageData);

        return new ResponseEntity<>(detail, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}/details/{id}")
    public ResponseEntity<String> deleteProductDetailById(@PathVariable Long id) {
        ProductDetail productDetail = this.productService.findProductDetailById(id);
        if (productDetail == null) {
            return new ResponseEntity<>("Can not find product detail to delete", HttpStatus.NOT_FOUND);
        }

        this.productService.deleteProductDetailById(id);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}