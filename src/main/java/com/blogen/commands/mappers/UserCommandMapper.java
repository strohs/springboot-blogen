package com.blogen.commands.mappers;

import com.blogen.commands.UserCommand;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Uses MapStruct to map between {@link com.blogen.domain.User} and {@link com.blogen.commands.UserCommand}
 *
 * @author Cliff
 */
@Mapper
public interface UserCommandMapper {

    UserCommandMapper INSTANCE = Mappers.getMapper( UserCommandMapper.class );



    UserCommand userToUserCommand( User user );

    default User userCommandToUser( UserCommand userCommand ) {
        User user = new User();
        user.setId( userCommand.getId() );
        user.setUserName( userCommand.getUserName() );
        user.setFirstName( userCommand.getFirstName() );
        user.setLastName( userCommand.getLastName() );
        user.setEmail( userCommand.getEmail() );
        user.setPassword( userCommand.getPassword() );
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setId( userCommand.getUserPrefs().getId() );
        userPrefs.setAvatarImage( userCommand.getUserPrefs().getAvatarImage() );
        //make sure to set this so OneToOne relationship is preserved
        user.setUserPrefs( userPrefs );
        return user;
    }

}
