package ct553.backend.review;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import ct553.backend.imagedata.ImageData;
import ct553.backend.imagedata.ImageDataService;
import ct553.backend.imagedata.ImageDataType;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewResource {

    @Autowired
    ReviewService reviewService;

    @Autowired
    ImageDataService imageDataService;

    @GetMapping
    public ResponseEntity<ArrayList<Review>> getAllReviewsByProductId(@PathVariable Long productId,
            @RequestParam("all") boolean isGetAllProduct) {

        ArrayList<Review> reviews;

        if (isGetAllProduct) {
            reviews = this.reviewService.getAllReviews();
        } else
            reviews = this.reviewService.getAllReviewsByProductId(productId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<ArrayList<Review>> getAllReviewsByCustomerId(@PathVariable Long customerId) {
        ArrayList<Review> reviews = this.reviewService.getAllReviewsByCustomerId(customerId);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        Review review = this.reviewService.findReviewById(id);
        if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity<?> getReviewByOrderId(@PathVariable Long orderId) {
        ArrayList<Review> reviews = this.reviewService.findReviewByOrderId(orderId);
        if (reviews == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/order-details/{orderDetailId}")
    public ResponseEntity<?> getReviewByOrderDetailId(@PathVariable Long orderDetailId) {
        ArrayList<Review> review = this.reviewService.findReviewByOrderDetailId(orderDetailId);
        if (review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

    @PostMapping(value = "/order-details/{orderDetailId}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<Review> addReview(@PathVariable Long orderDetailId,
        @RequestPart(name = "review", required = true) Review review,
        @RequestPart(value = "images", required = false) List<MultipartFile> files) throws IOException {
            ImageData imageData = null;
        if (Objects.nonNull(files)) {
            imageData = this.imageDataService.buildImageData(files, ImageDataType.REVIEW);
            review.setImageData(imageData);
        }
        this.reviewService.addReview(orderDetailId, review);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReviewById(@PathVariable Long id) {
        Review Review = this.reviewService.findReviewById(id);
        if (Review == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        this.reviewService.deleteReviewById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "{id}/upload")
    public ResponseEntity<?> upload(@PathVariable Long id, @Nullable @RequestParam("images") List<MultipartFile> files) throws IOException
             {
        if (Objects.nonNull(files)) {
            ImageData imageData = this.imageDataService.buildImageData(files, ImageDataType.REVIEW);
            Review review = this.reviewService.updateReviewImages(id, imageData);
            return new ResponseEntity<>(review, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
