package vnskilled.edu.ecom.Repository.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vnskilled.edu.ecom.Entity.User.UserEntity;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u " +
            "WHERE (:email IS NULL OR u.email LIKE %:email%) " +
            "AND (:roleId IS NULL OR u.roleId.id = :roleId) " +
            "AND (:active IS NULL OR u.active = :active) " +
            "AND (:emailActive IS NULL OR u.emailActive = :emailActive)")
    List<UserEntity> findUsersByCriteria(
            @Param("email") String email,
            @Param("roleId") Long roleId,
            @Param("active") Boolean active,
            @Param("emailActive") Boolean emailActive);
    @Query("SELECT u FROM UserEntity u WHERE u.roleId.id =:roleId")
    List<UserEntity> findUserByRoleId(@Param("roleId") Long roleId);
    UserEntity findByEmail(String email);
    UserEntity findByPhone(String phone);
    boolean existsByEmail(String email);
}
