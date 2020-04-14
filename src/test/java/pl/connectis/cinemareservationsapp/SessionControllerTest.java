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
public class SessionControllerTest {

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
    @CsvFileSource(resources = "/session/getSessionsByExample_GetAllSessions.csv", delimiter = ';')
    public void getSessionsByExample_HasAccessWhenUnauthenticated_StatusOkAndCorrectResponseBodyReceived(
            String responseBody) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(2)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/getSessionsByExample_GetAllSessions.csv", delimiter = ';')
    public void getSessionsByExample_HasAccessWhenAuthenticatedAsClient_StatusOkAndCorrectResponseBodyReceived(
            String responseBody) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/getSessionsByExample_GetAllSessions.csv", delimiter = ';')
    public void getSessionsByExample_HasAccessWhenAuthenticatedAsEmployee_StatusOkAndCorrectResponseBodyReceived(
            String responseBody) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(4)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExample_GetSessionsByDate.csv", delimiter = ';')
    public void getSessionsByExample_GetSessionsByDate_StatusOkAndCorrectResponseBodyReceived(
            String date, String responseBody) throws Exception {
        mockMvc.perform(get("/session?date={date}", date)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(5)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExample_GetSessionsByMovie.csv", delimiter = ';')
    public void getSessionsByExample_GetSessionsByMovie_StatusOkAndCorrectResponseBodyReceived(
            long movieId, String responseBody) throws Exception {
        mockMvc.perform(get("/session?movie={movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExample_GetSessionsByRoom.csv", delimiter = ';')
    public void getSessionsByExample_GetSessionsByRoom_StatusOkAndCorrectResponseBodyReceived(
            long roomId, String responseBody) throws Exception {
        mockMvc.perform(get("/session?room={roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(7)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_HasAccessWhenUnauthenticated_StatusOkAndCorrectResponseBodyReceived(
            long sessionId, String responseBody) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(8)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_HasAccessWhenAuthenticatedAsClient_StatusOkAndCorrectResponseBodyReceived(
            long sessionId, String responseBody) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(9)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_HasAccessWhenAuthenticatedAsEmployee_StatusOkAndCorrectResponseBodyReceived(
            long sessionId, String responseBody) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(10)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_HasAccessWhenUnauthenticated_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_HasAccessWhenAuthenticatedAsClient_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(12)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_HasAccessWhenAuthenticatedAsEmployee_StatusCreatedAndCorrectResponseBodyReceived(
            String requestBody, String responseBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(13)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_MovieDoesntExist.csv", delimiter = ';')
    public void addSession_MovieDoesntExist_StatusNotFound(
            String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Order(14)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_RoomDoesntExist.csv", delimiter = ';')
    public void addSession_RoomDoesntExist_StatusNotFound(
            String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Order(15)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_SessionStartsInPast.csv", delimiter = ';')
    public void addSession_SessionStartsInPast_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Order(16)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_SessionStartsBeforeEndOfPrevious.csv", delimiter = ';')
    public void addSession_SessionStartsBeforeEndOfPrevious_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Order(17)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_SessionEndsAfterStartOfNext.csv", delimiter = ';')
    public void addSession_SessionEndsAfterStartOfNext_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Order(18)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_HasAccessWhenUnauthenticated_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(19)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_HasAccessWhenAuthenticatedAsClient_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(20)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_HasAccessWhenAuthenticatedAsEmployee_StatusOkAndCorrectResponseBodyReceived(
            String requestBody, String responseBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(27)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession_SessionDoesntExist.csv", delimiter = ';')
    public void updateSession_SessionDoesntExist_StatusNotFound(
            String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Order(21)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession_MovieDoesntExist.csv", delimiter = ';')
    public void updateSession_MovieDoesntExist_StatusNotFound(
            String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Order(22)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession_RoomDoesntExist.csv", delimiter = ';')
    public void updateSession_RoomDoesntExist_StatusNotFound(
            String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Order(23)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession_HasAccessWhenUnauthenticated_StatusForbidden(
            long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(24)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession_HasAccessWhenAuthenticatedAsClient_StatusForbidden(
            long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(25)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession_HasAccessWhenAuthenticatedAsEmployee_StatusNoContent(
            long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Order(26)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/deleteSession_SessionDoesntExist.csv", delimiter = ';')
    public void deleteSession_SessionDoesntExist_StatusNotFound(
            long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
