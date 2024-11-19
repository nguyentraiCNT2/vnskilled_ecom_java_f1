package vnskilled.edu.ecom.Repository.Product;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Products.ProductVariantEntity;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {
	@Query ("SELECT v FROM ProductVariantEntity v WHERE v.variantId.id = :skuId")
	List<ProductVariantEntity> findVariantsBySKUId(@Param  ("skuId") Long skuId);
}
