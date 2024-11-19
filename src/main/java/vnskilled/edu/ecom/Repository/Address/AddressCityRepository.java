package vnskilled.edu.ecom.Repository.Address;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Address.AddressCityEntity;
import vnskilled.edu.ecom.Entity.Address.AddressCountryEntity;

import java.util.List;
@Repository
public interface AddressCityRepository extends JpaRepository<AddressCityEntity, Long> {
	boolean existsByName(String name);
	List<AddressCityEntity> findByCountryId(AddressCountryEntity country);


}
