package com.blogen.commands.mappers;

import com.blogen.commands.PostCommand;
import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
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
    @Mapping( target = "categoryName", source = "category.name")
    @Mapping( target = "userAvatar", source = "user.userPrefs.avatarImage")
    PostCommand postToPostCommand( Post post );


    //@Mapping( target = "parent.id", source = "parentId")
    //@Mapping( target = "category.id", source = "categoryId")
    //@Mapping( target = "user.id", source = "userId")
    //@Mapping( target = "user.userName", source = "userName")
    default Post postCommandToPost( PostCommand postCommand ) {
        Post post = new Post();
        post.setId( postCommand.getId() );
        post.setCategory( new Category() );
        post.getCategory().setName( postCommand.getCategoryName() );
        UserPrefs prefs = new UserPrefs();
        prefs.setAvatarImage( postCommand.getUserAvatar() );
        post.setUser( new User() );
        post.getUser().setUserName( postCommand.getUserName() );
        post.getUser().setId( postCommand.getUserId() );
        post.getUser().setUserPrefs( prefs );
        post.setImageUrl( postCommand.getImageUrl() );
        post.setTitle( postCommand.getTitle() );
        post.setText( postCommand.getText() );
        //postCommand.parentId is checked in the service layer
        return post;
    }




}
