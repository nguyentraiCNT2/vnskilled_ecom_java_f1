package vnskilled.edu.ecom.Entity.Carts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.Products.ProductEntity;
import vnskilled.edu.ecom.Entity.Products.ProductSKUEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
@Table(name = "crart_item")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_sku_id")
    private ProductSKUEntity productId;
    private int quantity;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
	@ManyToOne
	@JoinColumn(name = "cart_id")
	private ShoppingCartEntity cart;
    private BigDecimal price;
	private boolean selected;


}
