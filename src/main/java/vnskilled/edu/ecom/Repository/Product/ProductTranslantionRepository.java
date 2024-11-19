package vnskilled.edu.ecom.Repository.Product;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;
import vnskilled.edu.ecom.Entity.Products.ProductTranslationEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTranslantionRepository extends JpaRepository<ProductTranslationEntity, Long> {
	List<ProductTranslationEntity> findByProductId(ProductEntity productId);
	List<ProductTranslationEntity> findByNameLike(String name);
	@Query ("SELECT t FROM ProductTranslationEntity t WHERE t.productId.id = :productId AND t.language = :language")
	Optional<ProductTranslationEntity> findTranslationByProductIdAndLanguage(@Param("productId") Long productId, @Param  ("language") String language);
	Optional<ProductTranslationEntity> findByProductIdAndLanguage(ProductEntity productId, String language);
}
