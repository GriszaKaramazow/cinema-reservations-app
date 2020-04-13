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
    @CsvFileSource(resources = "/session/getSessionsByExample_GetAll.csv", delimiter = ';')
    public void getSessionsByExample_GetAll_Unauthenticated(String responseBody) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(2)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/getSessionsByExample_GetAll.csv", delimiter = ';')
    public void getSessionsByExample_GetAll_AuthenticatedAsClient(String responseBody) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/getSessionsByExample_GetAll.csv", delimiter = ';')
    public void getSessionsByExample_GetAll_AuthenticatedAsEmployee(String responseBody) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(4)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExample_GetByDate.csv", delimiter = ';')
    public void getSessionsByExample_GetByDate_Unauthenticated(String date, String responseBody) throws Exception {
        mockMvc.perform(get("/session?date={date}", date)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(5)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExample_GetByMovie.csv", delimiter = ';')
    public void getSessionsByExample_GetByMovie_Unauthenticated(long movieId, String responseBody) throws Exception {
        mockMvc.perform(get("/session?movie={movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionsByExample_GetByRoom.csv", delimiter = ';')
    public void getSessionsByExample_GetByRoom_Unauthenticated(long roomId, String responseBody) throws Exception {
        mockMvc.perform(get("/session?room={roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(7)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_Unauthenticated(long sessionId, String responseBody) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(8)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_AuthenticatedAsClient(long sessionId, String responseBody) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(9)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats_AuthenticatedAsEmployee(long sessionId, String responseBody) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(10)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_Unauthenticated(String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_AuthenticatedAsClient(String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(12)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession_AuthenticatedAsEmployee(String requestBody, String responseBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(13)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_MovieDoesntExist.csv", delimiter = ';')
    public void addSession_MovieDoesntExist_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(14)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_RoomDoesntExist.csv", delimiter = ';')
    public void addSession_RoomDoesntExist_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(15)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_SessionStartsInPast.csv", delimiter = ';')
    public void addSession_SessionStartsInPast_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(16)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_SessionStartsBeforeEndOfPrevious.csv", delimiter = ';')
    public void addSession_SessionStartsBeforeEndOfPrevious_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(17)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/addSession_SessionEndsAfterStartOfNext.csv", delimiter = ';')
    public void addSession_SessionEndsAfterStartOfNext_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(post("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(18)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_Unauthenticated(String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(19)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_AuthenticatedAsClient(String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(20)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession_AuthenticatedAsEmployee(String requestBody, String responseBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(21)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession_MovieDoesntExist.csv", delimiter = ';')
    public void updateSession_MovieDoesntExist_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(22)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession_RoomDoesntExist.csv", delimiter = ';')
    public void updateSession_RoomDoesntExist_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(23)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession_Unauthenticated(long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(24)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession_AuthenticatedAsClient(long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(25)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession_AuthenticatedAsEmployee(long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Order(26)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/deleteSession_SessionDoesntExist.csv", delimiter = ';')
    public void deleteSession_SessionDoesntExist_AuthenticatedAsEmployee(long sessionId) throws Exception {
        mockMvc.perform(delete("/session?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(27)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/session/updateSession_SessionDoesntExist.csv", delimiter = ';')
    public void updateSession_SessionDoesntExist_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(put("/session")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
