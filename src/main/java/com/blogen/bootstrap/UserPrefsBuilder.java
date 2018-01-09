package com.blogen.bootstrap;

import com.blogen.domain.UserPrefs;

/**
 * Convenience builder class for bootstrapping UserPrefs data
 *
 * User: Cliff
 */
public class UserPrefsBuilder {

    UserPrefs userPrefs;

    public UserPrefsBuilder( String avatarImage ) {
        userPrefs = new UserPrefs();
        userPrefs.setAvatarImage( avatarImage );
    }

    public UserPrefs build() {
        return userPrefs;
    }
}
