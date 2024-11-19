package vnskilled.edu.ecom.Repository.Product;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductSKURepository extends JpaRepository<ProductSKUEntity, Long> {
	@Query ("SELECT s FROM ProductSKUEntity s WHERE s.productId.id = :productId")
	List<ProductSKUEntity> findSKUsByProductId(@Param  ("productId") Long productId);
	// Hàm tìm SKU dựa trên productId, variantKey và value
	@Query("SELECT sku FROM ProductSKUEntity sku JOIN ProductVariantEntity variant ON sku.id = variant.variantId.id " +
			"WHERE sku.productId.id = :productId AND variant.variantKey = :variantKey AND variant.value = :value")
	Optional<ProductSKUEntity> findByProductIdAndVariantKeyAndValue(@Param("productId") Long productId,
	                                                                @Param("variantKey") String variantKey,
	                                                                @Param("value") String value);
}
