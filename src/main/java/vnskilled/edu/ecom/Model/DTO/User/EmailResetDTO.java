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
public class EmailResetDTO {
    private Long id;
    private String email;
    private OtpDTO otpId;
    private UserDTO userId;
    private Timestamp createdAt;

}
