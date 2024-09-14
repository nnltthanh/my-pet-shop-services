package ct553.backend.product.boundary;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import ct553.backend.imagedata.ImageData;
import ct553.backend.product.control.ProductRepository;
import ct553.backend.product.entity.Product;
import ct553.backend.product.entity.ProductOverviewResponse;
import ct553.backend.product.entity.ProductSearchingCriteria;
import ct553.backend.product.entity.ProductSortingCriteria;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    void addProduct(Product product) {
        product.setEngName(this.deAccent(product.getName()));
        this.productRepository.save(product);
    }

    public ProductOverviewResponse findProductOverviewResponseBy(ProductSortingCriteria sortingCriteria,
            ProductSearchingCriteria searchingCriteria, Pageable pageable) {
        Page<Product> products = productRepository.findAllBy(
                searchingCriteria,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), buildSortCriteria(sortingCriteria)));
        List<Product> data = new ArrayList<>(products.stream().toList());

        return ProductOverviewResponse.fromProducts(products.getTotalElements(), data);
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

    Product updateProductImages(Long id, ImageData imageData) {
        Product product = this.findProductById(id);
        product.setImageData(imageData);
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

    // public ArrayList<Product> findTop5MostSale() {
    // ArrayList<Product> filteredProducts = this.findAllBy();
    // ArrayList<Product> recommendedProducts = new ArrayList<>();
    // Map<Long, Integer> productSales = new HashMap<Long, Integer>();

    // for (Product product : filteredProducts) {
    // this.getAllProductDetails(product.getId())
    // .stream()
    // .forEach(productDetail -> productSales.put(
    // product.getId(),
    // productSales.get(product.getId()) == null ?
    // Integer.valueOf(productDetail.getSold())
    // : Integer.valueOf(productDetail.getSold()) +
    // productSales.get(product.getId())));
    // }
    // productSales.entrySet()
    // .stream()
    // .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
    // .limit(5)
    // .forEach(entry -> {
    // recommendedProducts.add(this.findProductById(entry.getKey()));
    // });

    // return recommendedProducts;
    // }

    private Sort buildSortCriteria(ProductSortingCriteria sortingCriteria) {
        if (Objects.isNull(sortingCriteria) || sortingCriteria.isEmptySortingCriteria()) {
            return Sort.by(Direction.DESC, "updatedAt");
        }
        List<Sort.Order> orders = new ArrayList<>();
        sortingCriteria.getAsc().stream().forEach(value -> orders.add(Sort.Order.by(value).with(Direction.ASC)));
        sortingCriteria.getDesc().stream().forEach(value -> orders.add(Sort.Order.by(value).with(Direction.DESC)));
        return CollectionUtils.isEmpty(orders) ? Sort.unsorted() : Sort.by(orders);
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