package vnskilled.edu.ecom.Model.DTO.Otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpDTO {
    private Long id;
    private String otpCode;
    private Timestamp createAt;
    private Timestamp updateAt;
    private String type;
    private int otpCount;
    private UserDTO userId;

}
