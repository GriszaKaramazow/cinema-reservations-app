package pl.connectis.cinemareservationsapp;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Properties;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeAll
    public static void setSpringProfile() {
        Properties properties = System.getProperties();
        properties.setProperty("spring.profiles.active", "develop");
    }

    @AfterAll
    public static void resetSpringProfile() {
        System.clearProperty("spring.profiles.active");
    }

    @Order(1)
    @ParameterizedTest
    @CsvFileSource(resources = "/user/getClientAll.csv", delimiter = ';')
    public void getClientAll(String response) throws Exception {
        mockMvc.perform(get("/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(2)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/user/getClient.csv", delimiter = ';')
    public void getClient(String response) throws Exception {
        mockMvc.perform(get("/myaccount")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(3)
    @ParameterizedTest
    @CsvFileSource(resources = "/user/addClient.csv", delimiter = ';')
    public void addClient(String request, String response) throws Exception {
        mockMvc.perform(post("/signup")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(4)
    @ParameterizedTest
    @WithMockUser("piotr.krakowski@kino.pl")
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

    @Order(5)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/user/updateUser.csv", delimiter = ';')
    public void updateUser(String request, String response) throws Exception {
        mockMvc.perform(put("/myaccount")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

}
