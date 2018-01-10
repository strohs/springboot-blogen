package com.blogen.commands.mappers;

import com.blogen.commands.PostCommand;
import com.blogen.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Uses MapStruct to map between {@link com.blogen.domain.Post} and {@link com.blogen.commands.PostCommand}
 *
 * @author Cliff
 */
@Mapper
public interface PostCommandMapper {

    PostCommandMapper INSTANCE = Mappers.getMapper( PostCommandMapper.class );

    @Mapping( target = "created", dateFormat = "EEE MMM dd, yyyy hh:mm a")
    @Mapping( target = "parentId", source = "parent.id")
    @Mapping( target = "userId", source = "user.id")
    @Mapping( target = "userName", source = "user.userName")
    @Mapping( target = "categoryId", source = "category.id")
    PostCommand postToPostCommand( Post post );

    //todo make sure we will never need to do conversion from postCommand->Post. The data we will need should be submitted with the HTML Form
    //TODO may need to write custom mapping logic
    Post postCommandToPost( PostCommand postCommand );

}
