package guru.sfg.brewery.web.controllers.api;

import guru.sfg.brewery.bootstrap.DefaultBreweryLoader;
import guru.sfg.brewery.web.controllers.BaseIT;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BeerRestControllerIT extends BaseIT {

    @Test
    void listBeers() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"))
            .andExpect(status().isOk());
    }

    @Test
    void getBeerById() throws Exception {
        mockMvc.perform(get("/api/v1/beer/"+ UUID.randomUUID()))
            .andExpect(status().isOk());
    }

    @Test
    void getBeerByUpc() throws Exception {
        mockMvc.perform(get("/api/v1/beerUpc/"+ DefaultBreweryLoader.BEER_1_UPC))
            .andExpect(status().isOk());
    }

    @Test
    void saveNewBeer() {
    }

    @Test
    void updateBeer() {
    }

    @Test
    void deleteBeerBadCreds() throws Exception {
        mockMvc.perform(
            delete("/api/v1/beer/"+ UUID.randomUUID())
                .header("Api-Key", "spring")
                .header("Api-Secret", "guru"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void deleteBeerHttpBasic() throws Exception {
        mockMvc.perform(
            delete("/api/v1/beer/"+ UUID.randomUUID())
                .with(httpBasic("admin", "test")))
            .andExpect(status().is2xxSuccessful());
    }
    @Test
    void deleteBeerNoAuth() throws Exception {
        mockMvc.perform(
            delete("/api/v1/beer/"+ UUID.randomUUID()))
            .andExpect(status().isUnauthorized());
    }
}