package ct553.backend.product;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
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
import ct553.backend.pet.healthrecord.HealthRecord;
import ct553.backend.product.pet.PetProduct;
import ct553.backend.product.productdetail.ProductDetailService;
import ct553.backend.review.Review;
import ct553.backend.review.ReviewRepository;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDetailService productDetailService;

    @Autowired
    private ReviewRepository reviewRepository;

    void addProduct(Product product) {
        product.setEngName(this.deAccent(product.getName()));
        this.productRepository.save(product);
    }

    public ProductOverviewResponse findProductOverviewResponseBy(ProductSortingCriteria sortingCriteria,
            ProductSearchingCriteria searchingCriteria, Pageable pageable) {
        if (searchingCriteria != null && searchingCriteria.getKeyword() == null) {
            searchingCriteria.setKeyword("%%");
        } else if (searchingCriteria != null && searchingCriteria.getKeyword() != null) {
            searchingCriteria.setKeyword("%" + searchingCriteria.getKeyword() + "%");
        }
        Page<Product> products = null;

        if (sortingCriteria.getDesc().indexOf("rating") > -1) {
            products = productRepository.findAllByAndRatingDesc(
                searchingCriteria,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), buildSortCriteria(sortingCriteria)));
        }
        else {
            products = productRepository.findAllBy(
                searchingCriteria,
                PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), buildSortCriteria(sortingCriteria)));
        }

        List<Product> data = new ArrayList<>(products.stream().map(p -> {
            ArrayList<Review> reviews = this.getAllReviewsByProductId(p.getId());
            p.setCountSold(this.productDetailService.countSoldByProductId(p.getId()));
            double rate = 0.0;
            for (Review review : reviews) {
                rate += review.getRate();
            }
            p.setRating(rate);
            p.setCountRating(getAllReviewsByProductId(p.getId()).size());
            // p.setRating(this.reviewRepository.getRatingByProductId(p.getId()));
            // p.setCountRating(this.reviewRepository.countByOrderDetail_ProductDetail_Product_Id(p.getId()));
            return p;
        }).toList());


        return ProductOverviewResponse.fromProducts(products.getTotalElements(), data);
    }

    public Product findProductById(Long id) {
        Product product = this.productRepository.findById(id).orElse(null);
        if (product instanceof PetProduct) {
            HealthRecord latestHealthRecord = ((PetProduct) product).getHealthRecord().stream()
                    .sorted(Comparator.comparing(HealthRecord::getCreatedAt).reversed()).toList().get(0);

            ((PetProduct) product).setLatestHealthRecord(latestHealthRecord);
        }
        if (product != null) {
            ArrayList<Review> reviews = this.getAllReviewsByProductId(product.getId());
            product.setProductDetails(this.productDetailService.getAllProductDetails(id));
            product.setCountSold(this.productDetailService.countSoldByProductId(product.getId()));
            // product.setRating(this.reviewRepository.getRatingByProductId(product.getId()));
            // product.setCountRating(this.reviewRepository.countByOrderDetail_ProductDetail_Product_Id(product.getId()));
            
            double rate = 0.0;
            for (Review review : reviews) {
                rate += review.getRate();
            }
            product.setRating(rate);
            product.setCountRating(getAllReviewsByProductId(id).size());
        }
        return product;
    }

    public ArrayList<Review> getAllReviewsByProductId(Long productId) {
        ArrayList<Review> reviewsDB = new ArrayList<>(
            this.reviewRepository.findByOrderDetail_ProductDetail_Product_Id(productId)
                                    .stream()
                                    .filter(r -> r.getEmployee() == null)
                                    .distinct()
                                    .sorted(Comparator.comparing(Review::getCreateDate).reversed())
                                    .toList()

        );
        reviewsDB = new ArrayList<>(removeDuplicate(reviewsDB)); 
        ArrayList<Review> reviewsReverse = new ArrayList<>(reviewsDB);
        Collections.reverse(reviewsReverse);
        return reviewsReverse;
    }

    private List<Review> removeDuplicate(List<Review> reviewsDB) {
        Map<Long, Review> newMap = new HashMap<>();

        for (Review review : reviewsDB) {
            newMap.put(review.getOrderDetail().getId(), review);
        }

        return new ArrayList<>(newMap.values()).stream()
            .sorted(Comparator.comparing(Review::getCreateDate))
            .toList();
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
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            Product productDB = product.get();
            productDB.setValidTo(new Date());
            this.productRepository.save(productDB);
        }
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
        sortingCriteria.getAsc().stream().forEach(value -> {
            if (!value.equals("rating")) {
                orders.add(Sort.Order.by(value).with(Direction.ASC));
            }
        });
        sortingCriteria.getDesc().stream().forEach(value -> {
            if (!value.equals("rating")) {
                orders.add(Sort.Order.by(value).with(Direction.DESC));
            }
        });
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