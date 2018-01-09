package com.blogen.bootstrap;

import com.blogen.domain.Category;
import com.blogen.domain.Post;
import com.blogen.domain.User;
import com.blogen.domain.UserPrefs;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserPrefsRepository;
import com.blogen.repositories.UserRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Bootstrap the blogen embedded JPA database with data
 * @author Cliff
 */
@Log4j
@Component
public class BlogenBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private CategoryRepository categoryRepository;
    private UserRepository userRepository;
    private UserPrefsRepository userPrefsRepository;
    private PostRepository postRepository;

    @Autowired
    public BlogenBootstrap( CategoryRepository categoryRepository, UserRepository userRepository,
                            UserPrefsRepository userPrefsRepository, PostRepository postRepository ) {
        this.categoryRepository = categoryRepository;
        this.userPrefsRepository = userPrefsRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }


    public void initData() {
        //BUILD CATEGORIES
        Category all = CategoryBuilder.build( "All Categories" );
        Category my = CategoryBuilder.build( "My Categories" );
        Category business = CategoryBuilder.build( "Business" );
        Category webDev = CategoryBuilder.build( "Web Development" );
        Category tech = CategoryBuilder.build( "Tech Gadgets" );
        Category health = CategoryBuilder.build( "Health & Fitness");

        categoryRepository.save( all );
        categoryRepository.save( my );
        categoryRepository.save( business );
        categoryRepository.save( webDev );
        categoryRepository.save( tech );
        categoryRepository.save( health );

        //BUILD USER PREFS
        UserPrefsBuilder upb = new UserPrefsBuilder( "avatar1.jpg" );
        UserPrefs upJohn = upb.build();
        userPrefsRepository.save( upJohn );

        upb = new UserPrefsBuilder( "avatar2.jpg" );
        UserPrefs upAdmin = upb.build();
        userPrefsRepository.save( upAdmin );

        upb = new UserPrefsBuilder( "avatar3.jpg" );
        UserPrefs upMaggie = upb.build();
        userPrefsRepository.save( upMaggie );

        upb = new UserPrefsBuilder( "avatar4.jpg");
        UserPrefs upWilliam = upb.build();
        userPrefsRepository.save( upWilliam );


        //BUILD USERS
        //administrator
        UserBuilder ub = new UserBuilder( "theAdmin","Carl","Sagan","admin@blogen.org","adminpassword",upAdmin );
        User admin = ub.build();
        userRepository.save( admin );

        //JohnDoe
        ub = new UserBuilder( "johndoe","John","Doe","jdoe@gmail.com","password", upJohn );
        User john = ub.build();
        userRepository.save( john );

        //Maggie McGill
        ub = new UserBuilder( "mgill","Maggie","McGill","gilly@yahoo.com","password", upMaggie );
        User maggie = ub.build();
        userRepository.save( maggie );

        //William Wallace
        ub = new UserBuilder( "scotsman","William","Wallace","scotty@hotmail.com","password", upWilliam );
        User william = ub.build();
        userRepository.save( william );

        // BUILD POSTS
        //
        //build posts for John - 1 Parent post with 3 child posts
        PostBuilder pb = new PostBuilder( john, tech, null, "johns tech post" );
        Post parent = pb.build();
        //postRepository.save( parent );
        Post child1 = pb.addChildPost( john, "child post1 for tech" );
        Post child2 = pb.addChildPost( maggie, "maggies replay to tech post" );
        Post child3 = pb.addChildPost( william,"wills reply to johns post" );
        Post child4 = pb.addChildPost( william,"wills reply to his reply" );
        postRepository.save( parent );

        //
        //build posts for william - 2 parent posts
        pb = new PostBuilder( william, health,null,"health and wellness tip" );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( william, business, "http://lorempixel.com/400/200/business/1", "business post" );
        parent = pb.build();
        postRepository.save( parent );

        //
        //build posts for maggie - 3 parent posts with 2 child posts each
        pb = new PostBuilder( maggie, business,"http://lorempixel.com/400/200/business/1", "maggies post about business" );
        parent = pb.build();
        child1 = pb.addChildPost( john, "johns reply to maggies business post" );
        child2 = pb.addChildPost( maggie, "maggies reply to johns reply" );
        postRepository.save( parent );

        pb = new PostBuilder( maggie,health,"http://lorempixel.com/400/200/sports/3","maggies parent post about health" );
        parent = pb.build();
        child1 = pb.addChildPost( william, "william first reply to maggies health post" );
        child2 = pb.addChildPost( william, "william second reply to maggies health post" );
        postRepository.save( parent );

        pb = new PostBuilder( maggie, webDev,"http://lorempixel.com/400/200/technics/3","maggies parent post about webdev" );
        parent = pb.build();
        child1 = pb.addChildPost( william, "william reply to maggies webdev post" );
        child2 = pb.addChildPost( admin, "admins reply to maggies webdev post" );
        postRepository.save( parent );

        //there should now be sixteen posts in total - six parent posts and ten child posts
    }

    @Transactional
    @Override
    public void onApplicationEvent( ContextRefreshedEvent event ) {
        initData();
        log.info("Finished bootstrapping data");
    }
}
