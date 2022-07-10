package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.web.model.BeerDto;
import guru.springframework.msscbrewery.web.services.BeerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {BeerController.class})
@AutoConfigureMockMvc
class BeerControllerTest {

    public static final String BEER_NAME = "YOPS";
    public static final String BEER_STYLE = "IPA";
    public static final String BASE_API_URL = "/api/v1/beer";
    @Autowired
    MockMvc mockMvc;

    @MockBean
    BeerService beerService;

    @Autowired
    ObjectMapper objectMapper;

    BeerDto validBeer;

    @BeforeEach
    void beforeAll() {
        validBeer = BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName(BEER_NAME)
                .beerStyle(BEER_STYLE)
                .build();
    }

    @Test
    void getBeer() throws Exception {
        given(beerService.getBeerById(any(UUID.class))).willReturn(validBeer);

        mockMvc.perform(get(BASE_API_URL + "/{beerId}", validBeer.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.id", is(validBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(BEER_NAME)));
    }

    @Test
    void handlePost() throws Exception {
        BeerDto beerDto = validBeer;
        beerDto.setId(null);
        BeerDto saved = BeerDto.builder()
                .id(UUID.randomUUID())
                .beerName("New Beer")
                .build();

        given(beerService.saveNewBeer(any())).willReturn(saved);

        mockMvc.perform(post(BASE_API_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validBeer)))
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, BASE_API_URL + "/" + saved.getId().toString()));
    }

    @Test
    void handleUpdate() throws Exception {
        //when
        BeerDto beerDto = validBeer;
        beerDto.setId(null);
        mockMvc.perform(put(BASE_API_URL + "/" + UUID.randomUUID())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(beerDto)))
                .andExpect(status().isNoContent());
        //then
        then(beerService).should().updateBeer(any(), any());
    }

    @Test
    void deleteBeer() {
    }
}