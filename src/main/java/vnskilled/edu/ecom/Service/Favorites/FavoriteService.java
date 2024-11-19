package vnskilled.edu.ecom.Service.Favorites;

import vnskilled.edu.ecom.Model.Response.Favorites.FavoriteResponse;

import java.util.List;

public interface FavoriteService {
	List<FavoriteResponse> getByUserId();
	void addFavorite(Long productId);
	void deleteFavorite(Long id);
}
