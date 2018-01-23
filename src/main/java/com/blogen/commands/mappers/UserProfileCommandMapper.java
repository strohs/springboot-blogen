package com.blogen.commands.mappers;

import com.blogen.commands.UserProfileCommand;
import com.blogen.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for mapping to and from {@link com.blogen.commands.UserProfileCommand} and {@link com.blogen.domain.User}
 *
 * @author Cliff
 */
@Mapper
public interface UserProfileCommandMapper {

    UserProfileCommandMapper INSTANCE = Mappers.getMapper( UserProfileCommandMapper.class );

    @Mapping( target = "avatarImage", source = "user.userPrefs.avatarImage")
    UserProfileCommand userToUserProfileCommand( User user );

    @Mapping( target = "userPrefs.avatarImage", source = "avatarImage")
    User userProfileCommandToUser( UserProfileCommand command );
}
