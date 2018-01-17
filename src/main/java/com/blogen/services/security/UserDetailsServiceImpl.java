package com.blogen.services.security;

import com.blogen.commands.mappers.UserDetailsMapper;
import com.blogen.domain.User;
import com.blogen.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of Spring UserDetailsService for our Blogen users
 * @author Cliff
 */
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    public UserDetailsServiceImpl( UserService userService, UserDetailsMapper userDetailsMapper ) {
        this.userService = userService;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userService.findByUserName( username );
        return userDetailsMapper.userToUserDetails( user );
    }
}
