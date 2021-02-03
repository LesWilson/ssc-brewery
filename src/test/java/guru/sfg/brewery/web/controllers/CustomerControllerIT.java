package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.Rollback;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerIT extends BaseIT {

    @DisplayName("Init New Customer Form")
    @Nested
    class InitForm {

        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.CustomerControllerIT#getNonAdminUsers")
        @DisplayName("With Logged In User Without Correct Role")
        public void initCreationForm(String user, String password) throws Exception {
            mockMvc.perform(get("/customers/new")
                .with(httpBasic(user, password)))
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("With Logged In User With Correct Role")
        void initCreationFormWrongUserRole() throws Exception {
            mockMvc.perform(get("/customers/new")
                .with(httpBasic("admin", "test")))
                .andExpect(status().isOk())
                .andExpect(view().name("customers/createCustomer"))
                .andExpect(model().attributeExists("customer"));
        }

        @Test
        @DisplayName("With No Logged In User")
        void initCreationFormNotAuth() throws Exception {
            mockMvc.perform(get("/customers/new"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Add New Customer")
    @Nested
    class AddCustomer {

        @Rollback
        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
        @MethodSource(value = "guru.sfg.brewery.web.controllers.CustomerControllerIT#getNonAdminUsers")
        @DisplayName("With Logged In User Without Correct Role")
        public void addCustomerUnauthorisedRoles(String user, String password) throws Exception {
            mockMvc.perform(post("/customers/new")
                .param("customerName", "New Name")
                .with(httpBasic(user, password)))
                .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("With Logged In User With Correct Role")
        void addCustomerWrongUserRole() throws Exception {
            mockMvc.perform(post("/customers/new")
                .param("customerName", "New Name")
                .with(httpBasic("admin", "test")))
                .andExpect(status().is3xxRedirection());
        }

        @Test
        @DisplayName("With No Logged In User")
        void addUserNotAuth() throws Exception {
            mockMvc.perform(post("/customers/new")
                .param("customerName", "New Name"))
                .andExpect(status().isUnauthorized());
        }
    }

    @DisplayName("Process Find Customer Form")
    @Nested
    class ProcessFindForm{
        @Test
        @DisplayName("With No Logged In User")
        void noLoggedInUser() throws Exception {
            mockMvc.perform(get("/customers"))
                .andExpect(status().isUnauthorized());
        }

        @ParameterizedTest(name = "#{index} with [{arguments}]")
        @MethodSource("guru.sfg.brewery.web.controllers.CustomerControllerIT#getAdminCustomerUsers")
        @DisplayName("With Logged In User With Correct Role")
        void findCustomerWithAllowedUser(String user, String pwd) throws Exception {
            mockMvc.perform(get("/customers")
                .with(httpBasic(user, pwd)))
                .andExpect(status().isOk());
        }

        @Test
        @DisplayName("With Logged In User Without Correct Role")
        void findCustomerWithWrongUserRole() throws Exception {
            mockMvc.perform(get("/customers")
                .with(httpBasic("user", "password")))
                .andExpect(status().isForbidden());
        }
    }


//    @DisplayName("Find Beers")
//    @Nested
//    class FindBeers {
//
//        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
//        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getAllUsers")
//        @DisplayName("Authorised User")
//        public void withLoggedInUser(String user, String password) throws Exception {
//            mockMvc.perform(get("/beers/find")
//                .with(httpBasic(user, password)))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(view().name("beers/findBeers"))
//                .andExpect(model().attributeExists("beer"));
//        }
//
//        @Test
//        @DisplayName("No Logged In User")
//        void withNoUser() throws Exception {
//            mockMvc.perform(get("/beers/find"))
//                .andExpect(status().isUnauthorized());
//        }
//    }
//
//    @DisplayName("Get Beer By Id")
//    @Nested
//    class GetBeerById {
//
//        @ParameterizedTest(name = "#{index} with {argumentsWithNames}")
//        @MethodSource(value = "guru.sfg.brewery.web.controllers.BeerControllerIT#getAllUsers")
//        @DisplayName("Authorised User")
//        public void withLoggedInUser(String user, String password) throws Exception {
//            Beer beer = beerRepository.findAll().get(0);
//            mockMvc.perform(get("/beers/"+beer.getId())
//                .with(httpBasic(user, password)))
//                .andExpect(status().is2xxSuccessful())
//                .andExpect(view().name("beers/beerDetails"))
//                .andExpect(model().attributeExists("beer"));
//        }
//
//        @Test
//        @DisplayName("No Logged In User")
//        void withNoUser() throws Exception{
//            mockMvc.perform(get("/beers/anyOldId"))
//                .andExpect(status().isUnauthorized());
//        }
//    }
}
