package pl.connectis.cinemareservationsapp;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
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
public class MovieControllerTest {

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
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/getMoviesByExample_GetAll.csv", delimiter = ';')
    public void getMoviesByExample_GetAll_Unauthenticated(String responseBody) throws Exception {
        mockMvc.perform(get("/movie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(2)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/movie/getMoviesByExample_GetAll.csv", delimiter = ';')
    public void getMoviesByExample_GetAll_AuthenticatedAsClient(String responseBody) throws Exception {
        mockMvc.perform(get("/movie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/getMoviesByExample_GetAll.csv", delimiter = ';')
    public void getMoviesByExample_GetAll_AuthenticatedAsEmployee(String responseBody) throws Exception {
        mockMvc.perform(get("/movie")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(4)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/getMoviesByExample_GetByCategory.csv", delimiter = ';')
    public void getMoviesByExample_GetByCategory_Unauthenticated(String category, String responseBody) throws Exception {
        mockMvc.perform(get("/movie?category={category}", category)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(5)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/getMoviesByExample_GetByTitle.csv", delimiter = ';')
    public void getMoviesByExample_GetByTitle_Unauthenticated(String title, String responseBody) throws Exception {
        mockMvc.perform(get("/movie?title={title}", title)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/addMovie.csv", delimiter = ';')
    public void addMovie_Unauthenticated(String requestBody) throws Exception {
        mockMvc.perform(post("/movie")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
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
                .andExpect(status().isForbidden());
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
                .andExpect(content().json(responseBody));
    }

    @Order(9)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/updateMovie.csv", delimiter = ';')
    public void updateMovie_Unauthenticated(String requestBody) throws Exception {
        mockMvc.perform(put("/movie")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(10)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/movie/updateMovie.csv", delimiter = ';')
    public void updateMovie_AuthenticatedAsClient(String requestBody) throws Exception {
        mockMvc.perform(put("/movie")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/updateMovie.csv", delimiter = ';')
    public void updateMovie_AuthenticatedAsEmployee(String requestBody, String responseBody) throws Exception {
        mockMvc.perform(put("/movie")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(responseBody));
    }

    @Order(12)
    @ParameterizedTest
    @CsvFileSource(resources = "/movie/deleteMovie.csv", delimiter = ';')
    public void deleteMovie_Unauthenticated(long movieId) throws Exception {
        mockMvc.perform(delete("/movie?id={movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(13)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/movie/deleteMovie.csv", delimiter = ';')
    public void deleteMovie_AuthenticatedAsClient(long movieId) throws Exception {
        mockMvc.perform(delete("/movie?id={movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(14)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/deleteMovie.csv", delimiter = ';')
    public void deleteMovie_AuthenticatedAsEmployee(long movieId) throws Exception {
        mockMvc.perform(delete("/movie?id={movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Order(15)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/deleteMovie_MovieDoesntExist.csv", delimiter = ';')
    public void deleteMovie_MovieDoesntExist_AuthenticatedAsEmployee(long movieId) throws Exception {
        mockMvc.perform(delete("/movie?id={movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Order(16)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/movie/updateMovie_MovieDoesntExist.csv", delimiter = ';')
    public void updateMovie_MovieDoesntExist_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(put("/movie")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}