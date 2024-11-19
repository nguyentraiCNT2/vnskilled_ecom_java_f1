package vnskilled.edu.ecom.Entity.Products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
@Entity
@Table(name = "product_variants")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @ManyToOne
    @JoinColumn(name = "sku_id")
    private ProductSKUEntity variantId;
    private String language;
	@Column(name = "variant_key") // Đổi tên cột từ "key" thành "variant_key"
	private String variantKey;
	private String value;
	private String lang;

}

