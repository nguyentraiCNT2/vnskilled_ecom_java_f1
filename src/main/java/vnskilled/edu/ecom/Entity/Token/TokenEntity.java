package vnskilled.edu.ecom.Entity.Token;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.sql.Timestamp;
@Entity
@Table(name = "tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "expires_at")
    private Timestamp expiresAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    private boolean revoked;

}
