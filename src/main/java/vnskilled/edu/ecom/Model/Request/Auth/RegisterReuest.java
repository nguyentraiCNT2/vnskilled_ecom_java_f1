package vnskilled.edu.ecom.Model.Request.Auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterReuest {
	private String email;
	private String password;
	private String firstName;
	private String lastName;

}
