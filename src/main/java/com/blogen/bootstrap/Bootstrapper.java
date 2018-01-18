package com.blogen.bootstrap;

import com.blogen.domain.*;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserPrefsRepository;
import com.blogen.repositories.UserRepository;
import com.blogen.services.RoleService;
import com.blogen.services.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Bootstrap the blogen embedded JPA database with data
 *
 *  @author Cliff
 */
@Log4j
@Component
public class Bootstrapper implements ApplicationListener<ContextRefreshedEvent> {

    private CategoryRepository categoryRepository;
    private UserService userService;
    //private UserRepository userRepository;
    private UserPrefsRepository userPrefsRepository;
    private PostRepository postRepository;
    private RoleService roleService;

    private static final String IMG_SERVICE = "http://lorempixel.com/400/200";
    private static final String IMG_SERVICE_BUSINESS = "http://lorempixel.com/400/200/business";
    private static final String IMG_SERVICE_NATURE = "http://lorempixel.com/400/200/nature";
    private static final String IMG_SERVICE_FOOD = "http://lorempixel.com/400/200/food";
    private static final String IMG_SERVICE_GREY = "http://lorempixel.com/g/400/200";

    @Autowired
    public Bootstrapper( CategoryRepository categoryRepository,
                         UserPrefsRepository userPrefsRepository, PostRepository postRepository,RoleService roleService ) {
        this.categoryRepository = categoryRepository;
        this.userPrefsRepository = userPrefsRepository;
        //this.userService = userService;
        this.postRepository = postRepository;
        this.roleService = roleService;
    }

    @Autowired
    public void setUserService( UserService userService ) {
        this.userService = userService;
    }

