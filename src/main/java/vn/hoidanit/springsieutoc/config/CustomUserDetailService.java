package vn.hoidanit.springsieutoc.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import vn.hoidanit.springsieutoc.service.UserService;

import java.util.Collections;


public class CustomUserDetailService implements UserDetailsService {
    private final UserService userService;

    public CustomUserDetailService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.hoidanit.springsieutoc.model.User myUser = this.userService.findUserByEmail(username);

        if (myUser == null) {
            throw new UsernameNotFoundException("user not found");
        }

        return new User(myUser.getEmail(), myUser.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + myUser.getRole().getName())));
    }
}
