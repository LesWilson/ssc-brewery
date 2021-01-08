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

import guru.sfg.brewery.domain.*;
import guru.sfg.brewery.domain.security.Authority;
import guru.sfg.brewery.domain.security.User;
import guru.sfg.brewery.repositories.*;
import guru.sfg.brewery.repositories.security.AuthorityRepository;
import guru.sfg.brewery.repositories.security.UserRepository;
import guru.sfg.brewery.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;


/**
 * Created by jt on 2019-01-26.
 */
@RequiredArgsConstructor
@Slf4j
@Component
public class DefaultSecurityLoader implements CommandLineRunner {

    private final AuthorityRepository authorityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        loadData();
    }

    private void loadData() {
        if (authorityRepository.count() == 0) {
            log.debug("********* Loading user and authority data");
            Authority userAuthority = authorityRepository.save(Authority.builder().role("USER").build());
            Authority adminAuthority = authorityRepository.save(Authority.builder().role("ADMIN").build());
            Authority customerAuthority = authorityRepository.save(Authority.builder().role("CUSTOMER").build());

            User adminUser = User.builder().password(encoder.encode("test")).username("admin").authority(adminAuthority).build();
            userRepository.save(adminUser);

            User normalUser = User.builder().password(encoder.encode("password")).username("user").authority(userAuthority).build();
            userRepository.save(normalUser);

            User scott = User.builder().password(encoder.encode("tiger")).username("scott").authority(customerAuthority).build();
            userRepository.save(scott);
            log.debug("********* Data loaded");
        }
    }
}
