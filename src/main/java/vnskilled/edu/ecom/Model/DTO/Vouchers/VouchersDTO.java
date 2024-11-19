package vnskilled.edu.ecom.Model.DTO.Vouchers;

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
public class VouchersDTO {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String discountType;
    private BigDecimal discountValue;
    private Timestamp startDate;
    private Timestamp endDate;
    private int usageLimit;
    private int useCount;
    private String status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
}
