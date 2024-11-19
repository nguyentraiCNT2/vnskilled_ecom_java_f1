package vnskilled.edu.ecom.Controller.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vnskilled.edu.ecom.Exception.ErrorResponse;
import vnskilled.edu.ecom.Model.Request.Cart.CartRequest;
import vnskilled.edu.ecom.Model.Response.Cart.CartItemResponse;
import vnskilled.edu.ecom.Service.ShoppingCart.ShoppingCartService;
import vnskilled.edu.ecom.Util.EndpointConstant.CartApiPaths;

import java.util.List;

@RestController
@RequestMapping(CartApiPaths.BASE_PATH)
public class ShoppingCartController {
	@Autowired
	private ShoppingCartService shoppingCartService;

	@GetMapping(CartApiPaths.GET_BY_USER)
	public ResponseEntity<?> getByUser() {
		try {
			List<CartItemResponse> cartItemResponseList = shoppingCartService.getCartItemByUser();
			return new ResponseEntity<>(cartItemResponseList, HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(CartApiPaths.ADD_TO_CART)
	public ResponseEntity<?> addToCart(@RequestBody CartRequest cartRequest) {
		try {
			shoppingCartService.addToCart(cartRequest);
			return new ResponseEntity<>("Thêm sản phẩm vào giỏ hàng thành công", HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping(CartApiPaths.UPDATE_QUANTITY)
	public ResponseEntity<?> updateQuantity(@PathVariable Long id, int quantity) {
		try {
			shoppingCartService.updateCartQuantity(id, quantity);
			return new ResponseEntity<>("Cập nhật giỏ hàng thành công", HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(CartApiPaths.DELETE_CART)
	public ResponseEntity<?> deleteCart(@PathVariable Long id) {
		try {
			shoppingCartService.delete(id);
			return new ResponseEntity<>("Delete successfully", HttpStatus.OK);
		} catch (Exception e) {
			return ErrorResponse.buildErrorResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


}
