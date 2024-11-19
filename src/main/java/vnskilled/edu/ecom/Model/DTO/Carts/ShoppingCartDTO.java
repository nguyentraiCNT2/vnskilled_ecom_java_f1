package vnskilled.edu.ecom.Model.DTO.Carts;

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
public class ShoppingCartDTO {
    private Long id;
    private UserDTO user;
	@JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp createdAt;
	@JsonFormat (pattern = "dd-MM-yyyy HH:mm:ss")
    private Timestamp updatedAt;

}
