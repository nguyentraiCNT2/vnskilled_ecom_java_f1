package vnskilled.edu.ecom.Model.Request.Otp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OtpRequest {
	private String email;
	private String otp;

}
