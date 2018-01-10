package com.blogen.bootstrap;

import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;

/**
 * builder for bootstrapping post data
 * @author Cliff
 */
public class PostBuilder {

    Post post;

    public PostBuilder( User user, Category category, String imageUrl, String title, String text ) {
        post = new Post();
        post.setUser( user );
        post.setCategory( category );
        post.setImageUrl( imageUrl );
        post.setTitle( title );
        post.setText( text );
    }

    public Post addChildPost( User user, String title, String text ) {
        Post child = new Post();
        child.setUser( user );
        child.setCategory( post.getCategory() );
        child.setImageUrl( null );
        child.setTitle( title );
        child.setText( text );
        post.addChild( child );
        return post;
    }

    public Post build() {
        return post;
    }

}
