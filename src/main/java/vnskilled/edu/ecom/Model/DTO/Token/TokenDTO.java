package vnskilled.edu.ecom.Model.DTO.Token;

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
public class TokenDTO {
    private Long id;
    private String token;
    private Timestamp createdAt;
    private Timestamp expiresAt;
    private UserDTO userId;
    private boolean revoked;

}
