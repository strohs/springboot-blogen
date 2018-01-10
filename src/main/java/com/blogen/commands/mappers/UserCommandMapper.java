package com.blogen.commands.mappers;

import com.blogen.commands.UserCommand;
import com.blogen.domain.User;
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

    User userCommandToUser( UserCommand userCommand );

}
