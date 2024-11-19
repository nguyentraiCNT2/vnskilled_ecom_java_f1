package vnskilled.edu.ecom.Model.DTO.Address;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class UserAddressDTO {
    private Long id;
    private UserDTO userId;
    private String address;
    private AddressCountryDTO countryId;
    private AddressCityDTO cityId;
    private AddressWardsDTO wardId;
	@JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp createdAt;
	@JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp updatedAt;
    private String phone;

}
