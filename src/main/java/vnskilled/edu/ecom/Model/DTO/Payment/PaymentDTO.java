package vnskilled.edu.ecom.Model.DTO.Payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PaymentDTO {
    private Long id;
    private String name;
    private String description;
    private Timestamp createdAt;
    private Timestamp updatedAt;

}
