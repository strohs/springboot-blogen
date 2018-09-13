package com.blogen.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Spring Security Configuration
 *
 * @author Cliff
 */
@Configuration
@EnableGlobalMethodSecurity( prePostEnabled = true )
public class SpringSecConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("daoAuthenticationProvider")
    private AuthenticationProvider authenticationProvider;

// setter injection was causing random NPEs when a user tried to login
//    @Autowired
//    @Qualifier("daoAuthenticationProvider")
//    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
//        this.authenticationProvider = authenticationProvider;
//    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        //This is new as of Spring Security 5, seems to use bcrypt as the default password encoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider( UserDetailsService userDetailsService ){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder( passwordEncoder() );
        daoAuthenticationProvider.setUserDetailsService( userDetailsService );
        return daoAuthenticationProvider;
    }



    @Autowired
    public void configureAuthManager(AuthenticationManagerBuilder authenticationManagerBuilder){
        authenticationManagerBuilder.authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure( WebSecurity web) throws Exception {
        //this will allow swagger UI and h2-console through spring-security
        web.ignoring().antMatchers("/v2/api-docs", "/swagger-resources/configuration/ui", "/swagger-resources",
                "/swagger-resources/configuration/security", "/swagger-ui.html", "/webjars/**", "/console/*",
                "/h2-console/**", "/actuator/**");
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeRequests()
                    .antMatchers( "/admin/**" )
                    .hasAuthority( "ADMIN" )
                .and()
                .authorizeRequests()
                    .antMatchers(
                            "/",
                            "/users/**",
                            "/api/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
                .and()
                .exceptionHandling().accessDeniedPage( "/error/403" );

        httpSecurity.csrf().disable();
        httpSecurity.headers().frameOptions().disable();
    }

//if using in-memory configuration
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("admin").password("admin").roles("ADMIN")
//                .and().withUser("user").password("user").roles("USER");;
//    }
}
