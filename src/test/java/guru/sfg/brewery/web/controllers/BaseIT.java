package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.stream.Stream;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest
public class BaseIT {

    protected MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    public static Stream<Arguments> getAllUsers() {
        return Stream.of(
                Arguments.of("user", "password"),
                Arguments.of("scott", "tiger"),
                Arguments.of("admin", "test"));
    }

    public static Stream<Arguments> getAdminCustomerUsers() {
        return Stream.of(
            Arguments.of("scott", "tiger"),
            Arguments.of("admin", "test"));
    }

    public static Stream<Arguments> getAdminUsers() {
        return Stream.of(
            Arguments.of("admin", "test"));
    }

    public static Stream<Arguments> getNonAdminUsers() {
        return Stream.of(
            Arguments.of("user", "password"),
            Arguments.of("scott", "tiger"));
    }

}
