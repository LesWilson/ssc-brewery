package guru.sfg.brewery.config;

import guru.sfg.brewery.security.SfgPasswordEncoderFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ADMIN_ROLE = "ADMIN";
    public static final String CUSTOMER_ROLE = "CUSTOMER";
    public static final String USER_ROLE = "USER";
    public static final String[] ALL_ROLES = new String[] {ADMIN_ROLE, USER_ROLE, CUSTOMER_ROLE};

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
