package vnskilled.edu.ecom.Entity.Address;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address_wards")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressWardsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private AddressCityEntity cityId;

}
