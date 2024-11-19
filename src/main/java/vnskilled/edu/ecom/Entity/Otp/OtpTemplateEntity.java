package vnskilled.edu.ecom.Entity.Otp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@Entity
@Table(name = "otp_templates")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpTemplateEntity {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;
    private String subject;
    @Column(columnDefinition = "text")
    private String body;
    private String language;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
