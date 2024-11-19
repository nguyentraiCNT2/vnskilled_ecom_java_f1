package vnskilled.edu.ecom.Service.Impl.Review;

import com.google.api.gax.rpc.NotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ws.server.endpoint.adapter.DefaultMethodEndpointAdapter;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;
import vnskilled.edu.ecom.Entity.Review.ReviewEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.DTO.Review.ReviewDTO;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Repository.Product.ProductRepository;
import vnskilled.edu.ecom.Repository.Review.ReviewRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;
import vnskilled.edu.ecom.Service.Review.ReviewService;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ReviewServiceImpl implements ReviewService {
    private UserRepository userRepository;
    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ReviewServiceImpl(UserRepository userRepository, ReviewRepository reviewRepository, ProductRepository productRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<ReviewDTO> getByProductId(Long productId) {
        try {
            ProductEntity product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm."));
            List<ReviewDTO> list = new ArrayList<>();
            List<ReviewEntity> reviewEntities = reviewRepository.findByProductId(product);
            if (reviewEntities.size() == 0)
                throw new NoSuchElementException("Không có bình luận nào");

            for (ReviewEntity reviewEntity : reviewEntities) {
                ReviewDTO reviewDTO = modelMapper.map(reviewEntity, ReviewDTO.class);
                list.add(reviewDTO);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public ReviewDTO getById(Long id) {
        try {
            ReviewEntity entity = reviewRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Không tìm thấy bình luận "));
            ReviewDTO reviewDTO = modelMapper.map(entity, ReviewDTO.class);
            return reviewDTO;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void CreateReview(ReviewDTO reviewDTO) {
        try {

            ProductEntity product = productRepository.findById(reviewDTO.getProductId().getId()).orElseThrow(() -> new NoSuchElementException("Không tìm thấy sản phẩm"));
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(email);
            if (user == null)
                throw new RuntimeException("User Not Found");
            ReviewEntity entity = modelMapper.map(reviewDTO, ReviewEntity.class);
            entity.setUserId(user);
            entity.setProductId(product);
            entity.setCreateAt(Timestamp.from(Instant.now()));
            reviewRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void UpdateReview(ReviewDTO reviewDTO) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getName();
            UserEntity user = userRepository.findByEmail(email);
            if (user == null)
                throw new RuntimeException("User Not Found");
            ReviewEntity review = reviewRepository.findById(reviewDTO.getId()).orElseThrow(() -> new NoSuchElementException("Review Not Found"));
            if (review.getUserId().getId() == user.getId()) {
                review.setContent(reviewDTO.getContent());
                review.setCreateAt(Timestamp.from(Instant.now()));
                review.setRating(reviewDTO.getRating());
                reviewRepository.save(review);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void DeleteReview(Long id) {
        try {
            reviewRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
