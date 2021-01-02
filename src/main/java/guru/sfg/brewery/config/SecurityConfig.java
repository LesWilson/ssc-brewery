package guru.sfg.brewery.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(auth ->
                auth
                    .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                    .antMatchers("/beers/find", "/beers").permitAll()
                    .antMatchers(GET, "/api/v1/beer/**").permitAll()
                    .mvcMatchers(GET, "/api/v1/beerUpc/{upc}").permitAll()
            )
            .authorizeRequests()
            .anyRequest()
            .authenticated()
            .and()
            .formLogin()
            .and()
            .httpBasic();

    }

//    @Override
//    @Bean
//    protected UserDetailsService userDetailsService() {
//        UserDetails admin = User.withDefaultPasswordEncoder()
//            .username("admin")
//            .password("test")
//            .roles("ADMIN")
//            .build();
//        UserDetails user = User.withDefaultPasswordEncoder()
//            .username("user")
//            .password("password")
//            .roles("USER")
//            .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }


    @Override
    /*
      This method achieves same as previous commented out method
      but uses fluent API
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .passwordEncoder(NoOpPasswordEncoder.getInstance())
            .withUser("admin")
            .password("test")  // add {noop} in front of password if not configuring encode above
            .roles("ADMIN")
            .and()
            .withUser("user")
            .password("password")
            .roles("USER")
            .and()
            .withUser("scott")
            .password("tiger")
            .roles("CUSTOMER");
    }
}
