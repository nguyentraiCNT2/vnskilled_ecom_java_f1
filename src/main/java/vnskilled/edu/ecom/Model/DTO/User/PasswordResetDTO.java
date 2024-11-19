package vnskilled.edu.ecom.Model.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.Otp.OtpDTO;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PasswordResetDTO {
    private Long id;
    private String password;
    private UserDTO userId;
    private OtpDTO otpId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
