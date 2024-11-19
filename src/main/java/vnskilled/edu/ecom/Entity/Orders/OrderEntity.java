package vnskilled.edu.ecom.Entity.Orders;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.Payment.PaymentEntity;
import vnskilled.edu.ecom.Entity.Shipping.ShippingEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    private String address;
    private String country;
    private String city;
    private String ward;
    private String status;
    @Column(name = "created_at")
    private Timestamp createdAt;
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @ManyToOne
    @JoinColumn(name = "shipping_id")
    private ShippingEntity shippingId;
    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentId;
    private String phone;
	@Column(name = "voucher_id")
	private int voucherId;

}
