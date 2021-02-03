package guru.sfg.brewery.web.controllers;

import guru.sfg.brewery.domain.Beer;
import guru.sfg.brewery.repositories.BeerRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BeerControllerIT extends BaseIT {

    @Autowired
    BeerRepository beerRepository;

    @DisplayName("Init New Form")
    @Nested
    class InitForm {

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getAdminUsers")
        void initCreationFormAuthorised(String user, String password) throws Exception {
            mockMvc.perform(get("/beers/new").with(httpBasic(user, password)))
                .andExpect(status().isOk())
                .andExpect(view().name("beers/createBeer"))
                .andExpect(model().attributeExists("beer"));
        }

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getNonAdminUsers")
        void initCreationFormNotAuthorised(String user, String password) throws Exception {
            mockMvc.perform(get("/beers/new").with(httpBasic(user, password)))
                .andExpect(status().isForbidden());
        }

        @Test
        void initCreateBeerNotLoggedIn() throws Exception {
            mockMvc.perform(get("/beers/new"))
                .andExpect(status().isUnauthorized());
        }
    }
    @DisplayName("Process Find Beer Form")
    @Nested
    class ProcessFindForm{
        @Test
        void findBeerForm() throws Exception {
            mockMvc.perform(get("/beers").param("beerName", ""))
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.BeerControllerIT#getAllUsers")
        void findBeerFormAuth(String user, String pwd) throws Exception {
            mockMvc.perform(get("/beers").param("beerName", "")
                .with(httpBasic(user, pwd)))
                .andExpect(status().isOk());
        }
    }


    @DisplayName("Find Beers")
    @Nested
    class FindBeers {

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getAllUsers")
        @DisplayName("Authorised User")
        public void withLoggedInUser(String user, String password) throws Exception {
            mockMvc.perform(get("/beers/find")
                .with(httpBasic(user, password)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("beers/findBeers"))
                .andExpect(model().attributeExists("beer"));
        }

        @Test
        @DisplayName("No Logged In User")
        void withNoUser() throws Exception {
            mockMvc.perform(get("/beers/find"))
                .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Get Beer By Id")
    @Nested
    class GetBeerById {

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getAllUsers")
        @DisplayName("Authorised User")
        public void withLoggedInUser(String user, String password) throws Exception {
            Beer beer = beerRepository.findAll().get(0);
            mockMvc.perform(get("/beers/"+beer.getId())
                .with(httpBasic(user, password)))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("beers/beerDetails"))
                .andExpect(model().attributeExists("beer"));
        }

        @Test
        @DisplayName("No Logged In User")
        void withNoUser() throws Exception{
            mockMvc.perform(get("/beers/anyOldId"))
                .andExpect(status().isUnauthorized());
        }
    }
}
