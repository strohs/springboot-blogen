package com.blogen.api.v1.mapper;

import com.blogen.api.v1.model.UserPrefsDTO;
import com.blogen.domain.UserPrefs;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for mapping between {@link com.blogen.domain.UserPrefs} and {@link com.blogen.api.v1.model.UserPrefsDTO}
 *
 * User: Cliff
 */
@Mapper
public interface UserPrefsMapper {

    UserPrefsMapper INSTANCE = Mappers.getMapper( UserPrefsMapper.class );

    UserPrefsDTO userPrefsToUserPrefsDto( UserPrefs userPrefs );

    UserPrefs userPrefsDtoToUserPrefs( UserPrefsDTO userPrefsDTO );

}
