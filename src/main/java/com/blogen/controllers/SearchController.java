package com.blogen.controllers;

import com.blogen.commands.SearchResultPageCommand;
import com.blogen.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for SearchPost requests
 *
 * @author Cliff
 */
@Slf4j
@Controller
public class SearchController {

    private PostService postService;

    @Autowired
    public SearchController( PostService postService ) {
        this.postService = postService;
    }

    @GetMapping("/posts/search")
    public String searchPosts( @RequestParam("searchStr") String searchStr, Model model) {
        log.debug( "search for posts with: " + searchStr );

        SearchResultPageCommand command = postService.searchPosts( searchStr, 0 );
        model.addAttribute( "page",command );

        return "searchResults";
    }

    @GetMapping("/posts/search/results")
    public String searchPostsResults( @RequestParam("search") String searchStr, @RequestParam("page") Integer page, Model model ) {
        log.debug( "search post results. page=" + page + " search=" + searchStr );

        SearchResultPageCommand command = postService.searchPosts( searchStr, page );
        model.addAttribute( "page",command );

        return "searchResults";
    }
}
