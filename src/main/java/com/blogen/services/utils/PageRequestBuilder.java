package com.blogen.services.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

/**
 * Helper class for building Spring-Data page requests.
 *
 * @see  {@link PageRequest}
 *
 * @author Cliff
 */
@Component
public class PageRequestBuilder {

    //number of parent posts to display on the posts.html page
    @Value( "${blogen.posts.per.page}" )
    private int POSTS_PER_PAGE;

    @Value( "${blogen.categories.per.page}" )
    private int CATEGORIES_PER_PAGE;

    public PageRequest buildPostPageRequest( int pageNum, Sort.Direction sortDir, String property ) {
        return PageRequest.of( pageNum, POSTS_PER_PAGE, sortDir, property );
    }

    public PageRequest buildCategoryPageRequest( int pageNum, Sort.Direction sortDir, String property ) {
        return PageRequest.of( pageNum, CATEGORIES_PER_PAGE, sortDir, property );
    }

    public PageRequest buildPageRequest( int pageNum, int elementsPerPage, Sort.Direction sortDir, String property ) {
        return PageRequest.of( pageNum, elementsPerPage, sortDir, property );
    }
}
