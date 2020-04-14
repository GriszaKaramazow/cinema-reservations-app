package pl.connectis.cinemareservationsapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("develop")
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    public void buildMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Order(1)
    @Test
    public void getClientsByExample_HasAccessWhenUnauthenticated_StatusForbidden() throws Exception {
        mockMvc.perform(get("/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(2)
    @Test
    @WithMockUser(roles = "CLIENT")
    public void getClientsByExample_HasAccessWhenAuthenticatedAsClient_StatusForbidden() throws Exception {
        mockMvc.perform(get("/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/getClientsByExample_GetAllClients.csv", delimiter = ';')
    public void getClientsByExample_HasAccessWhenAuthenticatedAsEmployee_StatusOkAndCorrectResponseBodyReceived(
            String responseBody) throws Exception {
        mockMvc.perform(get("/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(4)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/getClientsByExample_GetClientsByLastName.csv", delimiter = ';')
    public void getClientsByExample_GetClientsByLastName_StatusOkAndCorrectResponseBodyReceived(
            String lastName, String responseBody) throws Exception {
        mockMvc.perform(get("/client?lastName={lastName}", lastName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(5)
    @Test
    public void getLoggedUser_HasAccessWhenUnauthenticated_StatusForbidden() throws Exception {
        mockMvc.perform(get("/myaccount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/user/getLoggedUser_GetClient", delimiter = ';')
    public void getLoggedUser_HasAccessWhenAuthenticatedAsClient_StatusOkAndCorrectResponseBodyReceived(
            String responseBody) throws Exception {
        mockMvc.perform(get("/myaccount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(7)
    @ParameterizedTest
    @WithMockUser(username = "piotr.krakowski@kino.pl", roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/getLoggedUser_GetEmployee", delimiter = ';')
    public void getLoggedUser_HasAccessWhenAuthenticatedAsEmployee_StatusOkAndCorrectResponseBodyReceived(
            String responseBody) throws Exception {
        mockMvc.perform(get("/myaccount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(8)
    @ParameterizedTest
    @CsvFileSource(resources = "/user/addClient.csv", delimiter = ';')
    public void addClient_HasAccessWhenUnauthenticated_StatusCreatedAndCorrectResponseBodyReceived(
            String requestBody, String responseBody) throws Exception {
        mockMvc.perform(post("/signup")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(9)
    @ParameterizedTest
    @CsvFileSource(resources = "/user/addEmployee.csv", delimiter = ';')
    public void addEmployee_HasAccessWhenUnauthenticated_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(post("/register")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(10)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/user/addEmployee.csv", delimiter = ';')
    public void addEmployee_HasAccessWhenAuthenticatedAsClient_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(post("/register")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/addEmployee.csv", delimiter = ';')
    public void addEmployee_HasAccessWhenAuthenticatedAsEmployee_StatusCreatedAndCorrectResponseBodyReceived(
            String requestBody, String responseBody) throws Exception {
        mockMvc.perform(post("/register")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(12)
    @ParameterizedTest
    @CsvFileSource(resources = "/user/updateUser_UpdateClient.csv", delimiter = ';')
    public void updateUser_HasAccessWhenUnauthenticated_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(13)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/user/updateUser_UpdateClient.csv", delimiter = ';')
    public void updateUser_HasAccessWhenAuthenticatedAsClient_StatusOkAndCorrectResponseBodyReceived(
            String requestBody, String responseBody) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(14)
    @ParameterizedTest
    @WithMockUser(username = "piotr.krakowski@kino.pl", roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/updateUser_UpdateEmployee.csv", delimiter = ';')
    public void updateUser_HasAccessWhenAuthenticatedAsEmployee_StatusOkAndCorrectResponseBodyReceived(
            String requestBody, String responseBody) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(16)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/user/updateUser_InappropriateUsername.csv", delimiter = ';')
    public void updateUser_InappropriateUsername_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}