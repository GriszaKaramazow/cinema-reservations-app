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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerTest {

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
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/reservation/makeReservation.csv", delimiter = ';')
    public void makeReservation(String request, String response) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/reservation/makeReservationSessionDoesntExists.csv", delimiter = ';')
    public void makeReservationSessionDoesntExists(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/reservation/makeReservationEmptySeats.csv", delimiter = ';')
    public void makeReservationEmptySeats(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/reservation/makeReservationReservedSeats.csv", delimiter = ';')
    public void makeReservationReservedSeats(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/reservation/makeReservationNoSuchRow.csv", delimiter = ';')
    public void makeReservationNoSuchRow(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/reservation/makeReservationNoSuchSeatInTheRow.csv", delimiter = ';')
    public void makeReservationNoSuchSeatInTheRow(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/reservation/makeReservationSeatsInDifferentRows.csv", delimiter = ';')
    public void makeReservationSeatsInDifferentRows(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser("filip.chmielewski@poczta.pl")
    @CsvFileSource(resources = "/reservation/makeReservationNotNextToEachOther.csv", delimiter = ';')
    public void makeReservationNotNextToEachOther(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}
