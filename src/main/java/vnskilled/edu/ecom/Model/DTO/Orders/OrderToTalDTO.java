package vnskilled.edu.ecom.Model.DTO.Orders;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderToTalDTO {
	private Long id;
	private OrderDTO orderId;
	private BigDecimal value;
	private String title;
	private Timestamp createdAt;
	private Timestamp updatedAt;

}
