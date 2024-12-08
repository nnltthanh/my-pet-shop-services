package ct553.backend.review;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ct553.backend.imagedata.ImageData;
import ct553.backend.order.boundary.OrderService;
import ct553.backend.order.entity.OrderDetail;
import ct553.backend.user.User;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    OrderService orderService;

    void addReview(Long orderDetailId, Review review) {
        OrderDetail orderDetail = this.orderService.findOrderDetailById(orderDetailId);
        review.setOrderDetail(orderDetail);
        User customer = orderDetail.getOrder().getCustomer();
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

    public Long countAllReviewsByProductId(Long productId) {
        return this.reviewRepository.countByOrderDetail_ProductDetail_Product_Id(productId);
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
