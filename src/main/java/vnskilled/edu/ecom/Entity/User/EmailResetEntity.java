package vnskilled.edu.ecom.Entity.User;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.Otp.OtpEntity;

import java.sql.Timestamp;

@Entity
@Table(name = "email_resets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmailResetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String email;
    @ManyToOne
    @JoinColumn(name = "otp_id")
    private OtpEntity otpId;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    @Column(unique = true, nullable = false,name = "created_at")
    private Timestamp createdAt;

}
