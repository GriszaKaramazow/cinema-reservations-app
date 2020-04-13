package pl.connectis.cinemareservationsapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("develop")
public class UserControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeAll
    public void buildMockMvc() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Order(1)
    @Test
    @CsvFileSource(resources = "/user/getClientByExampleAll.csv", delimiter = ';')
    public void getClientByExampleAll_Unauthenticated() throws Exception {
        mockMvc.perform(get("/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(2)
    @Test
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/user/getClientByExampleAll.csv", delimiter = ';')
    public void getClientAll_AuthenticatedAsClient() throws Exception {
        mockMvc.perform(get("/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/getClientByExampleAll.csv", delimiter = ';')
    public void getClientAll_AuthenticatedAsEmployee(String response) throws Exception {
        mockMvc.perform(get("/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(4)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/getClientByLastName.csv", delimiter = ';')
    public void getClientByLastName_AuthenticatedAsEmployee(String lastName, String response) throws Exception {
        mockMvc.perform(get("/client?lastName={lastName}", lastName)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(5)
    @Test
    @CsvFileSource(resources = "/user/getLoggedClient.csv", delimiter = ';')
    public void getLoggedUser_Unauthenticated() throws Exception {
        mockMvc.perform(get("/myaccount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/user/getLoggedClient.csv", delimiter = ';')
    public void getLoggedUser_AuthenticatedAsClient(String response) throws Exception {
        mockMvc.perform(get("/myaccount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(7)
    @ParameterizedTest
    @WithMockUser(username = "piotr.krakowski@kino.pl", roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/getLoggedEmployee.csv", delimiter = ';')
    public void getLoggedUser_AuthenticatedAsEmployee(String response) throws Exception {
        mockMvc.perform(get("/myaccount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(8)
    @ParameterizedTest
    @CsvFileSource(resources = "/user/addClient.csv", delimiter = ';')
    public void addClient_Unauthenticated(String request, String response) throws Exception {
        mockMvc.perform(post("/signup")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(9)
    @ParameterizedTest
    @CsvFileSource(resources = "/user/addEmployee.csv", delimiter = ';')
    public void addEmployee_Unauthenticated(String request) throws Exception {
        mockMvc.perform(post("/register")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(10)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/user/addEmployee.csv", delimiter = ';')
    public void addEmployee_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/register")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/addEmployee.csv", delimiter = ';')
    public void addEmployee(String request, String response) throws Exception {
        mockMvc.perform(post("/register")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(12)
    @ParameterizedTest
    @CsvFileSource(resources = "/user/updateClient.csv", delimiter = ';')
    public void updateUser(String request) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(13)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/user/updateClient.csv", delimiter = ';')
    public void updateClient(String request, String response) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(14)
    @ParameterizedTest
    @WithMockUser(username = "piotr.krakowski@kino.pl", roles = "EMPLOYEE")
    @CsvFileSource(resources = "/user/updateEmployee.csv", delimiter = ';')
    public void updateEmployee(String request, String response) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(16)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/user/updateClientInappropriateUsername.csv", delimiter = ';')
    public void updateClientInappropriateUsername(String request) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}
