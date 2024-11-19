package vnskilled.edu.ecom.Configuration.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vnskilled.edu.ecom.Entity.Role.RoleEntity;
import vnskilled.edu.ecom.Entity.User.UserEntity;
import vnskilled.edu.ecom.Model.Request.RequestContext;
import vnskilled.edu.ecom.Repository.Role.RoleRepository;
import vnskilled.edu.ecom.Repository.User.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fetch user entity and validate it
        UserEntity userEntity = userRepository.findByEmail(username);
	    if (userEntity == null)
		    throw new RuntimeException ("Không tìm thấy người dùng với email: " + username);
	    if (!userEntity.isEmailActive())
		    throw new RuntimeException("Tài khoản chưa được xác thực email");
	    if (!userEntity.isActive())
		    throw new RuntimeException("Tài khoản của bạn đã bị khóa");


        return buildUserDetails(userEntity, userEntity.getRoleId ());
    }

    private UserDetails buildUserDetails(UserEntity userEntity, RoleEntity roleEntity) {

        return User.withUsername(userEntity.getEmail())
                .password(userEntity.getPassword())
                .roles(roleEntity.getName())
                .build();
    }
}
