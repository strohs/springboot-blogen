package com.blogen.api.v1.mapper;

import com.blogen.api.v1.model.UserDTO;
import com.blogen.domain.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for mapping between {@link com.blogen.domain.User} and {@link com.blogen.api.v1.model.UserDTO}
 *
 * @author Cliff
 */
@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserDTO userToUserDto( User user );

    User userDtoToUser( UserDTO userDTO );


}
