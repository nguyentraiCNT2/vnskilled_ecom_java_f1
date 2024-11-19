package vnskilled.edu.ecom.Model.Response.Favorites;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.Favorites.FavoritesEntity;
import vnskilled.edu.ecom.Model.DTO.Favorites.FavoritesDTO;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteResponse {
	private FavoritesDTO favorites;
	private ProductResponse product;

}
