package com.blogen.api.v1.mapper;

import com.blogen.api.v1.model.PostDTO;
import com.blogen.domain.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for mapping data between {@link com.blogen.domain.Post} and {@link com.blogen.api.v1.model.PostDTO}
 *
 * @author Cliff
 */
@Mapper
public interface PostMapper {

    PostMapper INSTANCE = Mappers.getMapper( PostMapper.class );

    @Mapping( source = "parent.id", target = "parentId" )
    @Mapping( source = "user.id", target = "userId")
    @Mapping( source = "user.userName", target = "userName")
    @Mapping( target = "created", dateFormat = "EEE MMM dd, yyyy hh:mm a")

    PostDTO postToPostDto( Post post );

    //TODO decide if this would actually be needed in the REST API
    //Post postDtoToPost( PostDTO postDTO );

}
