package vnskilled.edu.ecom.Model.DTO.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.Products.ProductDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailDTO {
    private Long id;
    private OrderDTO orderId;
    private ProductDTO productId;
    private int quantity;
    private BigDecimal price;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
