package vnskilled.edu.ecom.Entity.Log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.sql.Timestamp;
@Entity
@Table(name = "logs")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    private String method;
    @Column(name = "api_url")
    private String apiUrl;
    private String status;
    @Column(name = "error_message")
    private String errorMessage;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "user_agent")
    private String userAgent;
    @Column(name = "host_name")
    private String hostName;
    @Column(name = "created_at")
    private Timestamp createAt;
    @Column(name = "updated_at")
    private Timestamp updateAt;

}
