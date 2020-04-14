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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("develop")
public class RoomControllerTest {

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
    public void getRoomsByExample_GetAll_Unauthenticated() throws Exception {
        mockMvc.perform(get("/room")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(2)
    @Test
    @WithMockUser(roles = "CLIENT")
    public void getRoomsByExample_GetAll_AuthenticatedAsClient() throws Exception {
        mockMvc.perform(get("/room")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(3)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/getRoomsByExample_GetAll.csv", delimiter = ';')
    public void getRoomsByExample_GetAll_AuthenticatedAsEmployee(String responseBody) throws Exception {
        mockMvc.perform(get("/room")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(4)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/getRoomsByExample_GetByCapacity.csv", delimiter = ';')
    public void getRoomsByExample_GetByCapacity_AuthenticatedAsEmployee(int capacity, String responseBody) throws Exception {
        mockMvc.perform(get("/room?capacity={capacity}", capacity)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(5)
    @ParameterizedTest
    @CsvFileSource(resources = "/room/addRoom.csv", delimiter = ';')
    public void addRoom_Unauthenticated(String requestBody) throws Exception {
        mockMvc.perform(post("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(6)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/room/addRoom.csv", delimiter = ';')
    public void addRoom_AuthenticatedAsClient(String requestBody) throws Exception {
        mockMvc.perform(post("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(7)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/addRoom.csv", delimiter = ';')
    public void addRoom_AuthenticatedAsEmployee(String requestBody, String responseBody) throws Exception {
        mockMvc.perform(post("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(8)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/addRoom_CapacityDoesntMeetLayout.csv", delimiter = ';')
    public void addRoom_CapacityDoesntMeetLayout_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(post("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Order(9)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/addRoom_InappropriateLayoutFormat.csv", delimiter = ';')
    public void addRoom_InappropriateLayoutFormat_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(post("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Order(10)
    @ParameterizedTest
    @CsvFileSource(resources = "/room/updateRoom.csv", delimiter = ';')
    public void updateRoom_Unauthenticated(String requestBody) throws Exception {
        mockMvc.perform(put("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(11)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/room/updateRoom.csv", delimiter = ';')
    public void updateRoom_AuthenticatedAsClient(String requestBody) throws Exception {
        mockMvc.perform(put("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(12)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/updateRoom.csv", delimiter = ';')
    public void updateRoom_AuthenticatedAsEmployee(String requestBody, String responseBody) throws Exception {
        mockMvc.perform(put("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(responseBody));
    }

    @Order(13)
    @ParameterizedTest
    @CsvFileSource(resources = "/room/deleteRoom.csv", delimiter = ';')
    public void deleteRoom_Unauthenticated(long roomId) throws Exception {
        mockMvc.perform(delete("/room?id={roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(14)
    @ParameterizedTest
    @WithMockUser(roles = "CLIENT")
    @CsvFileSource(resources = "/room/deleteRoom.csv", delimiter = ';')
    public void deleteRoom_AuthenticatedAsClient(long roomId) throws Exception {
        mockMvc.perform(delete("/room?id={roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Order(15)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/deleteRoom.csv", delimiter = ';')
    public void deleteRoom_AuthenticatedAsEmployee(long roomId) throws Exception {
        mockMvc.perform(delete("/room?id={roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Order(16)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/deleteRoom_RoomDoesntExists.csv", delimiter = ';')
    public void deleteRoom_RoomDoesntExists_AuthenticatedAsEmployee(long roomId) throws Exception {
        mockMvc.perform(delete("/room?id={roomId}", roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Order(17)
    @ParameterizedTest
    @WithMockUser(roles = "EMPLOYEE")
    @CsvFileSource(resources = "/room/updateRoom_RoomDoesntExists.csv", delimiter = ';')
    public void updateRoom_RoomDoesntExists_AuthenticatedAsEmployee(String requestBody) throws Exception {
        mockMvc.perform(put("/room")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}