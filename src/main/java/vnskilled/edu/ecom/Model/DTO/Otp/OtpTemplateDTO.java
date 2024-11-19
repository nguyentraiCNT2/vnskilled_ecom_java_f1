package vnskilled.edu.ecom.Model.DTO.Otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpTemplateDTO {
    private Long id;
    private String type;
    private String subject;
    private String body;
    private String language;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
