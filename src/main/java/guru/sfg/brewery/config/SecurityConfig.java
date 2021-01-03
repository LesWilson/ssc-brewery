package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @Bean
    PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//        return new LdapShaPasswordEncoder();
//        return new StandardPasswordEncoder();
//        return new BCryptPasswordEncoder();
        // Enables use of multiple encoders
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
        // Enables use of multiple encoders
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    /*
      This method achieves same as previous commented out method
      but uses fluent API
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
            .withUser("admin")
            .password("{bcrypt}$2a$10$lRM4npUeTSyUyhndRHY.TO23kpMbcbLC06R39Icx8B8Vj8SsCST1e")  // bcrypt of test
//            .password("44fcc154d52ef4a45637eb05e607889aee9aba5e0975152e85d2075aa52a0fdf16da37849240294f")  // sha256 of test
//            .password("test")  // add {noop} in front of password if not configuring encoder
            .roles("ADMIN")
            .and()
            .withUser("user")
            .password("{ldap}{SSHA}42rKfSk8zc64/H6pVCIGs1k1rwoeTH2AR5q9iA==")
            .roles("USER")
            .and()
            .withUser("scott")
//            .password("$2a$10$1a9/7xF7tOS9EieYyMXm6uGXsW3zaL5hPEy66fi1gDwW5BeYRO0ii")  // bcrypt of tiger
            .password("{bcrypt15}$2a$15$Zk7XOSTpNNfFlCt8buLjcO3knnoPX5efdAlJxafLXtmZ0CrPaeOzi")  // bcrypt15 of tiger
//            .password("{sha256}53b75dd6691244eb5bbacc6295ec9097039d220dc607bdc1eaf59b7bb147e998e69b546d7d955b94")  // sha256 of tiger
//            .password("tiger")
            .roles("CUSTOMER");
    }
}
