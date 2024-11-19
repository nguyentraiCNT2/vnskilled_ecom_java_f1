package vnskilled.edu.ecom.Model.Response.Cart;

import vnskilled.edu.ecom.Model.DTO.Carts.CartItemDTO;
import vnskilled.edu.ecom.Model.DTO.Carts.ShoppingCartDTO;
import vnskilled.edu.ecom.Model.Response.Product.ProductResponse;

public class CartItemResponse {
    private CartItemDTO item;
    private ProductResponse productResponse;

    public CartItemDTO getItem() {
        return item;
    }

    public void setItem(CartItemDTO item) {
        this.item = item;
    }


    public ProductResponse getProductResponse() {
        return productResponse;
    }

    public void setProductResponse(ProductResponse productResponse) {
        this.productResponse = productResponse;
    }
}
