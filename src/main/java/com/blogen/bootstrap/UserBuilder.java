package com.blogen.bootstrap;

import com.blogen.domain.User;

/**
 * Builder used for bootstrapping User data
 * @author Cliff
 */
public class UserBuilder {

    User user;

    public UserBuilder( String userName, String firstName, String lastName, String email, String password ) {
        user = new User();
        user.setUserName( userName );
        user.setFirstName( firstName );
        user.setLastName( lastName );
        user.setEmail( email );
        user.setPassword( password );
    }

//    public Post addPost( Category category, String text ) {
//        Post post = new Post();
//        post.setText( text );
//        post.setCategory( category );
//        user.addPost( post );
//        return post;
//    }
//
//    public Post addChildPost( Post parent, String text ) {
//        Post child = new Post();
//        child.setCategory( parent.getCategory() );
//        child.setText( text );
//        parent.addChild( child );
//        return child;
//    }

    public User build() {
        return user;
    }
}