    public void initData() {
        //BUILD ROLES
        Role userRole = new Role();
        userRole.setRole("USER");
        roleService.saveOrUpdate(userRole);
        log.info("Saved role" + userRole.getRole());
        Role adminRole = new Role();
        adminRole.setRole("ADMIN");
        roleService.saveOrUpdate(adminRole);
        log.info("Saved role" + adminRole.getRole());


        //BUILD CATEGORIES
        //Category all = CategoryBuilder.build( "All Categories" );
        //Category my = CategoryBuilder.build( "My Categories" );
        Category business = CategoryBuilder.build( "Business" );
        Category webDev = CategoryBuilder.build( "Web Development" );
        Category tech = CategoryBuilder.build( "Tech Gadgets" );
        Category health = CategoryBuilder.build( "Health & Fitness");

        //categoryRepository.save( all );
        //categoryRepository.save( my );
        categoryRepository.save( business );
        categoryRepository.save( webDev );
        categoryRepository.save( tech );
        categoryRepository.save( health );



        //BUILD USER PREFS
        UserPrefsBuilder upb = new UserPrefsBuilder( "avatar3.jpg" );
        UserPrefs upJohn = upb.build();
        userPrefsRepository.save( upJohn );

        upb = new UserPrefsBuilder( "avatar2.jpg" );
        UserPrefs upAdmin = upb.build();
        userPrefsRepository.save( upAdmin );

        upb = new UserPrefsBuilder( "avatar1.jpg" );
        UserPrefs upMaggie = upb.build();
        userPrefsRepository.save( upMaggie );

        upb = new UserPrefsBuilder( "avatar4.jpg");
        UserPrefs upWilliam = upb.build();
        userPrefsRepository.save( upWilliam );

        upb = new UserPrefsBuilder( "avatar5.jpg");
        UserPrefs upElizabeth = upb.build();
        userPrefsRepository.save( upElizabeth );


        //BUILD USERS
        //administrator
        UserBuilder ub = new UserBuilder( "admin","Carl","Sagan","admin@blogen.org","adminpassword",upAdmin );
        User admin = ub.build();
        admin.addRole( adminRole );
        userService.saveUser( admin );

        //JohnDoe
        ub = new UserBuilder( "johndoe","John","Doe","jdoe@gmail.com","password", upJohn );
        User john = ub.build();
        john.addRole( userRole );
        userService.saveUser( john );

        //Maggie McGill
        ub = new UserBuilder( "mgill","Maggie","McGill","gilly@yahoo.com","password", upMaggie );
        User maggie = ub.build();
        maggie.addRole( userRole );
        userService.saveUser( maggie );

        //William Wallace
        ub = new UserBuilder( "scotsman","William","Wallace","scotty@hotmail.com","password", upWilliam );
        User william = ub.build();
        william.addRole( userRole );
        userService.saveUser( william );

        //Elizabeth Reed
        ub = new UserBuilder( "lizreed","Elizabeth","Reed","liz@gmail.com","password", upElizabeth);
        User elizabeth = ub.build();
        elizabeth.addRole( userRole );
        userService.saveUser( elizabeth );

        // BUILD POSTS
        //
        //build posts for John - 1 Parent post with 3 child posts
        PostBuilder pb = new PostBuilder( john, tech, IMG_SERVICE,"Love this tech", "johns tech post" );
        Post parent = pb.build();
        //postRepository.save( parent );
        Post child1 = pb.addChildPost( john,"Love it too", "child post1 for tech", IMG_SERVICE_NATURE );
        Post child2 = pb.addChildPost( maggie,"Not so fast", "maggies replay to tech post", IMG_SERVICE_FOOD);
        Post child3 = pb.addChildPost( william,"Here today gone tomorrow","wills reply to johns post", IMG_SERVICE_GREY );
        Post child4 = pb.addChildPost( william,"no no no","wills reply to his reply", IMG_SERVICE );
        postRepository.save( parent );

        //
        //build posts for william - 2 parent posts
        pb = new PostBuilder( william, health,IMG_SERVICE_FOOD,"Start lifting today","health and wellness tip" );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( william, business, "http://lorempixel.com/400/200/business/1","Try these stocks", "business post" );
        parent = pb.build();
        postRepository.save( parent );

        //
        //build posts for maggie - 3 parent posts with 2 child posts each
        pb = new PostBuilder( maggie, business,"http://lorempixel.com/400/200/business/1","Bitcoin or bust", "maggies post about business" );
        parent = pb.build();
        child1 = pb.addChildPost( john,"probably buying it", "johns reply to maggies business post", IMG_SERVICE_NATURE );
        child2 = pb.addChildPost( maggie,"beware the bubble", "maggies reply to johns reply", IMG_SERVICE_GREY );
        postRepository.save( parent );

        pb = new PostBuilder( maggie,health,"http://lorempixel.com/400/200/sports/3","Eat these healthy foods","maggies parent post about health" );
        parent = pb.build();
        child1 = pb.addChildPost( william,"sounds gross", "william first reply to maggies health post", IMG_SERVICE_FOOD );
        child2 = pb.addChildPost( william, "on second thought...","william second reply to maggies health post", IMG_SERVICE_GREY );
        postRepository.save( parent );

        pb = new PostBuilder( maggie, webDev,"http://lorempixel.com/400/200/technics/3","Is PHP dead?","maggies parent post about webdev" );
        parent = pb.build();
        child1 = pb.addChildPost( william,"I doubt it", "william reply to maggies webdev post", IMG_SERVICE_BUSINESS );
        child2 = pb.addChildPost( admin, "We don't use it here","admins reply to maggies webdev post", IMG_SERVICE_GREY );
        postRepository.save( parent );

        //
        //Build Posts for elizabeth (10 parent posts for her)
        pb = new PostBuilder( elizabeth, business, IMG_SERVICE, "Invest now","these stocks are at all time lows and ready to rise" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 1, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, health, IMG_SERVICE_GREY, "Proper Diet trumps all","No matter what excercise you do, just remember you can never out-train a poor diet" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 2, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, tech, IMG_SERVICE_BUSINESS, "About Alexa","Does anyone own one of these? Is it any good?" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 3, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, webDev, IMG_SERVICE_BUSINESS, "Bootstrap 4","Hey you all. Would it be worth my time to learn Bootstrap 4?" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 4, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, business, IMG_SERVICE_BUSINESS, "Buying gold","I wanna buy some gold. Can someone point me in the right direction" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 5, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, health, IMG_SERVICE_FOOD, "HIIT Training","Forget about running for hours on end. High Intensity Interval Training can give you all the benefits in half the time" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 6, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, tech, IMG_SERVICE_BUSINESS, "Toys that teach Programming","My nephew is showing an interest in programming. Can anyone recommend something for a ten year old?" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 7, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, webDev, IMG_SERVICE_GREY, "Clojure Script","You guys need to try this http://clojure.org, It saved me hours of web dev work" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 8, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, tech, IMG_SERVICE, "Samsung Galaxy 8","This phone is the greatest. Nice screen, good battery life, and tons of apps!" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 9, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, webDev, IMG_SERVICE_GREY, "Spring Framework 5","I hear webFlux is all the rage in Spring Framework. Does anyone have first hand experience?" );
        parent = pb.build();
        parent.setCreated( LocalDateTime.of( 2017, 1, 10, 10,11,12 ) );
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
