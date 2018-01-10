package com.blogen.commands.mappers;

import com.blogen.commands.UserPrefsCommand;
import com.blogen.domain.UserPrefs;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * Uses MapStruct to map between {@link com.blogen.domain.UserPrefs} and {@link com.blogen.commands.UserPrefsCommand}
 * 
 * @author Cliff
 */
@Mapper
public interface UserPrefsCommandMapper {

    UserPrefsCommandMapper INSTANCE = Mappers.getMapper( UserPrefsCommandMapper.class );

    UserPrefsCommand userPrefsToUserPrefsCommand( UserPrefs userPrefs );

    UserPrefs userPrefsCommandToUserPrefs( UserPrefsCommand userPrefsCommand );

}
