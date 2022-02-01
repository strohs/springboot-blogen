package com.blogen.commands.mappers;

import com.blogen.commands.CategoryCommand;
import com.blogen.domain.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Uses MapStruct to map between {@link com.blogen.domain.Category} and {@link com.blogen.commands.CategoryCommand}
 *
 * @author Cliff
 */
@Mapper(componentModel = "spring")
public interface CategoryCommandMapper {

    CategoryCommandMapper INSTANCE = Mappers.getMapper( CategoryCommandMapper.class );

    @Mapping( target = "created", dateFormat = "MMM dd, yyyy hh:mm a")
    CategoryCommand categoryToCategoryCommand( Category category );

    Category categoryCommandToCategory( CategoryCommand categoryCommand );
}
