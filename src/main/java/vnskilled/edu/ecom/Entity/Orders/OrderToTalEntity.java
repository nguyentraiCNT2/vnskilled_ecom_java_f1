package vnskilled.edu.ecom.Entity.Orders;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Entity
@Table(name = "order_totals")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderToTalEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@ManyToOne
	@JoinColumn(name = "order_id")
	private OrderEntity orderId;
	private BigDecimal value;
	private String title;
	@Column(name = "created_at")
	private Timestamp createdAt;
	@Column(name = "updated_at")
	private Timestamp updatedAt;

}
