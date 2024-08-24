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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ct553.backend.imagedata.ImageData;
import ct553.backend.product.control.ProductDetailRepository;
import ct553.backend.product.control.ProductRepository;
import ct553.backend.product.entity.Product;
import ct553.backend.product.entity.ProductDetail;
import ct553.backend.product.entity.ProductOverviewResponse;
import ct553.backend.product.entity.ProductSearchingCriteria;
import ct553.backend.product.entity.ProductSortingCriteria;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    void addProduct(Product product) {
        product.setEngName(this.deAccent(product.getName()));
        this.productRepository.save(product);
    }

    public ArrayList<Product> findAllBy(ProductSortingCriteria sortingCriteria, Pageable pageable) {
        return new ArrayList<>(productRepository.findAll(
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), buildSortCriteria(sortingCriteria)))
                .stream().toList());
    }

    public ProductOverviewResponse findProductOverviewResponseBy(ProductSortingCriteria sortingCriteria,
            ProductSearchingCriteria searchingCriteria, Pageable pageable) {
        List<Product> data = new ArrayList<>(productRepository.findAllByPriceBetween(
                searchingCriteria.getPriceFrom(), searchingCriteria.getPriceTo(),
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), buildSortCriteria(sortingCriteria)))
                .stream().toList());
        Long total = this.productRepository.countAllByPriceBetween(searchingCriteria.getPriceFrom(), searchingCriteria.getPriceTo());
        return new ProductOverviewResponse(total, data);
    }

    public ArrayList<Product> findAllBy() {
        return new ArrayList<>(productRepository.findAll());
    }

    public Product findProductById(Long id) {
        return this.productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Long id, Product productUpdateInfo) {
        Product existingProduct = this.productRepository.findById(id).orElse(null);

        if (existingProduct != null) {
            existingProduct.setName(
                    productUpdateInfo.getName() != null ? productUpdateInfo.getName() : existingProduct.getName());
            existingProduct.setName(productUpdateInfo.getDescription() != null ? productUpdateInfo.getDescription()
                    : existingProduct.getDescription());
            existingProduct.setImageData(productUpdateInfo.getImageData() != null ? productUpdateInfo.getImageData()
                    : existingProduct.getImageData());
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
            existingProductDetail.setUnit(productDetailUpdateInfo.getUnit() != null ? productDetailUpdateInfo.getUnit()
                    : existingProductDetail.getUnit());
            existingProductDetail
                    .setQuantity(productDetailUpdateInfo.getQuantity() > 0 ? productDetailUpdateInfo.getQuantity()
                            : existingProductDetail.getQuantity());
            existingProductDetail.setSold(productDetailUpdateInfo.getSold() >= 0 ? productDetailUpdateInfo.getSold()
                    : existingProductDetail.getSold());
            existingProductDetail.setImageData(
                    productDetailUpdateInfo.getImageData() != null ? productDetailUpdateInfo.getImageData()
                            : existingProductDetail.getImageData());

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
        ArrayList<Product> filteredProducts = this.findAllBy();
        ArrayList<Product> recommendedProducts = new ArrayList<>();
        Map<Long, Integer> productSales = new HashMap<Long, Integer>();

        for (Product product : filteredProducts) {
            this.getAllProductDetails(product.getId())
                    .stream()
                    .forEach(productDetail -> productSales.put(
                            product.getId(),
                            productSales.get(product.getId()) == null ? Integer.valueOf(productDetail.getSold())
                                    : Integer.valueOf(productDetail.getSold()) + productSales.get(product.getId())));
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

    private Sort buildSortCriteria(ProductSortingCriteria sortingCriteria) {
        if (Objects.isNull(sortingCriteria) || sortingCriteria.isEmptySortingCriteria()) {
            return Sort.by(Direction.DESC, "updatedAt");
        }
        if (sortingCriteria.getUpdatedAt() != null) {
            return Sort.by(Sort.Order.by("updatedAt").with(sortingCriteria.getUpdatedAt()));
        }

        if (sortingCriteria.getPrice() != null) {
            return Sort.by(Sort.Order.by("price").with(sortingCriteria.getPrice()));
        }

        if (sortingCriteria.getPrice() != null) {
            return Sort.by(Sort.Order.by("rating").with(sortingCriteria.getRating()));
        }
        return Sort.unsorted();
    }

    private String deAccent(String text) {
        text = text.replaceAll("à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ", "a");
        text = text.replaceAll("è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ", "e");
        text = text.replaceAll("ì|í|ị|ỉ|ĩ", "i");
        text = text.replaceAll("ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ", "o");
        text = text.replaceAll("ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ", "u");
        text = text.replaceAll("ỳ|ý|ỵ|ỷ|ỹ", "y");
        text = text.replaceAll("đ", "d");

        text = text.replaceAll("À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ", "A");
        text = text.replaceAll("È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ", "E");
        text = text.replaceAll("Ì|Í|Ị|Ỉ|Ĩ", "I");
        text = text.replaceAll("Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ", "O");
        text = text.replaceAll("Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ", "U");
        text = text.replaceAll("Ỳ|Ý|Ỵ|Ỷ|Ỹ", "Y");
        text = text.replaceAll("Đ", "D");
        return text;
    }
}