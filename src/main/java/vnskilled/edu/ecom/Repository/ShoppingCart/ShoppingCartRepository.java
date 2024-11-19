package vnskilled.edu.ecom.Repository.ShoppingCart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import vnskilled.edu.ecom.Entity.Carts.ShoppingCartEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.util.List;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCartEntity, Long> {
    @Query("select c from ShoppingCartEntity c"+
            " join CartItemEntity ci on ci.cart.id = c.id "+
    "where ci.productId.id = :productid")
    ShoppingCartEntity findByUserIdAndProductId(@Param("productid") Long productId);

}
