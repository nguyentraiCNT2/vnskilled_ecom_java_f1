package vnskilled.edu.ecom.Entity.Address;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address_citys")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AddressCityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private AddressCountryEntity countryId;

}
