package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
            .headers()
            .contentSecurityPolicy("script-src 'self' 'unsafe-inline' data:; style-src 'self' 'unsafe-inline' getbootstrap.com maxcdn.bootstrapcdn.com fonts.googleapis.com; img-src 'self' ssl.gstatic.com www.gstatic.com lh3.googleusercontent.com data:; font-src 'self' ssl.gstatic.com www.gstatic.com fonts.gstatic.com fonts.googleapis.com; frame-src 'self' ; object-src ; ");

//        http
//            .headers()
//            .xssProtection()
//            .block(false);

        http
            .headers()
            .frameOptions()
            .sameOrigin();

        http
            .csrf().disable();

        http
            .authorizeRequests(auth ->
                auth
                    .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                    .antMatchers("/h2-console/**", "/favicon.ico").permitAll()
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

    @Bean
    PasswordEncoder passwordEncoder() {
        // Enables use of multiple encoders
        return SfgPasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}
