package pl.connectis.cinemareservationsapp;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("develop")
public class MovieControllerTest {

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
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/getMoviesByExampleAll.csv", delimiter = ';')
    public void getMoviesByExampleAll_Unauthenticated(String response) throws Exception {
        mockMvc.perform(get("/movie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(2)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/movie/getMoviesByExampleAll.csv", delimiter = ';')
    public void getMoviesByExampleAll_AuthenticatedAsClient(String response) throws Exception {
        mockMvc.perform(get("/movie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/getMoviesByExampleAll.csv", delimiter = ';')
    public void getMoviesByExampleAll_AuthenticatedAsEmployee(String response) throws Exception {
        mockMvc.perform(get("/movie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(4)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/getMoviesByExampleCategory.csv", delimiter = ';')
    public void getMoviesByExampleCategory_Unauthenticated(String category, String response) throws Exception {
        mockMvc.perform(get("/movie?category={category}", category)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(5)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/getMoviesByExampleTitle.csv", delimiter = ';')
    public void getMoviesByExampleTitle_Unauthenticated(String title, String response) throws Exception {
        mockMvc.perform(get("/movie?title={title}", title)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/addMovie.csv", delimiter = ';')
    public void addMovie_Unauthenticated(String requestBody) throws Exception {
        mockMvc.perform(post("/movie")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(7)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/movie/addMovie.csv", delimiter = ';')
    public void addMovie_AuthenticatedAsClient(String requestBody) throws Exception {
        mockMvc.perform(post("/movie")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(8)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/addMovie.csv", delimiter = ';')
    public void addMovie_AuthenticatedAsEmployee(String requestBody, String responseBody) throws Exception {
        mockMvc.perform(post("/movie")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(9)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/updateMovie.csv", delimiter = ';')
    public void updateMovie_Unauthenticated(String request) throws Exception {
        mockMvc.perform(put("/movie")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(10)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/movie/updateMovie.csv", delimiter = ';')
    public void updateMovie_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(put("/movie")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/updateMovie.csv", delimiter = ';')
    public void updateMovie_AuthenticatedAsEmployee(String request, String response) throws Exception {
        mockMvc.perform(put("/movie")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(12)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/deleteMovie.csv", delimiter = ';')
    public void deleteMovie_Unauthenticated(long id) throws Exception {
        mockMvc.perform(delete("/movie?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(13)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/movie/deleteMovie.csv", delimiter = ';')
    public void deleteMovie_AuthenticatedAsClient(long id) throws Exception {
        mockMvc.perform(delete("/movie?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(14)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/deleteMovie.csv", delimiter = ';')
    public void deleteMovie_AuthenticatedAsEmployee(long id) throws Exception {
        mockMvc.perform(delete("/movie?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Order(15)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/deleteMovieDoesntExist.csv", delimiter = ';')
    public void deleteMovieDoesntExist_AuthenticatedAsEmployee(long id) throws Exception {
        mockMvc.perform(delete("/movie?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(16)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/updateMovieDoesntExist.csv", delimiter = ';')
    public void updateMovieDoesntExist_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(put("/movie")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
