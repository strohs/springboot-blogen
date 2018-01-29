package com.blogen.api.v1.mappers;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mappers for mapping data between {@link com.blogen.domain.Post} and {@link com.blogen.api.v1.model.PostDTO}
 *
 * @author Cliff
 */
@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );

    @Mapping( source = "parent.id", target = "parentId" )
    @Mapping( source = "user.id", target = "userId")
    @Mapping( source = "user.userName", target = "userName")
    @Mapping( source = "category.name", target = "categoryName")
    //@Mapping( target = "created", dateFormat = "EEE MMM dd, yyyy hh:mm a")
    PostDTO postToPostDto( Post post );

    @Mapping( target = "parent.id", source = "parentId")
    @Mapping( target = "user.id", source = "userId")
    @Mapping( target = "user.userName", source = "userName")
    @Mapping( target = "category.name",  source = "categoryName")
    Post postDtoToPost( PostDTO postDTO );

}
