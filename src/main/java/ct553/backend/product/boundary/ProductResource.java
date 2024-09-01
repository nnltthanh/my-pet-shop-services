package ct553.backend.product.boundary;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ct553.backend.CloudinaryService;
import ct553.backend.imagedata.ImageDataService;
import ct553.backend.pet.PetBreed;
import ct553.backend.product.entity.ProductOverviewResponse;
import ct553.backend.product.entity.ProductSearchingCriteria;
import ct553.backend.product.entity.ProductSortingCriteria;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/products")
@Slf4j
public class ProductResource {

    @Autowired
    ProductService productService;

    @Autowired
    CloudinaryService cloudinaryService;

    @Autowired
    ImageDataService imageDataService;

    @GetMapping({ "/", "" })
    @ResponseStatus(value = HttpStatus.OK)
    public ProductOverviewResponse getAllProducts(
            @RequestParam(value = "updatedAt", required = false) Sort.Direction updatedAtOrder,
            @RequestParam(value = "alphabet", required = false) Sort.Direction alphabetOrder,
            @RequestParam(value = "rating", required = false) Sort.Direction ratingOrder,
            @RequestParam(value = "price", required = false) Sort.Direction priceOrder,
            @RequestParam(value = "priceFrom", required = false, defaultValue = "0") BigDecimal priceFrom,
            @RequestParam(value = "priceTo", required = false, defaultValue = "10000000000") BigDecimal priceTo,
            @RequestParam(value = "page", required = false, defaultValue = "0") int numberOfPage,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") int pageSize,
            @RequestParam(value = "breeds", required = false, defaultValue = "DOG,CAT,HAMSTER") List<PetBreed> breeds) {
        return this.productService.findProductOverviewResponseBy(
                new ProductSortingCriteria(updatedAtOrder, alphabetOrder, ratingOrder, priceOrder),
                new ProductSearchingCriteria(priceFrom, priceTo, breeds),
                PageRequest.of(numberOfPage, pageSize));
    }

    // @GetMapping("/{id}")
    // public ResponseEntity<?> getProductById(@PathVariable Long id) {
    // Product product = this.productService.findProductById(id);
    // if (product == null) {
    // return new ResponseEntity<>("This product is not exist",
    // HttpStatus.NOT_FOUND);
    // }
    // return new ResponseEntity<>(product, HttpStatus.OK);
    // }

    // @PostMapping
    // public ResponseEntity<Product> addProduct(@Valid @RequestBody Product
    // product) {
    // this.productService.addProduct(product);
    // return new ResponseEntity<>(product, HttpStatus.CREATED);
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody
    // Product updatedProductInfo) {
    // Product existingProduct = this.productService.findProductById(id);
    // if (existingProduct == null) {
    // return new ResponseEntity<>("Can not find product to update",
    // HttpStatus.NOT_FOUND);
    // }

    // Product updatedProduct = this.productService.updateProduct(id,
    // updatedProductInfo);
    // return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    // }

    // @PutMapping(value = "/{productId}/upload")
    // public ResponseEntity<?> uploadProductImage(@PathVariable Long productId,
    // @RequestParam("images") List<MultipartFile> files)
    // throws IOException {
    // StringBuilder imageUrls = new StringBuilder();

    // for (MultipartFile multipartFile : files) {
    // String imageUrl = this.cloudinaryService.uploadFile(multipartFile);

    // imageUrls.append(imageUrl);

    // if (multipartFile != files.get(files.size() - 1))
    // imageUrls.append(", ");
    // }

    // ImageData data = new ImageData(null, imageUrls.toString(),
    // ImageDataType.PRODUCT);
    // data = this.imageDataService.addImageData(data);

    // Product product = this.productService.updateProductImages(productId, data);

    // return new ResponseEntity<>(product, HttpStatus.OK);
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteProductById(@PathVariable Long id) {
    // Product product = this.productService.findProductById(id);
    // if (product == null) {
    // return new ResponseEntity<>("Can not find product to delete",
    // HttpStatus.NOT_FOUND);
    // }

    // this.productService.deleteProductById(id);
    // return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    // }

    // @GetMapping(value = "/details")
    // public ArrayList<ProductDetail> getAllProductDetails() {
    // return this.productService.getListProductDetails();
    // }

    // @GetMapping(value = "/{productId}/details")
    // public ArrayList<ProductDetail> getAllProductDetails(@PathVariable Long
    // productId) {
    // return this.productService.getAllProductDetails(productId);
    // }

    // @GetMapping(value = "/{productId}/details/{id}")
    // public ResponseEntity<?> getProductDetailById(@PathVariable Long id) {
    // ProductDetail productDetail = this.productService.findProductDetailById(id);
    // if (productDetail == null) {
    // return new ResponseEntity<>("This product detail is not exist",
    // HttpStatus.NOT_FOUND);
    // }
    // return new ResponseEntity<>(productDetail, HttpStatus.OK);
    // }

    // @PostMapping(value = "/{productId}/details")
    // public ResponseEntity<ProductDetail> addProductDetail(@PathVariable Long
    // productId,
    // @RequestBody ProductDetail productDetail) {
    // this.productService.addProductDetail(productId, productDetail);
    // return new ResponseEntity<>(productDetail, HttpStatus.CREATED);
    // }

    // @PutMapping("/{productId}/details/{id}")
    // public ResponseEntity<?> updateProductDetail(@PathVariable Long id,
    // @RequestBody ProductDetail updatedProductDetailInfo) {
    // ProductDetail existingProductDetail =
    // this.productService.findProductDetailById(id);
    // if (existingProductDetail == null) {
    // return new ResponseEntity<>("Can not find product detail to update",
    // HttpStatus.NOT_FOUND);
    // }

    // ProductDetail updatedProductDetail =
    // this.productService.updateProductDetail(id, updatedProductDetailInfo);
    // System.out.println(updatedProductDetail);
    // return new ResponseEntity<>(updatedProductDetail, HttpStatus.OK);
    // }

    // @PutMapping(value = "/{productId}/details/{id}/upload")
    // public ResponseEntity<?> upload(@PathVariable Long productId, @PathVariable
    // Long id,
    // @RequestParam("images") List<MultipartFile> files)
    // throws IOException {
    // StringBuilder imageUrls = new StringBuilder();

    // for (MultipartFile multipartFile : files) {
    // String imageUrl = this.cloudinaryService.uploadFile(multipartFile);

    // imageUrls.append(imageUrl);

    // if (multipartFile != files.get(files.size() - 1))
    // imageUrls.append(", ");
    // }

    // ImageData imageData = new ImageData();
    // imageData.setPath(imageUrls.toString());
    // imageData.setType(ImageDataType.PRODUCT_DETAIL);

    // ProductDetail detail = this.productService.updateProductDetailImageData(id,
    // imageData);

    // return new ResponseEntity<>(detail, HttpStatus.OK);
    // }

    // @DeleteMapping("/{productId}/details/{id}")
    // public ResponseEntity<String> deleteProductDetailById(@PathVariable Long id)
    // {
    // ProductDetail productDetail = this.productService.findProductDetailById(id);
    // if (productDetail == null) {
    // return new ResponseEntity<>("Can not find product detail to delete",
    // HttpStatus.NOT_FOUND);
    // }

    // this.productService.deleteProductDetailById(id);
    // return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    // }
}