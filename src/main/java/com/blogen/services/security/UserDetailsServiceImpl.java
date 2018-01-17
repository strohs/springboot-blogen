package com.blogen.services.security;

import com.blogen.commands.mappers.UserDetailsMapper;
import com.blogen.domain.User;
import com.blogen.repositories.UserRepository;
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

    private UserRepository userRepository;
    private UserDetailsMapper userDetailsMapper;

    @Autowired
    public UserDetailsServiceImpl( UserRepository userRepository, UserDetailsMapper userDetailsMapper ) {
        this.userRepository = userRepository;
        this.userDetailsMapper = userDetailsMapper;
    }

    @Override
    public UserDetails loadUserByUsername( String username ) throws UsernameNotFoundException {
        User user = userRepository.findByUserName( username );
        return userDetailsMapper.userToUserDetails( user );
    }
}
