package vnskilled.edu.ecom.Entity.Categories;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.sql.Timestamp;
@Entity
@Table(name = "categories_descriptions")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class CategoriesDescriptionsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @JoinColumn(name = "created_by")
    private UserEntity createdBy;
    private String language;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private CategoriesEntity categoryId;

}
