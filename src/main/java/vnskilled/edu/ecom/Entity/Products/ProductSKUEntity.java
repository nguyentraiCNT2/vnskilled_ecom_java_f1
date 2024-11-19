package vnskilled.edu.ecom.Entity.Products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
@Table(name = "product_skus")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ProductSKUEntity {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	@CreatedDate
	@Column (name = "create_at")
	private Timestamp createdAt;
	@LastModifiedDate
	@Column(name = "updated_at")
	private Timestamp updatedAt;
	@ManyToOne
	@JoinColumn(name = "created_by")
	private UserEntity createBy;
	@ManyToOne
	@JoinColumn(name = "updated_by")
	private UserEntity updateBy;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity productId;
	@Column(columnDefinition = "text")
	private String image;
	private Long quantity;
	private BigDecimal price;

}
