package vnskilled.edu.ecom.Repository.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.Role.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
}
