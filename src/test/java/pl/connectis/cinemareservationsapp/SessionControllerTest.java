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
import org.springframework.test.web.servlet.MockMvc;

import java.util.Properties;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
public class SessionControllerTest {

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
    @CsvFileSource(resources = "/session/getSession.csv", delimiter = ';')
    public void getSession(String response) throws Exception {
        mockMvc.perform(get("/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(2)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionByDate.csv", delimiter = ';')
    public void getSessionByDate(String date, String response) throws Exception {
        mockMvc.perform(get("/session?date={date}", date)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(3)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionByMovie.csv", delimiter = ';')
    public void getSessionByMovie(long movieId, String response) throws Exception {
        mockMvc.perform(get("/session?movie={movieId}", movieId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(4)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSessionByRoom.csv", delimiter = ';')
    public void getSessionByRoom(long roomId, String response) throws Exception {
        mockMvc.perform(get("/session?room={roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(5)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/getSeats.csv", delimiter = ';')
    public void getSeats(long sessionId, String response) throws Exception {
        mockMvc.perform(get("/session/seats?id={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSession.csv", delimiter = ';')
    public void addSession(String request, String response) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(7)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSessionMovieDoesntExist.csv", delimiter = ';')
    public void addSessionMovieDoesntExist(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(8)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSessionRoomDoesntExist.csv", delimiter = ';')
    public void addSessionRoomDoesntExist(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(9)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSessionStartInPast.csv", delimiter = ';')
    public void addSessionStartInPast(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(10)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSessionStartsBeforeEndOfPrevious.csv", delimiter = ';')
    public void addSessionStartsBeforeEndOfPrevious(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(11)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/addSessionEndsAfterStartOfNext.csv", delimiter = ';')
    public void addSessionEndsAfterStartOfNext(String request) throws Exception {
        mockMvc.perform(post("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(12)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/updateSession.csv", delimiter = ';')
    public void updateSession(String request, String response) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(13)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/updateSessionMovieDoesntExist.csv", delimiter = ';')
    public void updateSessionMovieDoesntExist(String request) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(14)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/updateSessionRoomDoesntExist.csv", delimiter = ';')
    public void updateSessionRoomDoesntExist(String request) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(15)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/deleteSession.csv", delimiter = ';')
    public void deleteSession(long id) throws Exception {
        mockMvc.perform(delete("/session?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Order(16)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/deleteSessionDoesntExist.csv", delimiter = ';')
    public void deleteSessionDoesntExist(long id) throws Exception {
        mockMvc.perform(delete("/session?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(17)
    @ParameterizedTest
    @CsvFileSource(resources = "/session/updateSessionDoesntExist.csv", delimiter = ';')
    public void updateSessionDoesntExist(String request) throws Exception {
        mockMvc.perform(put("/session")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}
