package com.blogen.controllers;

import com.blogen.commands.PostCommand;
import com.blogen.commands.SearchResultPageCommand;
import com.blogen.services.PostService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests using for SearchController
 * @author Cliff
 */
public class SearchControllerTest {

    private SearchController searchController;

    @Mock
    PostService postService;

    MockMvc mockMvc;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks( this );

        searchController = new SearchController( postService );

        mockMvc = MockMvcBuilders.standaloneSetup( searchController )
                .setControllerAdvice( new ControllerExceptionHandler() )
                .build();
    }

    @Test
    public void should_returnOk_andSetModelWithPageData_when_searchPostsRequestIsMade() throws Exception {
        //search string
        String search = "text";
        PostCommand pc1 = getPostCommand1();
        PostCommand pc2 = getPostCommand2();
        List<PostCommand> postCommands = Arrays.asList( pc1,pc2 );

        SearchResultPageCommand command = new SearchResultPageCommand();
        command.setSearchStr( search );
        command.setPosts( postCommands );

        given( postService.searchPosts( anyString(),anyInt() )).willReturn( command );

        mockMvc.perform( get("/posts/search?searchStr=text") )
                .andExpect( status().isOk() )
                .andExpect( view().name( "searchResults" ) )
                .andExpect( model().attributeExists( "page" ) );

    }

    @Test
    public void should_returnOk_when_validSearchPostsResultsRequestIsMade() throws Exception {
        //search string
        String search = "text";
        PostCommand pc1 = getPostCommand1();
        PostCommand pc2 = getPostCommand2();
        List<PostCommand> postCommands = Arrays.asList( pc1,pc2 );

        SearchResultPageCommand command = new SearchResultPageCommand();
        command.setSearchStr( search );
        command.setPosts( postCommands );

        given( postService.searchPosts( anyString(),anyInt() )).willReturn( command );

        mockMvc.perform( get("/posts/search/results?search=text&page=1") )
                .andExpect( status().isOk() )
                .andExpect( view().name( "searchResults" ) )
                .andExpect( model().attributeExists( "page" ) );
    }

    @Test
    public void should_returnBadRequestError_when_invalidSearchPostsResultsRequestIsMade() throws Exception {
        //search string
        String search = "text";
        PostCommand pc1 = getPostCommand1();
        PostCommand pc2 = getPostCommand2();
        List<PostCommand> postCommands = Arrays.asList( pc1,pc2 );

        SearchResultPageCommand command = new SearchResultPageCommand();
        command.setSearchStr( search );
        command.setPosts( postCommands );

        given( postService.searchPosts( anyString(),anyInt() )).willReturn( command );

        mockMvc.perform( get("/posts/search/results?page=1") )
                .andExpect( status().isBadRequest() )
                .andExpect( view().name( "400error" ) );
    }

    @Test
    public void should_returnBadRequestError_when_malformedPageParameter() throws Exception {
        //search string
        String search = "text";
        PostCommand pc1 = getPostCommand1();
        PostCommand pc2 = getPostCommand2();
        List<PostCommand> postCommands = Arrays.asList( pc1,pc2 );

        SearchResultPageCommand command = new SearchResultPageCommand();
        command.setSearchStr( search );
        command.setPosts( postCommands );

        given( postService.searchPosts( anyString(),anyInt() )).willReturn( command );

        mockMvc.perform( get("/posts/search/results?search=text&page=1SD") )
                .andExpect( status().isBadRequest() )
                .andExpect( view().name( "400error" ) );
    }

    @Test
    public void should_throw4xx_when_malformedSearchUrl() throws Exception {
        //search string
        String search = "text";
        PostCommand pc1 = getPostCommand1();
        PostCommand pc2 = getPostCommand2();
        List<PostCommand> postCommands = Arrays.asList( pc1,pc2 );

        SearchResultPageCommand command = new SearchResultPageCommand();
        command.setSearchStr( search );
        command.setPosts( postCommands );

        given( postService.searchPosts( anyString(),anyInt() )).willReturn( command );

        mockMvc.perform( get("/posts/search/") )
                .andExpect( status().is4xxClientError() );
    }



    private PostCommand getPostCommand1() {
        PostCommand pc1 = new PostCommand();
        pc1.setId( 1L );
        pc1.setText( "this is post one text" );
        pc1.setTitle( "this is title one" );
        return pc1;
    }

    private PostCommand getPostCommand2() {
        PostCommand pc2 = new PostCommand();
        pc2.setId( 2L );
        pc2.setText( "this is post two text" );
        pc2.setTitle( "this is title two" );
        return pc2;
    }
}