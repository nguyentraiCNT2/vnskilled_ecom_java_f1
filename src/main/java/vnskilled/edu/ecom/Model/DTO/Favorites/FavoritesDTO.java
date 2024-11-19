package vnskilled.edu.ecom.Model.DTO.Favorites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FavoritesDTO {
    private Long id;
    private UserDTO userId;
    private ProductDTO productId;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
