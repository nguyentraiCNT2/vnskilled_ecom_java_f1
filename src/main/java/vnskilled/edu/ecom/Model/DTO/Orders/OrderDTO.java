package vnskilled.edu.ecom.Model.DTO.Orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Model.DTO.Payment.PaymentDTO;
import vnskilled.edu.ecom.Model.DTO.Shipping.ShippingDTO;
import vnskilled.edu.ecom.Model.DTO.User.UserDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDTO {
    private Long id;
    private UserDTO userId;
    private BigDecimal totalPrice;
    private String address;
    private String country;
    private String city;
    private String ward;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private ShippingDTO shippingId;
    private PaymentDTO paymentId;
    private String phone;
	private int voucherId;

}
