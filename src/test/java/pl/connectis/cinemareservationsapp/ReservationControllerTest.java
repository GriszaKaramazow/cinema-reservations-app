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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("develop")
public class ReservationControllerTest {

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
    @CsvFileSource(resources = "/reservation/makeReservation.csv", delimiter = ';')
    public void makeReservation_Unauthenticated(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(2)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservation.csv", delimiter = ';')
    public void makeReservation_AuthenticatedAsClient(String request, String response) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().json(response))
                .andDo(print());
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(username = "piotr.krakowski@kino.pl", roles = "EMPLOYEE")
    @CsvFileSource(resources = "/reservation/makeReservation.csv", delimiter = ';')
    public void makeReservation_AuthenticatedAsEmployee(String request, String response) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(4)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservationSessionDoesntExists.csv", delimiter = ';')
    public void makeReservationSessionDoesntExists_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Order(5)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservationEmptySeats.csv", delimiter = ';')
    public void makeReservationEmptySeats_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservationReservedSeats.csv", delimiter = ';')
    public void makeReservationReservedSeats_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(7)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservationNoSuchRow.csv", delimiter = ';')
    public void makeReservationNoSuchRow_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(8)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservationNoSuchSeatInTheRow.csv", delimiter = ';')
    public void makeReservationNoSuchSeatInTheRow_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(9)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservationSeatsInDifferentRows.csv", delimiter = ';')
    public void makeReservationSeatsInDifferentRows_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Order(10)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/reservation/makeReservationNotNextToEachOther.csv", delimiter = ';')
    public void makeReservationNotNextToEachOther_AuthenticatedAsClient(String request) throws Exception {
        mockMvc.perform(post("/reservation")
                .content(request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}
