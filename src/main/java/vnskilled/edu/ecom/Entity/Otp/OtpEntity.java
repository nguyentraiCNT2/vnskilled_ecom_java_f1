package vnskilled.edu.ecom.Entity.Otp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.sql.Timestamp;
@Entity
@Table(name = "otps")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String otpCode;
    @Column(name = "creared_at")
    private Timestamp createAt;
    @Column(name = "updated_at")
    private Timestamp updateAt;
    private String type;
    private int otpCount;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;

}
