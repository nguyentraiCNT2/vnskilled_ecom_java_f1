package vnskilled.edu.ecom.Repository.ShoppingCart;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Carts.CartItemEntity;
import vnskilled.edu.ecom.Entity.Carts.ShoppingCartEntity;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;
import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItemEntity, Long> {
    CartItemEntity findByCartAndProductId(ShoppingCartEntity cart, ProductSKUEntity product);

	@Query("SELECT ci FROM CartItemEntity ci WHERE ci.cart.id = :cartId")
    List<CartItemEntity> findByCart(@Param("cartId") Long cartId);
	@Query("SELECT ci FROM CartItemEntity ci WHERE ci.productId.id = :productId")
	CartItemEntity findByProductId(@Param("productId") Long productId);
}
