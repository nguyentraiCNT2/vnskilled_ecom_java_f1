package vnskilled.edu.ecom.Entity.Address;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.sql.Timestamp;
@Entity
@Table(name = "user_address")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserAddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userId;
    @Column(unique = true, nullable = false)
    private String address;
    @ManyToOne
    @JoinColumn(name = "country_id")
    private AddressCountryEntity countryId;
    @ManyToOne
    @JoinColumn(name = "city_id")
    private AddressCityEntity cityId;
    @ManyToOne
    @JoinColumn(name = "ward_id")
    private AddressWardsEntity wardId;
    @Column(unique = true, nullable = false)
    private Timestamp createdAt;
    @Column(unique = true, nullable = false)
    private Timestamp updatedAt;
    private String phone;

}
