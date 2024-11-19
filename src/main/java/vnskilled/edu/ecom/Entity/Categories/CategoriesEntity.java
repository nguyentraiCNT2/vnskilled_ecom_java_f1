package vnskilled.edu.ecom.Entity.Categories;

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
@Table (name = "categories")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class CategoriesEntity {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	@Column (name = "parent_id")
	private Long parentId;
	@Column (columnDefinition = "text")
	private String image;
	@CreatedDate
	@Column (name = "created_at")
	private Timestamp createdAt;
	@LastModifiedDate
	@Column (name = "updated_at")
	private Timestamp updatedAt;
	@Column (name = "deleted_at")
	private Timestamp deletedAt;
}
