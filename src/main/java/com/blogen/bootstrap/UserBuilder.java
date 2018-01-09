package com.blogen.bootstrap;

import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;

/**
 * Builder used for bootstrapping User data
 * @author Cliff
 */
public class UserBuilder {

    User user;

    public UserBuilder( String userName, String firstName, String lastName, String email, String password, UserPrefs userPrefs ) {
        user = new User();
        user.setUserName( userName );
        user.setFirstName( firstName );
        user.setLastName( lastName );
        user.setEmail( email );
        user.setPassword( password );
        user.addUserPrefs( userPrefs );
    }

    public void setUserPrefs( UserPrefs userPrefs ) {
        user.addUserPrefs( userPrefs );
    }

    public User build() {
        return user;
    }
}
