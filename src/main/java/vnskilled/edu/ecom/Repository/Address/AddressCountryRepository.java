package vnskilled.edu.ecom.Repository.Address;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Address.AddressCountryEntity;

import java.util.List;
@Repository
public interface AddressCountryRepository extends JpaRepository<AddressCountryEntity, Long> {
	boolean existsByName(String name);
	List<AddressCountryEntity> findAll();
}