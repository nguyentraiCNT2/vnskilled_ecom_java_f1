package vnskilled.edu.ecom.Entity.Vouchers;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
@Table(name = "vouchers")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class VouchersEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String code;
    @Column(columnDefinition = "text")
    private String description;
    @Column(name = "discount_type")
    private String discountType;
    @Column(name = "discount_value")
    private BigDecimal discountValue;
    @Column(name = "start_date")
    private Timestamp startDate;
    @Column(name = "end_date")
    private Timestamp endDate;
    @Column(name = "usage_limit")
    private int usageLimit;
    @Column(name = "use_count")
    private int useCount;
    private String status;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;

}
