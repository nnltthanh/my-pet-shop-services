package ct553.backend.product.boundary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.imagedata.ImageData;
import ct553.backend.product.control.ProductDetailRepository;
import ct553.backend.product.control.ProductRepository;
import ct553.backend.product.entity.Product;
import ct553.backend.product.entity.ProductDetail;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    void addProduct(Product product) {
        this.productRepository.save(product);
    }

    ArrayList<Product> getAllProducts() {
        return (ArrayList<Product>) this.productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return this.productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Long id, Product productUpdateInfo) {
        Product existingProduct = this.productRepository.findById(id).orElse(null);

        if (existingProduct != null) {
            existingProduct.setName(productUpdateInfo.getName() != null ? productUpdateInfo.getName() : existingProduct.getName());
            existingProduct.setName(productUpdateInfo.getDescription() != null ? productUpdateInfo.getDescription() : existingProduct.getDescription());
            existingProduct.setImageData(productUpdateInfo.getImageData() != null ? productUpdateInfo.getImageData() : existingProduct.getImageData());
            this.productRepository.save(existingProduct);
            return existingProduct;
        } else {
            return null;
        }
    }

    Product updateProductImages(Long id, ImageData images) {
        Product product = this.findProductById(id);
        product.setImageData(images);
        return this.productRepository.save(product);
    }

    public List<Product> findProductByIds(Object ids) {

        List<String> listOfIds = new ArrayList<String>(Arrays.asList(ids.toString().split(", ")));
        List<Long> result = listOfIds.stream().map(Long::parseLong).collect(Collectors.toList());

        return this.productRepository.findAllById(result);
    }

    void deleteProductById(Long id) {
        this.productRepository.deleteById(id);
    }

    public void addProductDetail(Long productId, ProductDetail productDetail) {
        Product product = this.findProductById(productId);

        if (product != null) {
            productDetail.setProduct(product);
            this.productDetailRepository.save(productDetail);
        }
    }

    public ProductDetail updateProductDetail(Long id, ProductDetail productDetailUpdateInfo) {
        ProductDetail existingProductDetail = this.productDetailRepository.findById(id).orElse(null);

        if (Objects.nonNull(existingProductDetail)) {
            existingProductDetail.setUnit(productDetailUpdateInfo.getUnit() != null ? productDetailUpdateInfo.getUnit() : existingProductDetail.getUnit());
            existingProductDetail.setQuantity(productDetailUpdateInfo.getQuantity() > 0 ? productDetailUpdateInfo.getQuantity() : existingProductDetail.getQuantity());
            existingProductDetail.setSold(productDetailUpdateInfo.getSold() >= 0 ? productDetailUpdateInfo.getSold() : existingProductDetail.getSold());
            existingProductDetail.setImageData(productDetailUpdateInfo.getImageData() != null ? productDetailUpdateInfo.getImageData() : existingProductDetail.getImageData());

            this.productDetailRepository.save(existingProductDetail);
            return existingProductDetail;
        } else {
            return null;
        }
    }

    public ProductDetail updateProductDetail(ProductDetail productDetail) {
        return this.productDetailRepository.save(productDetail);
    }

    ArrayList<ProductDetail> getListProductDetails() {
        return (ArrayList<ProductDetail>) this.productDetailRepository.findAll();
    }

    ArrayList<ProductDetail> getAllProductDetails(Long productId) {
        return (ArrayList<ProductDetail>) this.productDetailRepository.findByProduct_Id(productId);
    }

    public ProductDetail findProductDetailById(Long id) {
        return this.productDetailRepository.findById(id).orElse(null);
    }

    public ArrayList<Product> findTop5MostSale() {
        ArrayList<Product> filteredProducts = this.getAllProducts();
        ArrayList<Product> recommendedProducts = new ArrayList<>();
        Map<Long, Integer> productSales = new HashMap<Long, Integer>();

        for (Product product : filteredProducts) {
            this.getAllProductDetails(product.getId())
                .stream()
                .forEach(productDetail -> 
                    productSales.put(
                        product.getId(), 
                        productSales.get(product.getId()) == null ?
                        Integer.valueOf(productDetail.getSold()) :
                        Integer.valueOf(productDetail.getSold()) + productSales.get(product.getId())
                    )
                );
        }
        productSales.entrySet()
            .stream()
            .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
            .limit(5)
            .forEach(entry -> {
                recommendedProducts.add(this.findProductById(entry.getKey()));
            });

        return recommendedProducts;
    }

    void deleteProductDetailById(Long id) {
        this.productDetailRepository.deleteById(id);
    }

    ProductDetail updateProductDetailImageData(Long id, ImageData imageData) {
        ProductDetail detail = this.findProductDetailById(id);
        detail.setImageData(imageData);
        return this.productDetailRepository.save(detail);
    }
}