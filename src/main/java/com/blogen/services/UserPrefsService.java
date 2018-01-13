package com.blogen.services;

import com.blogen.commands.UserPrefsCommand;

/**
 * UserPrefsService methods for CRUD ops on UserPrefs
 *
 * @author Cliff
 */
public interface UserPrefsService {

    UserPrefsCommand getUserPrefsByUserId( Long userId );
;


}
