package pl.connectis.cinemareservationsapp;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.TestInstance.Lifecycle;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("develop")
public class TicketControllerTest {

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
    @Test
    public void getTicketsByExample_GetAll_Unauthenticated() throws Exception {
        mockMvc.perform(get("/ticket")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(2)
    @Test
    @WithMockUser(roles = "CLIENT")
    public void getTicketsByExample_GetAll_AuthenticatedAsClient() throws Exception {
        mockMvc.perform(get("/ticket")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/ticket/getTicketsByExample_GetAll.csv", delimiter = ';')
    public void getTicketsByExample_GetAll_AuthenticatedAsEmployee(String responseBody) throws Exception {
        mockMvc.perform(get("/ticket")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(4)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/ticket/getTicketsByExample_GetBySession.csv", delimiter = ';')
    public void getTicketsByExample_GetBySession_AuthenticatedAsEmployee(long sessionId, String responseBody) throws Exception {
        mockMvc.perform(get("/ticket?session={sessionId}", sessionId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(5)
    @Test
    public void getMyTickets_Unauthenticated() throws Exception {
        mockMvc.perform(get("/mytickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser(username = "filip.chmielewski@poczta.pl", roles = "CLIENT")
    @CsvFileSource(resources = "/ticket/getMyTickets.csv", delimiter = ';')
    public void getMyTickets_AuthenticatedAsClient(String responseBody) throws Exception {
        mockMvc.perform(get("/mytickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody))
                .andDo(print());
    }

    @Order(7)
    @Test
    @WithMockUser(username = "piotr.krakowski@kino.pl", roles = "EMPLOYEE")
    public void getMyTickets_AuthenticatedAsEmployee() throws Exception {
        mockMvc.perform(get("/mytickets")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(8)
    @ParameterizedTest
    @CsvFileSource(resources = "/ticket/deleteTicket.csv", delimiter = ';')
    public void deleteTicket_Unauthenticated(long id) throws Exception {
        mockMvc.perform(delete("/ticket?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(9)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/ticket/deleteTicket.csv", delimiter = ';')
    public void deleteTicket_AuthenticatedAsClient(long id) throws Exception {
        mockMvc.perform(delete("/ticket?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Order(10)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/ticket/deleteTicket.csv", delimiter = ';')
    public void deleteTicket_AuthenticatedAsEmployee(long id) throws Exception {
        mockMvc.perform(delete("/ticket?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/ticket/deleteTicketDoesntExist.csv", delimiter = ';')
    public void deleteTicketDoesntExist_AuthenticatedAsEmployee(long id) throws Exception {
        mockMvc.perform(delete("/ticket?id={id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

}