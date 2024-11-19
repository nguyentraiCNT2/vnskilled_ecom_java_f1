package vnskilled.edu.ecom.Entity.Notification;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.sql.Timestamp;
@Entity
@Table(name = "notifications")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NotificantionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    private String title;
    private String content;
    @Column(name = "created_at")
    private Timestamp createAt;
    @Column(name = "recelve_at")
    private Timestamp recelveAt;

}
