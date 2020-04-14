package pl.connectis.cinemareservationsapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
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

    @DirtiesContext
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

    @DirtiesContext
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

    @ParameterizedTest
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession_HasAccessWhenUnauthenticated_StatusForbidden(
            long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

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

    @DirtiesContext
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
