package com.blogen.config;

import org.springframework.context.annotation.Configuration;


/**
 * Configure some of the MVC defaults used by this project.
 * NOTE: this is all handled by Spring Boot's default MVC configuration.
 *
 * @author Cliff
 */
@Configuration
public class MvcConfig { //extends WebMvcConfigurerAdapter {


//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
//        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
//        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/");
//        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
//    }

//    @Bean("simpleMappingExceptionResolver")
//    public SimpleMappingExceptionResolver createSimpleMappingExceptionResolver() {
//        SimpleMappingExceptionResolver smer = new SimpleMappingExceptionResolver();
//        Properties props = new Properties(  );
//        props.setProperty( "AccessDeniedException","403error" );
//
//        smer.setExceptionMappings( props );
//        smer.setDefaultErrorView( "error" );
//        smer.setExceptionAttribute( "exception" );
//        smer.setWarnLogCategory( "com.blogen" );
//        return smer;
//    }

    //    @Bean
//    public ViewResolver viewResolver() {
//        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
//        resolver.setTemplateEngine(templateEngine());
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setContentType("text/html");
//        resolver.setOrder( Ordered.LOWEST_PRECEDENCE - 5 );
//        return resolver;
//    }
//
//    @Bean
//    public TemplateEngine templateEngine() {
//        SpringTemplateEngine engine = new SpringTemplateEngine();
//        
//        Set<IDialect> dialects = new HashSet<>();
//        dialects.add( new SpringSecurityDialect() );
//        engine.setAdditionalDialects( dialects );
//
//        engine.setEnableSpringELCompiler(true);
//        engine.setTemplateResolver( templateResolver() );
//        return engine;
//    }
//
//    private ITemplateResolver templateResolver() {
//        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
//        resolver.setApplicationContext( applicationContext );
//        resolver.setPrefix("classpath:/templates/");
//        resolver.setSuffix(".html");
//        resolver.setTemplateMode( TemplateMode.HTML );
//        resolver.setCharacterEncoding("UTF-8");
//        resolver.setCacheable( true );
//        return resolver;
//    }
}
