package com.blogen.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Helper class for building page requests
 * @see  {@link PageRequest}
 *
 * @author Cliff
 */
@Component
public class PageRequestBuilder {

    //number of parent posts to display on the posts.html page
    @Value( "${blogen.posts.per.page}" )
    private int POSTS_PER_PAGE;

    public PageRequest buildPageRequest( int pageNum, Sort.Direction sortDir, String property ) {
        return new PageRequest( pageNum, POSTS_PER_PAGE, sortDir, property );
    }

    public PageRequest buildPageRequest( int pageNum, int elementsPerPage, Sort.Direction sortDir, String property ) {
        return new PageRequest( pageNum, elementsPerPage, sortDir, property );
    }
}
