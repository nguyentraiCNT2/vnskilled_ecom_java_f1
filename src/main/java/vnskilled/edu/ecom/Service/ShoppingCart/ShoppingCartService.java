package vnskilled.edu.ecom.Service.ShoppingCart;

import vnskilled.edu.ecom.Model.DTO.Carts.ShoppingCartDTO;
import vnskilled.edu.ecom.Model.Request.Cart.CartRequest;
import vnskilled.edu.ecom.Model.Response.Cart.CartItemResponse;

import java.util.List;

public interface ShoppingCartService {
    List<CartItemResponse> getCartItemByUser();
    void addToCart(CartRequest request);
    void updateCartQuantity(Long id, int quantity);
    void delete(Long id);
}
