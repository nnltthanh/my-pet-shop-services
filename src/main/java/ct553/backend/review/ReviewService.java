package ct553.backend.review;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.customer.Customer;
import ct553.backend.imagedata.ImageData;
import ct553.backend.order.boundary.OrderService;
import ct553.backend.order.entity.OrderDetail;
import ct553.backend.product.boundary.ProductService;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    ProductService productService;

    @Autowired
    OrderService orderService;

    void addReview(Long orderDetailId, Review review) {
        OrderDetail orderDetail = this.orderService.findOrderDetailById(orderDetailId);
        review.setOrderDetail(orderDetail);
        Customer customer = orderDetail.getOrder().getCustomer();
        review.setCustomer(customer);
        this.reviewRepository.save(review);
    }

    ArrayList<Review> getAllReviews() {
        ArrayList<Review> reviewsDB = (ArrayList<Review>) this.reviewRepository.findAll();
        ArrayList<Review> reviewsReverse = new ArrayList<>(reviewsDB);
        Collections.reverse(reviewsReverse);
        return reviewsReverse;
    }

    ArrayList<Review> getAllReviewsByCustomerId(Long customerId) {
        ArrayList<Review> reviewsDB = this.reviewRepository.findByCustomer_Id(customerId);
        ArrayList<Review> reviewsReverse = new ArrayList<>(reviewsDB);
        Collections.reverse(reviewsReverse);
        return reviewsReverse;
    }

    ArrayList<Review> getAllReviewsByProductId(Long productId) {
        ArrayList<Review> reviewsDB = this.reviewRepository.findByOrderDetail_ProductDetail_Product_Id(productId);
        ArrayList<Review> reviewsReverse = new ArrayList<>(reviewsDB);
        Collections.reverse(reviewsReverse);
        return reviewsReverse;
    }

    public Review findReviewById(Long id) {
        return this.reviewRepository.findById(id).orElse(null);
    }

    public ArrayList<Review> findReviewByOrderDetailId(Long id) {
        return this.reviewRepository.findByOrderDetail_Id(id);
    }

    public ArrayList<Review> findReviewByOrderId(Long id) {
        return this.reviewRepository.findByOrderDetail_Order_Id(id);
    }

    void deleteReviewById(Long id) {
        this.reviewRepository.deleteById(id);
    }

    Review updateReviewImages(Long id, ImageData imageData) {
        Review review = this.findReviewById(id);
        review.setImageData(imageData);
        return this.reviewRepository.save(review);
    }

}
