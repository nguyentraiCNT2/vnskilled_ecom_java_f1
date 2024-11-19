package vnskilled.edu.ecom.Entity.Products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vnskilled.edu.ecom.Entity.Categories.CategoriesEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.sql.Timestamp;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean published;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoriesEntity categoryId;
    @ManyToOne
    @JoinColumn(name = "created_by")
    private UserEntity createBy;
    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updateBy;
    @CreatedDate
    @Column(name = "created_at")
    private Timestamp createAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updateAt;
    @Column(name = "host_product")
    private boolean hotProduct;
    @Column(name = "new_product")
    private boolean newProduct;
	@Column(columnDefinition = "text")
    private String image;
	@Column(columnDefinition = "text")
    private String video;
	@Column(name = "deleted_at")
	private Timestamp deletedAt;
    private double rating;


}
