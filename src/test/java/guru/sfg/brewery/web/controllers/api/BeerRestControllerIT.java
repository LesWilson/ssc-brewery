package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.bootstrap.DefaultBreweryLoader;
import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BeerRestControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @DisplayName("Find Beers")
    @Nested
    class FindBeers {

        @Test
        void withNoLoggedInUser() throws Exception {
            mockMvc.perform(get("/api/v1/beer/"))
                .andExpect(status().isUnauthorized());
        }
        @Test
        void withBadCredentials() throws Exception {
            mockMvc.perform(get("/api/v1/beer/")
                .with(httpBasic("user1", "password")))
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getAllUsers")
        public void withLoggedInUser(String user, String password) throws Exception {
            mockMvc.perform(get("/api/v1/beer/")
                .with(httpBasic(user, password)))
                .andExpect(status().is2xxSuccessful());
        }
    }

    @DisplayName("Get Beer By Id")
    @Nested
    class GetBeerById {

        @Test
        @DisplayName("No Logged In User")
        void withNoLoggedInUser() throws Exception {
            mockMvc.perform(get("/api/v1/beer/12345"))
                .andExpect(status().isUnauthorized());
        }
        @Test
        @DisplayName("Bad Credentials")
        void withBadCredentials() throws Exception {
            mockMvc.perform(get("/api/v1/beer/23456")
                .with(httpBasic("user1", "password")))
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getAllUsers")
        @DisplayName("Authorised User")
        public void withLoggedInUser(String user, String password) throws Exception {
            Beer beer = beerRepository.findAll().get(0);
            mockMvc.perform(get("/api/v1/beer/" + beer.getId())
                .with(httpBasic(user, password)))
                .andExpect(status().is2xxSuccessful());
        }
    }

    @DisplayName("Get Beer By UPC")
    @Nested
    class GetBeerByUpc {

        @Test
        @DisplayName("No Logged In User")
        void withNoLoggedInUser() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/"+ DefaultBreweryLoader.BEER_1_UPC))
                .andExpect(status().isUnauthorized());
        }
        @Test
        @DisplayName("Bad Credentials")
        void withBadCredentials() throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/"+ DefaultBreweryLoader.BEER_1_UPC)
                .with(httpBasic("user1", "password")))
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getAllUsers")
        @DisplayName("Authorised User")
        public void withLoggedInUser(String user, String password) throws Exception {
            mockMvc.perform(get("/api/v1/beerUpc/" + DefaultBreweryLoader.BEER_1_UPC)
                .with(httpBasic(user, password)))
                .andExpect(status().is2xxSuccessful());
        }
    }

    @Test
    void saveNewBeer() {
    }

    @Test
    void updateBeer() {
    }

    @DisplayName("Delete Beer")
    @Nested
    class DeleteBeer {

        @Test
        @DisplayName("No Logged In User")
        void withNoLoggedInUser() throws Exception {
            mockMvc.perform(
                delete("/api/v1/beer/" + UUID.randomUUID()))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Bad Credentials")
        void withBadCredentials() throws Exception {
            mockMvc.perform(
                delete("/api/v1/beer/" + UUID.randomUUID())
                    .with(httpBasic("spring", "test")))
                .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Admin User")
        void withAdmin() throws Exception {
            Beer beer = beerRepository.saveAndFlush(Beer.builder().beerName("To Be Deleted").build());
            mockMvc.perform(
                delete("/api/v1/beer/" + beer.getId())
                    .with(httpBasic("admin", "test")))
                .andExpect(status().is2xxSuccessful());
        }

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.api.BeerRestControllerIT#getNonAdminUsers")
        @DisplayName("Non Admin logged in user")
        void withNonAdminRoles(String user, String password) throws Exception {
            mockMvc.perform(
                delete("/api/v1/beer/" + UUID.randomUUID())
                    .with(httpBasic(user, password)))
                .andExpect(status().isForbidden());
        }
    }
}