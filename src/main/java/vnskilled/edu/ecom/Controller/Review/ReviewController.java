package vnskilled.edu.ecom.Controller.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Exception.ErrorResponse;
import vnskilled.edu.ecom.Model.DTO.Review.ReviewDTO;
import vnskilled.edu.ecom.Service.Review.ReviewService;
import vnskilled.edu.ecom.Util.EndpointConstant.ReviewApiPaths;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(ReviewApiPaths.BASE_PATH)
public class ReviewController {

	private final ReviewService reviewService;

	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@GetMapping(ReviewApiPaths.GET_REVIEWS_BY_PRODUCT_ID)
	public ResponseEntity<?> getReviewsByProductId(@PathVariable Long productId) {
		try {
			List<ReviewDTO> reviews = reviewService.getByProductId(productId);
			return new ResponseEntity<>(reviews, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping(ReviewApiPaths.GET_REVIEW_BY_ID)
	public ResponseEntity<?> getReviewById(@PathVariable Long id) {
		try {
			ReviewDTO reviewDTO = reviewService.getById(id);
			return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(ReviewApiPaths.CREATE_REVIEW)
	public ResponseEntity<?> createReview(@RequestBody ReviewDTO reviewDTO) {
		try {
			reviewService.CreateReview(reviewDTO);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (NoSuchElementException e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(ReviewApiPaths.UPDATE_REVIEW)
	public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewDTO reviewDTO) {
		try {
			reviewDTO.setId(id); // Đảm bảo đúng id cần cập nhật
			reviewService.UpdateReview(reviewDTO);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(ReviewApiPaths.DELETE_REVIEW)
	public ResponseEntity<?> deleteReview(@PathVariable Long id) {
		try {
			reviewService.DeleteReview(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (NoSuchElementException e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
