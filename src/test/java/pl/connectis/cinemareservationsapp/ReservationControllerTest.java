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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
public class ReservationControllerTest {

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
    @CsvFileSource(resources = "/reservation/makeReservation.csv", delimiter = ';')
    public void makeReservation_HasAccessWhenUnauthenticated_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @DirtiesContext
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation.csv", delimiter = ';')
    public void makeReservation_HasAccessWhenAuthenticatedAsClient_StatusCreatedAndCorrectResponseBodyReceived(
            String requestBody, String responseBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @ParameterizedTest
    @WithMockUser(username = "piotr.krakowski@kino.pl", roles = "EMPLOYEE")
    @CsvFileSource(resources = "/reservation/makeReservation.csv", delimiter = ';')
    public void makeReservation_HasAccessWhenAuthenticatedAsEmployee_StatusForbidden(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation_SessionDoesntExists.csv", delimiter = ';')
    public void makeReservation_SessionDoesntExists_StatusNotFound(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation_NoSeatsChosen.csv", delimiter = ';')
    public void makeReservation_NoSeatsChosen_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation_SeatsAlreadyReserved.csv", delimiter = ';')
    public void makeReservation_SeatsAlreadyReserved_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation_NoSuchRowInRoom.csv", delimiter = ';')
    public void makeReservation_NoSuchRowInRoom_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation_NoSuchSeatInRow.csv", delimiter = ';')
    public void makeReservation_NoSuchSeatInRow_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation_SeatsInDifferentRows.csv", delimiter = ';')
    public void makeReservation_SeatsInDifferentRows_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation_SeatsNotNextToEachOther.csv", delimiter = ';')
    public void makeReservation_SeatsNotNextToEachOther_StatusBadRequest(
            String requestBody) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}