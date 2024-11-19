package vnskilled.edu.ecom.Service.Review;

import vnskilled.edu.ecom.Model.DTO.Review.ReviewDTO;

import java.util.List;

public interface ReviewService {
	List<ReviewDTO> getByProductId(Long productId);
	ReviewDTO getById(Long id);
	void CreateReview(ReviewDTO reviewDTO);
	void UpdateReview(ReviewDTO reviewDTO);
	void DeleteReview(Long id);

}
