/*
 *  Copyright 2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package guru.sfg.brewery.bootstrap;

import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.Role;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.RoleRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static guru.sfg.brewery.config.SecurityConfig.*;
import static java.lang.String.format;


/**
 * Created by jt on 2019-01-26.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class DefaultSecurityLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    @Override
    public void run(String... args) {
        loadData();
    }

    private void loadData() {
        if (authorityRepository.count() == 0) {
            log.debug("********* Loading user and authority data");
            // Beer CRUD Authorities
            Authority findBeer = authorityRepository.save(Authority.builder().authority("beer_read").build());
            Authority createBeer = authorityRepository.save(Authority.builder().authority("beer_create").build());
            Authority updateBeer = authorityRepository.save(Authority.builder().authority("beer_update").build());
            Authority deleteBeer = authorityRepository.save(Authority.builder().authority("beer_delete").build());

            Authority findBrewery = authorityRepository.save(Authority.builder().authority("brewery_read").build());
            Authority createBrewery = authorityRepository.save(Authority.builder().authority("brewery_create").build());
            Authority updateBrewery = authorityRepository.save(Authority.builder().authority("brewery_update").build());
            Authority deleteBrewery = authorityRepository.save(Authority.builder().authority("brewery_delete").build());

            Authority findCustomer = authorityRepository.save(Authority.builder().authority("customer_read").build());
            Authority createCustomer = authorityRepository.save(Authority.builder().authority("customer_create").build());
            Authority updateCustomer = authorityRepository.save(Authority.builder().authority("customer_update").build());
            Authority deleteCustomer = authorityRepository.save(Authority.builder().authority("customer_delete").build());

            Role adminRole = roleRepository.save(Role.builder()
                .name(format("ROLE_%s", ADMIN_ROLE))
                .build());
            Role userRole = roleRepository.save(Role.builder()
                .name(format("ROLE_%s", USER_ROLE))
                .build());
            Role customerRole = roleRepository.save(Role.builder()
                .name(format("ROLE_%s", CUSTOMER_ROLE))
                .build());

            adminRole.setAuthorities(Set.of(findBeer, createBeer, updateBeer, deleteBeer, findBrewery, createBrewery, updateBrewery, deleteBrewery, findCustomer, createCustomer, updateCustomer, deleteCustomer));
            userRole.setAuthorities(Set.of(findBeer));
            customerRole.setAuthorities(Set.of(findBeer, findBrewery, findCustomer));

            User adminUser = User.builder().password(encoder.encode("test")).username("admin").role(adminRole).build();
            userRepository.save(adminUser);

            User normalUser = User.builder().password(encoder.encode("password")).username("user").role(userRole).build();
            userRepository.save(normalUser);

            User scott = User.builder().password(encoder.encode("tiger")).username("scott").role(customerRole).build();
            userRepository.save(scott);

            scott.getAuthorities().forEach(System.out::println);
            log.debug("********* Data loaded");
        }
    }
}
