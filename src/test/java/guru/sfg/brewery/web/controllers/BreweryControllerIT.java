package guru.sfg.brewery.web.controllers;

import org.junit.jupiter.api.Test;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class BreweryControllerIT extends BaseIT {

    @Test
    void getBreweriesJsonWithAdmin() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
            .with(httpBasic("admin", "test")))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getBreweriesJsonWithCustomer() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
            .with(httpBasic("scott", "tiger")))
            .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getBreweriesJsonWithUser() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries")
            .with(httpBasic("user", "password")))
            .andExpect(status().isForbidden());
    }

    @Test
    void getBreweriesJsonNoLoggedInUser() throws Exception {
        mockMvc.perform(get("/brewery/api/v1/breweries"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void listBreweriesWithCustomer() throws Exception{
        mockMvc.perform(get("/brewery/breweries")
            .with(httpBasic("scott", "tiger")))
            .andExpect(status().isOk())
            .andExpect(view().name("breweries/index"))
            .andExpect(model().attributeExists("breweries"));
    }
    @Test
    void listBreweriesWithUser() throws Exception{
        mockMvc.perform(get("/brewery/breweries")
            .with(httpBasic("user", "password")))
            .andExpect(status().isForbidden());
    }
    @Test
    void listBreweriesWithAdmin() throws Exception{
        mockMvc.perform(get("/brewery/breweries")
            .with(httpBasic("admin", "test")))
            .andExpect(status().is2xxSuccessful());
    }
    @Test
    void listBreweriesWithNoUser() throws Exception{
        mockMvc.perform(get("/brewery/breweries"))
            .andExpect(status().isUnauthorized());
    }

}
