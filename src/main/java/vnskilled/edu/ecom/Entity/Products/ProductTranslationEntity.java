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
@Table(name = "product_translations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ProductTranslationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String language;
    private String name;
    @Column(columnDefinition = "text")
    private String description;
    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;
	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity productId;

}
