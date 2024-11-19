package vnskilled.edu.ecom.Repository.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Address.AddressCityEntity;
import vnskilled.edu.ecom.Entity.Address.AddressWardsEntity;

import java.util.List;
@Repository
public interface AddressWardsRepository extends JpaRepository<AddressWardsEntity, Long> {
	boolean existsByName(String name);
	List<AddressWardsEntity> findByCityId(AddressCityEntity city);

}
