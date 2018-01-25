package com.blogen.bootstrap;

import com.blogen.domain.*;
import com.blogen.repositories.CategoryRepository;
import com.blogen.repositories.PostRepository;
import com.blogen.repositories.UserPrefsRepository;
import com.blogen.services.RoleService;
import com.blogen.services.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
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
    private static final String IMG_SERVICE_CITY = "http://lorempixel.com/400/200/city";
    private static final String IMG_SERVICE_ABSTRACT = "http://lorempixel.com/400/200/abstract";
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
        //
        //retrieve the roles we created
        adminRole = roleService.getByName( "ADMIN" );
        userRole = roleService.getByName( "USER" );

        //administrator
        UserBuilder ub = new UserBuilder( "admin","Carl","Sagan","admin@blogen.org","adminpassword",upAdmin );
        User admin = ub.build();
        admin.addRole( adminRole );
        admin.addRole( userRole );
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
        PostBuilder pb = new PostBuilder( john, tech, IMG_SERVICE,"Love this tech", "Smart-phones are the greatest invention in the history of mankind",LocalDateTime.of( 2017, 1, 1, 10,11,12 ) );
        Post parent = pb.build();
        //postRepository.save( parent );
        pb.addChildPost( john,"Love it too", "I wish I could embed the phone into my head", IMG_SERVICE_NATURE, LocalDateTime.of( 2017, 1, 1, 10,12,12 ) );
        pb.addChildPost( maggie,"Not so fast", "Are they even greater than the Internet?", IMG_SERVICE_FOOD, LocalDateTime.of( 2017, 1, 1, 10,13,12 ));
        pb.addChildPost( william,"Here today gone tomorrow","They're the greatest for now, but something better will come along", IMG_SERVICE_GREY,LocalDateTime.of( 2017, 1, 1, 10,14,12 ) );
        pb.addChildPost( william,"No No No","the greatest invention is velcro :)", IMG_SERVICE,LocalDateTime.of( 2017, 1, 1, 10,15,12 ) );
        postRepository.save( parent );

        //
        //build posts for william - 2 parent posts
        pb = new PostBuilder( william, health,IMG_SERVICE_FOOD,"Started lifting today","Trying to burn off these holiday calories. I hear resistence training is better than running",LocalDateTime.of( 2017, 2, 1, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( william, business, "http://lorempixel.com/400/200/business/1","Bulls are on parade", "They stock markets won't stop running higher. When will the bubble burst?",LocalDateTime.of( 2017, 3, 1, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        //
        //build posts for maggie - 3 parent posts with 2 child posts each
        pb = new PostBuilder( maggie, business,"http://lorempixel.com/400/200/business/1","Bitcoin or bust", "Forget about gold, I'm all in on bitcoin",LocalDateTime.of( 2017, 4, 1, 10,11,12 ) );
        parent = pb.build();
        pb.addChildPost( john,"probably buying it", "I'm game too. I just don't know where to but it from", IMG_SERVICE_NATURE,LocalDateTime.of( 2017, 4, 2, 10,11,12 ) );
        pb.addChildPost( maggie,"beware the bubble", "If we've waited this long, I fear it's already too late", IMG_SERVICE_BUSINESS,LocalDateTime.of( 2017, 4, 3, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( maggie,health,"http://lorempixel.com/400/200/sports/3","What ever happened to Jazzercise?","It used to be all the rage, now I can't find a single gym that offers it",LocalDateTime.of( 2017, 5, 1, 10,11,12 ) );
        parent = pb.build();
        pb.addChildPost( william,"sounds gross", "Jazzercise? Really? What do you do, listen to jazz until you pass out?", IMG_SERVICE_FOOD,LocalDateTime.of( 2017, 5, 2, 10,11,12 ) );
        pb.addChildPost( william, "on second thought...","I changed my mind. It looks kinda fun. Someone get me leg warmers", IMG_SERVICE_GREY,LocalDateTime.of( 2017, 5, 3, 10,11,12 ) );
        postRepository.save( parent );

        pb = new PostBuilder( maggie, webDev,"http://lorempixel.com/400/200/technics/3","Is PHP dead?","Does anyone have stats on PHP usage in the wild?",LocalDateTime.of( 2017, 6, 1, 10,11,12 ) );
        parent = pb.build();
        pb.addChildPost( william,"I doubt it", "PHP is everywhere. I'm pretty sure it still powers the internet!", IMG_SERVICE_BUSINESS,LocalDateTime.of( 2017, 6, 2, 10,11,12 ) );
        pb.addChildPost( admin, "We don't use it here","...anymore. We switched to Kotlin/React, but a lot of companies are still powered by PHP", IMG_SERVICE_ABSTRACT,LocalDateTime.of( 2017, 6, 3, 10,11,12 ) );
        postRepository.save( parent );

        //
        //Build Posts for elizabeth (10 parent posts for her)
        pb = new PostBuilder( elizabeth, business, IMG_SERVICE, "Invest now","Market returns are crazy, there is still time to jump on in",LocalDateTime.of( 2017, 1, 1, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, health, IMG_SERVICE_GREY, "Proper Diet trumps all","No matter what excercise you do, just remember you can never out-train a poor diet",LocalDateTime.of( 2017, 1, 2, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, tech, IMG_SERVICE_BUSINESS, "About Alexa","Does anyone own one of these? Is it any good?",LocalDateTime.of( 2017, 1, 3, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, webDev, IMG_SERVICE_BUSINESS, "Bootstrap 4","Hey you all. Would it be worth my time to learn Bootstrap 4?",LocalDateTime.of( 2017, 1, 4, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, business, IMG_SERVICE_BUSINESS, "Buying gold","I wanna buy some gold. Can someone point me in the right direction",LocalDateTime.of( 2017, 1, 5, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, health, IMG_SERVICE_FOOD, "HIIT Training","Forget about running for hours on end. High Intensity Interval Training can give you all the benefits in half the time",LocalDateTime.of( 2017, 1, 6, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, tech, IMG_SERVICE_BUSINESS, "Toys that teach Programming","My nephew is showing an interest in programming. Can anyone recommend something for a ten year old?",LocalDateTime.of( 2017, 1, 7, 10,11,12 )  );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, webDev, IMG_SERVICE_GREY, "Clojure Script","You guys need to try this http://clojure.org, It saved me hours of web dev work", LocalDateTime.of( 2017, 1, 8, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, tech, IMG_SERVICE, "Samsung Galaxy 8","This phone is the greatest. Nice screen, good battery life, and tons of apps!",LocalDateTime.of( 2017, 1, 9, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );

        pb = new PostBuilder( elizabeth, webDev, IMG_SERVICE_GREY, "Spring Framework 5","I hear webFlux is all the rage in Spring Framework. Does anyone have first hand experience?",LocalDateTime.of( 2017, 1, 10, 10,11,12 ) );
        parent = pb.build();
        postRepository.save( parent );


        //there should now be 26 posts in total - sixteen parent posts and ten child posts
    }

    @Transactional
    @Override
    public void onApplicationEvent( ContextRefreshedEvent event ) {
        initData();
        log.info("Finished bootstrapping data");
    }
}
