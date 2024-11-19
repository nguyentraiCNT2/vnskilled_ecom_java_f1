package vnskilled.edu.ecom.Controller.Favorites;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Exception.ErrorResponse;
import vnskilled.edu.ecom.Service.Favorites.FavoriteService;
import vnskilled.edu.ecom.Model.Response.Favorites.FavoriteResponse;
import vnskilled.edu.ecom.Util.EndpointConstant.FavoriteApiPath;

import java.util.List;

@RestController
@RequestMapping(FavoriteApiPath.FAVORITES_BASE)
public class FavoritesController {

	private final FavoriteService favoriteService;

	@Autowired
	public FavoritesController(FavoriteService favoriteService) {
		this.favoriteService = favoriteService;
	}

	@GetMapping
	public ResponseEntity<?> getUserFavorites() {
		try {
			List<FavoriteResponse> favoriteResponses = favoriteService.getByUserId();
			return ResponseEntity.ok(favoriteResponses);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse (e.getMessage (), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Thêm sản phẩm vào danh sách yêu thích
	@PostMapping(FavoriteApiPath.FAVORITES_ADD)
	public ResponseEntity<?> addFavorite(@RequestParam Long productId) {
		try {
			favoriteService.addFavorite(productId);
			return ResponseEntity.ok("Product added to favorites successfully.");
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse (e.getMessage (), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// Xóa sản phẩm khỏi danh sách yêu thích
	@DeleteMapping(FavoriteApiPath.FAVORITES_DELETE)
	public ResponseEntity<?> deleteFavorite(@RequestParam Long favoriteId) {
		try {
			favoriteService.deleteFavorite(favoriteId);
			return ResponseEntity.ok("Favorite removed successfully.");
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse (e.getMessage (), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}