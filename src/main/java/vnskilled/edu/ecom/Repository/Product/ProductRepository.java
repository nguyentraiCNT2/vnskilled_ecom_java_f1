package vnskilled.edu.ecom.Repository.Product;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
	@Query ("SELECT p FROM ProductEntity p JOIN ProductTranslationEntity pt ON p.id = pt.productId.id WHERE pt.name LIKE %:productName%")
	List<ProductEntity> findByNameContaining(@Param  ("productName") String productName, Pageable pageable);
	@Query("SELECT p FROM ProductEntity p " +
			"LEFT JOIN ProductTranslationEntity t ON t.productId.id = p.id " +
			"WHERE t.language = :language")
	List<ProductEntity> findAllWithTranslations(@Param("language") String language);

	@Query("SELECT p FROM ProductEntity p " +
			"LEFT JOIN ProductTranslationEntity t ON t.productId.id = p.id " +
			"WHERE t.language = :language AND p.id = :id ")
	ProductEntity findByIdWithTranslations(@Param("language") String language, @Param ("id") Long id);


	@Query("SELECT p FROM ProductEntity p " +
			"LEFT JOIN ProductTranslationEntity t ON t.productId.id = p.id " +
			"WHERE t.language = :language AND p.newProduct = true ")
	List<ProductEntity>  findByNewProductWithTranslations(@Param("language") String language);


	// Thêm Pageable vào phương thức
	@Query("SELECT p FROM ProductEntity p " +
			"JOIN ProductTranslationEntity t ON t.productId.id = p.id " +
			"WHERE t.language = :language AND p.hotProduct = true")
	List<ProductEntity> findHotProducts(@Param("language") String language, Pageable pageable);

	@Query("select p from ProductEntity p join ProductTranslationEntity tr on p.id = tr.productId.id where tr.name = :name and p.categoryId.id =:categoryId")
	Optional<ProductEntity> findByNameAndCategoryId(@Param ("name") String name,@Param ("categoryId") Long categoryId);
}
