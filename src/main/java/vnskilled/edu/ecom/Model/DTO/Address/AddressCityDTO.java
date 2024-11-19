package vnskilled.edu.ecom.Model.DTO.Address;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressCityDTO {
    private Long id;
    private String name;
    private AddressCountryDTO countryId;

}
