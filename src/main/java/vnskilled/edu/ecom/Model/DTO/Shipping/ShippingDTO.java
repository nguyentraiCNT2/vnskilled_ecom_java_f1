package vnskilled.edu.ecom.Model.DTO.Shipping;

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
public class ShippingDTO {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
