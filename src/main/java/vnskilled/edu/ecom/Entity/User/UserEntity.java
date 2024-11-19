package vnskilled.edu.ecom.Entity.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import vnskilled.edu.ecom.Entity.Carts.ShoppingCartEntity;
import vnskilled.edu.ecom.Entity.Role.RoleEntity;
import vnskilled.edu.ecom.Entity.Address.UserAddressEntity;

import java.sql.Timestamp;
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String password;
    private String phone;
    private boolean active;
    @Column(name = "email_active")
    private boolean emailActive;
	@CreatedDate
    @Column(name = "created_at")
    private Timestamp createdAt;
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity roleId;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private UserAddressEntity addressId;
    private String language;
	@LastModifiedDate
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @Column(name = "avatar", columnDefinition = "text")
    private String avatar;
    @Column(name = "login_at")
    private Timestamp loginAt;
    @Column(name = "deleted_at")
    private Timestamp deletedAt;
	@OneToOne
	@JoinColumn(name = "cart_id")
	private ShoppingCartEntity cart;

}
