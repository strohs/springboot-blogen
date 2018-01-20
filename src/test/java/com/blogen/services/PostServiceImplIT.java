package com.blogen.services;

import com.blogen.commands.PageCommand;
import com.blogen.commands.PostCommand;
import com.blogen.commands.mappers.PostCommandMapper;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Matchers.anyLong;

/**
 * Integration Tests for {@link PostServiceImpl}
 *
 * @author Cliff
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"blogen.posts.per.page=3"})
public class PostServiceImplIT {

    @Autowired
    private PostServiceImpl postService;

    private static final Long ALL_CATEGORIES_ID    = 0L;
    private static final Long BUSINESS_CATEGORY_ID = 1L;
    private static final Long WEBDEV_CATEGORY_ID   = 2L;
    private static final Long TECH_CATEGORY_ID     = 3L;
    private static final Long HEALTH_CATEGORY_ID   = 4L;


    @Test
    public void should_returnThreePosts_when_getAllPostsByUser() {
        //this user has 3 parent posts
        Long userId = 3L;
        List<PostCommand> commands = postService.getAllPostsByUser( userId );

        assertThat( commands.size(), is(3) );
    }

    @Test
    public void should_returnTwoPosts_when_getAllPostsByUserAndCategoryForPage() throws Exception {
        //this user has 2 posts in Business category
        Long userId = 5L;
        PageCommand pageCommand = postService.getAllPostsByUserAndCategoryForPage( userId, BUSINESS_CATEGORY_ID, 0 );
        assertThat( pageCommand.getTotalElements(), is( 2L ));
    }

    @Test
    public void should_returnFourPosts_when_getAllPostsByCategoryForPage() throws Exception {
        //there are 4 posts total in Business category
        Long totalBusinessPosts = 4L;
        PageCommand pageCommand = postService.getAllPostsByCategoryForPage( BUSINESS_CATEGORY_ID, 0 );
        assertThat( pageCommand.getTotalElements(), is( totalBusinessPosts ));
    }

    @Test
    public void should_returnSixteenPosts_when_getAllPostsForPage() throws Exception {
        //there are 16 parent posts total
        Long totalParentPosts = 16L;
        PageCommand pageCommand = postService.getAllPostsByCategoryForPage( ALL_CATEGORIES_ID, 0 );
        assertThat( pageCommand.getTotalElements(), is( totalParentPosts ));
    }

    @Test
    public void should_returnTenPosts_when_GetAllPostsByUserAndCategoryForPage() throws Exception {
        //this user has 10 Posts total across all categories
        Long userId = 5L;
        Long totalParentPosts = 10L;
        PageCommand pageCommand = postService.getAllPostsByUserAndCategoryForPage( userId, ALL_CATEGORIES_ID, 0 );
        assertThat( pageCommand.getTotalElements(), is( totalParentPosts ));
    }

    @Test
    public void should_returnThreePosts_when_PageZeroIsRequested() throws Exception {
        //this user has 10 Posts total
        Long userId = 5L;
        Long totalParentPosts = 10L;
        PageCommand pageCommand = postService.getAllPostsByUserAndCategoryForPage( userId, ALL_CATEGORIES_ID, 0 );
        assertThat( pageCommand.getPosts().size(), is(3));
        assertThat( pageCommand.getPosts().get( 0 ).getUserId(), is( userId ));
        assertThat( pageCommand.getRequestedPage(), is(0));
        assertThat( pageCommand.getTotalPages(), is(4));
    }

    @Test
    public void should_returnPostsInDescendingOrder_when_AnyPageIsRequested() throws Exception {
        //this user has 10 Posts total
        Long userId = 5L;
        Long mostRecentPostId = 26L;
        PageCommand pageCommand = postService.getAllPostsByUserAndCategoryForPage( userId, ALL_CATEGORIES_ID, 0 );
        assertThat( pageCommand.getPosts().get( 0 ).getId(), is( mostRecentPostId ));
    }

    @Test
    public void should_returnOnePost_when_PageThreeIsRequested() throws Exception {
        //this user has 10 Posts total
        Long userId = 5L;
        Long postId = 17L;
        PageCommand pageCommand = postService.getAllPostsByUserAndCategoryForPage( userId, ALL_CATEGORIES_ID, 3 );
        assertThat( pageCommand.getPosts().size(), is(1));
        assertThat( pageCommand.getPosts().get( 0 ).getId(), is(postId));
    }

    @Test
    @Transactional
    @WithMockUser("lizreed")
    public void should_saveNewParentPost_when_savePostCommand() throws Exception {
        Long userId = 5L;
        PostCommand postCommand = new PostCommand();
        postCommand.setUserId( userId );
        postCommand.setTitle( "new title" );
        postCommand.setText( "new text" );
        postCommand.setImageUrl( "http://pexels.com" );
        postCommand.setCategoryName( "Business" );
        PostCommand savedCommand = postService.savePostCommand( postCommand );
        assertThat( savedCommand.getId(), is(notNullValue()));
        assertThat( savedCommand.getText(), is("new text"));
        assertThat( savedCommand.getTitle(), is("new title"));
        assertThat( savedCommand.getChildren().size(), is(0));
    }

    @Test
    @Transactional
    @WithMockUser("lizreed")
    public void should_saveNewChildPost_when_savingPostCommandThatHasValidParentId() throws Exception {
        Long userId = 4L;
        //this is a post with no children
        Long parentId = 6L;
        PostCommand postCommand = new PostCommand();
        postCommand.setParentId( parentId );
        postCommand.setUserId( userId );
        postCommand.setTitle( "new title" );
        postCommand.setText( "new text" );
        postCommand.setImageUrl( "http://pexels.com" );
        postCommand.setCategoryName( "Health & Fitness" );
        //saves the child and returns back the parent post command object
        PostCommand savedCommand = postService.savePostCommand( postCommand );
        assertThat( savedCommand.getId(), is(notNullValue()));
        assertThat( savedCommand.getChildren().size(), is(1));
        assertThat( savedCommand.getChildren().get( 0 ).getId(), is(notNullValue()));
        assertThat( savedCommand.getChildren().get( 0 ).getText(), is("new text"));
        assertThat( savedCommand.getChildren().get( 0 ).getTitle(), is("new title"));
        assertThat( savedCommand.getChildren().get( 0 ).getCategoryName(),is("Health & Fitness"));
    }


}