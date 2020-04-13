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
public class SessionControllerTest {

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
    @CsvFileSource(resources = "/session/getSessionsByExampleAll.csv", delimiter = ';')
    public void getSessionsByExampleAll_Unauthenticated(String response) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(2)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/getSessionsByExampleAll.csv", delimiter = ';')
    public void getSessionsByExampleAll_AuthenticatedAsClient(String response) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/getSessionsByExampleAll.csv", delimiter = ';')
    public void getSessionsByExampleAll_AuthenticatedAsEmployee(String response) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(4)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExampleDate.csv", delimiter = ';')
    public void getSessionsByExampleDate_Unauthenticated(String date, String response) throws Exception {
        mockMvc.perform(get("/session?date={date}", date)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(5)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExampleMovie.csv", delimiter = ';')
    public void getSessionsByExampleMovie_Unauthenticated(long movieId, String response) throws Exception {
        mockMvc.perform(get("/session?movie={movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExampleRoom.csv", delimiter = ';')
    public void getSessionsByExampleRoom_Unauthenticated(long roomId, String response) throws Exception {
        mockMvc.perform(get("/session?room={roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(7)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_Unauthenticated(long sessionId, String response) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(8)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_AuthenticatedAsClient(long sessionId, String response) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(9)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_AuthenticatedAsEmployee(long sessionId, String response) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(10)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_Unauthenticated(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(12)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_AuthenticatedAsEmployee(String request, String response) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(13)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSessionMovieDoesntExist.csv", delimiter = ';')
    public void addSessionMovieDoesntExist_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(14)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSessionRoomDoesntExist.csv", delimiter = ';')
    public void addSessionRoomDoesntExist_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(15)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSessionStartInPast.csv", delimiter = ';')
    public void addSessionStartInPast_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(16)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSessionStartsBeforeEndOfPrevious.csv", delimiter = ';')
    public void addSessionStartsBeforeEndOfPrevious_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(17)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSessionEndsAfterStartOfNext.csv", delimiter = ';')
    public void addSessionEndsAfterStartOfNext_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(18)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_Unauthenticated(String request) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(19)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(20)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_AuthenticatedAsEmployee(String request, String response) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(21)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSessionMovieDoesntExist.csv", delimiter = ';')
    public void updateSessionMovieDoesntExist_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(22)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSessionRoomDoesntExist.csv", delimiter = ';')
    public void updateSessionRoomDoesntExist_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(23)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession_AuthenticatedAsEmployee(long id) throws Exception {
        mockMvc.perform(delete("/session?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Order(24)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/deleteSessionDoesntExist.csv", delimiter = ';')
    public void deleteSessionDoesntExist_AuthenticatedAsEmployee(long id) throws Exception {
        mockMvc.perform(delete("/session?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(25)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSessionDoesntExist.csv", delimiter = ';')
    public void updateSessionDoesntExist_AuthenticatedAsEmployee(String request) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
