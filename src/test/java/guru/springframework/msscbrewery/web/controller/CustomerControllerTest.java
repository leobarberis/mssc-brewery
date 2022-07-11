package guru.springframework.msscbrewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.msscbrewery.web.model.CustomerDto;
import guru.springframework.msscbrewery.services.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
@AutoConfigureMockMvc
class CustomerControllerTest {

    public static final String CUSTOMER_DTO_NAME = "NewCustomerDTO";
    public static final String INVALID_CUSTOMER_DTO_NAME = "NO";
    public static final UUID CUSTOMER_DTO_ID = UUID.randomUUID();
    public static final String BASE_URL = "/api/v1/customer";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @Autowired
    ObjectMapper mapper;

    CustomerDto customerDto;

    @BeforeEach
    void beforeEach() {
        customerDto = CustomerDto.builder()
                .customerId(CUSTOMER_DTO_ID)
                .name(CUSTOMER_DTO_NAME)
                .build();
    }

    @Test
    @DisplayName("POST /api/v1/customer SUCCESS")
    void handlePost() throws Exception {

        CustomerDto saved = CustomerDto.builder()
                .customerId(UUID.randomUUID())
                .name("NewName")
                .build();

        given(customerService.saveCustomer(any())).willReturn(saved);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(customerDto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("POST /api/v1/customer INVALID_NAME")
    void handlePostWhenCustomerNameIsInvalid() throws Exception {
        CustomerDto invalidDTO = customerDto;
        invalidDTO.setName(INVALID_CUSTOMER_DTO_NAME);

        given(customerService.saveCustomer(any())).willReturn(customerDto);

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andReturn();

    }
}