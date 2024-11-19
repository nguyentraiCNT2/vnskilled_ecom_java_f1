package vnskilled.edu.ecom.Model.Request.Auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ResetPasswordRequest {
    private String email;
    private String otp;
    private String newPassword;
}
