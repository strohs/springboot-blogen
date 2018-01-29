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

    @Mapping( source = "user.userName", target = "userName")
    @Mapping( source = "category.name", target = "categoryName")
    //@Mapping( target = "created", dateFormat = "EEE MMM dd, yyyy hh:mm a")
    PostDTO postToPostDto( Post post );

    @Mapping( target = "user.userName", source = "userName")
    @Mapping( target = "category.name",  source = "categoryName")
    Post postDtoToPost( PostDTO postDTO );

    /**
     * Merge non-null fields of PostDTO into a {@link Post} object
     * @param target Post object to merge fields into
     * @param source PostDTO containing the non-null fields you want to merge
     * @return a Post object containing the merged fields
     */
    default Post mergePostDtoToPost( Post target, PostDTO source ) {
        if ( source.getImageUrl() != null )
            target.setImageUrl( source.getImageUrl() );
        if ( source.getCategoryName() != null )
            target.getCategory().setName( source.getCategoryName() );
        if ( source.getUserName() != null )
            target.getUser().setUserName( source.getUserName() );
        if ( source.getCreated() != null )
            target.setCreated( source.getCreated() );
        if ( source.getTitle() != null )
            target.setTitle( source.getTitle() );
        if ( source.getText() != null )
            target.setText( source.getText() );
        return target;
    }

}
