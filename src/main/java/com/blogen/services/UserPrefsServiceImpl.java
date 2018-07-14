package com.blogen.services;

import com.blogen.commands.UserPrefsCommand;
import com.blogen.commands.mappers.UserPrefsCommandMapper;
import com.blogen.domain.UserPrefs;
import com.blogen.repositories.UserPrefsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides CRUD ops for storing and retrieving UserPrefs on the backend
 *
 * @author Cliff
 */
@Slf4j
@Service
public class UserPrefsServiceImpl implements UserPrefsService {

    private UserPrefsCommandMapper userPrefsCommandMapper;
    private UserPrefsRepository userPrefsRepository;

    @Autowired
    public UserPrefsServiceImpl( UserPrefsCommandMapper userPrefsCommandMapper, UserPrefsRepository userPrefsRepository ) {
        this.userPrefsCommandMapper = userPrefsCommandMapper;
        this.userPrefsRepository = userPrefsRepository;
    }

    @Override
    public UserPrefsCommand getUserPrefsByUserId( Long userId ) {
        UserPrefs userPrefs = userPrefsRepository.findByUser_Id( userId );
        return userPrefsCommandMapper.userPrefsToUserPrefsCommand( userPrefs );
    }


}
