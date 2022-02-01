package com.blogen.services.security;

import com.blogen.domain.User;
import com.blogen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Implementation of Spring UserDetailsService for our Blogen domain Users
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl( UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userService.findByUserName( username );

        if (user != null) {
            UserPrincipal principal = new UserPrincipal();

            principal.setUsername( user.getUserName() );
            principal.setPassword( user.getEncryptedPassword() );
            principal.setEnabled( user.getEnabled() );

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            user.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getRole()));
            });
            principal.setAuthorities(authorities);

            return principal;
        } else {
            throw new UsernameNotFoundException("db returned null for username" + username);
        }
    }
}
