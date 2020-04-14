package pl.connectis.cinemareservationsapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.connectis.cinemareservationsapp.dto.ReservationDTO;
import pl.connectis.cinemareservationsapp.dto.SessionDTO;
import pl.connectis.cinemareservationsapp.dto.UserDTO;
import pl.connectis.cinemareservationsapp.model.Movie;
import pl.connectis.cinemareservationsapp.model.Room;
import pl.connectis.cinemareservationsapp.model.Seat;
import pl.connectis.cinemareservationsapp.service.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@Profile("develop")
public class DevelopDatabaseInitializer implements CommandLineRunner {

    private final UserService userService;
    private final MovieService movieService;
    private final RoomService roomService;
    private final SessionService sessionService;
    private final ReservationService reservationService;

    public DevelopDatabaseInitializer(UserService userService, MovieService movieService, RoomService roomService, SessionService sessionService, ReservationService reservationService) {
        this.userService = userService;
        this.movieService = movieService;
        this.roomService = roomService;
        this.sessionService = sessionService;
        this.reservationService = reservationService;
    }

    @Override
    public void run(String... args) {
        initializeDatabase();
    }

    private void initializeDatabase() {
        addClients();
        addEmployees();
        addMovies();
        addRooms();
        addSessions();
        addTickets();
    }

    private void addClients() {
        userService.createAccount(
                new UserDTO("adrian.budny@poczta.pl", "adrianbudny",
                null, "Adrian", "Budny",
                LocalDate.of(1981, 2, 15)), false);
        userService.createAccount(
                new UserDTO("gracjan.pakulski@poczta.pl", "gracjanpakulski",
                null, "Gracjan", "Pakulski",
                LocalDate.of(2003, 5, 28)), false);
        userService.createAccount(
                new UserDTO("sylwester.lis@poczta.pl", "sylwesterlis",
                null, "Sylwester", "Lis",
                LocalDate.of(1999, 4, 4)), false);
        userService.createAccount(
                new UserDTO("marta.fabian@poczta.pl", "martafabian",
                null, "Marta", "Fabian",
                LocalDate.of(2010, 7, 30)), false);
        userService.createAccount(
                new UserDTO("boleslaw.sniegowski@poczta.pl", "boleslawsniegowski",
                null, "Bolesław", "Śniegowski",
                LocalDate.of(2006, 3, 7)), false);
        userService.createAccount(
                new UserDTO("diana.czajka@poczta.pl", "dianaczajka",
                null, "Diana", "Czajka",
                LocalDate.of(1973, 11, 16)), false);
        userService.createAccount(
                new UserDTO("filip.chmielewski@poczta.pl", "filipchmielewski",
                null, "Filip", "Chmielewski",
                LocalDate.of(1921, 8, 6)), false);
        userService.createAccount(
                new UserDTO("mateusz.wlodarczyk@poczta.pl", "mateuszwlodarczyk",
                null, "Mateusz", "Włodarczyk",
                LocalDate.of(2001, 1, 27)), false);
    }

    private void addEmployees() {
        userService.createAccount(
                new UserDTO("rozalia.sikora@kino.pl", "rozaliasikora",
                        null, "Rozalia", "Sikora",
                        LocalDate.of(1994, 5, 27)), true);
        userService.createAccount(
                new UserDTO("franciszek.walentowicz@kino.pl", "franciszekwalentowicz",
                        null, "Franciszek", "Walentowicz",
                        LocalDate.of(1993, 10, 18)), true);
        userService.createAccount(
                new UserDTO("piotr.krakowski@kino.pl", "piotrkrakowski",
                        null, "Piotr", "Krakowski",
                        LocalDate.of(1997, 6, 9)), true);
    }

    private void addMovies() {
        movieService.save(
                new Movie(1L, "Gwiezdne wojny: Skywalker. Odrodzenie", "Familijny", 142,
                        "Członkowie organizacji Ruchu Oporu ponownie stawiają czoła Najwyższemu Porządkowi.",
                        10));
        movieService.save(
                new Movie(2L, "Jak zostałem gangsterem. Historia prawdziwa", "Akcja", 140,
                        "Historia najniebezpieczniejszego gangstera w Polsce, dla którego władza, bycie " +
                                "ponad stan i pieniądze stanowią priorytet.", 15));
        movieService.save(
                new Movie(3L, "Judy", "Dramat", 118,
                        "Zima 1968 roku. Ciesząca się ogromną popularnością Judy Garland przybywa do " +
                                "Londynu na serię koncertów.", 15));
        movieService.save(
                new Movie(4L, "Oficer i szpieg", "Thriller szpiegowski", 132,
                        "Historia francuskiego kapitana Alfreda Dreyfusa, który został niesłusznie " +
                                "oskarżony o zdradę stanu i skazany na dożywotnie więzienie.", 15));
        movieService.save(
                new Movie(5L, "Jumanji: Przygoda w dżungli", "Familijny", 123,
                        "Wielki powrót bohaterów „Jumanji: Przygoda w dżungli”! Pierwsza część filmu " +
                                "stała się międzynarodowym przebojem, zarabiając na całym świecie ponad 960 milionów " +
                                "dolarów.", 12));
    }

    private void addRooms() {
        roomService.save(new Room(1L, 154, "22,22,22,22,22,22,22"));
        roomService.save(new Room(2L, 50, "10,10,10,10,10"));
        roomService.save(new Room(3L, 300, "30,30,30,30,30,30,30,30,30,30"));
        roomService.save(new Room(4L, 96, "16,16,16,16,16,16"));
        roomService.save(new Room(5L, 90, "15,15,15,15,15,15"));
    }

    private void addSessions() {
        sessionService.save(new SessionDTO(1L, 3L, 1L,
                LocalDate.of(2020, 7, 10), LocalTime.of(15, 0), 14.99));
        sessionService.save(new SessionDTO(2L, 2L, 2L,
                LocalDate.of(2020, 7, 2), LocalTime.of(16, 0), 14.99));
        sessionService.save(new SessionDTO(3L, 1L, 5L,
                LocalDate.of(2020, 7, 2), LocalTime.of(17, 0), 14.99));
        sessionService.save(new SessionDTO(4L, 4L, 1L,
                LocalDate.of(2020, 7, 2), LocalTime.of(18, 0), 14.99));
        sessionService.save(new SessionDTO(5L, 2L, 3L,
                LocalDate.of(2020, 7, 2), LocalTime.of(18, 30), 14.99));
        sessionService.save(new SessionDTO(6L, 2L, 2L,
                LocalDate.of(2020, 7, 2), LocalTime.of(21, 0), 14.99));
        sessionService.save(new SessionDTO(7L, 2L, 3L,
                LocalDate.of(2020, 7, 2), LocalTime.of(21, 30), 14.99));
    }

    private void addTickets() {
        reservationService.makeReservation(new ReservationDTO(4L, new ArrayList<>(Arrays.asList(
                new Seat(4, 10, false)))), "adrian.budny@poczta.pl");
        reservationService.makeReservation(new ReservationDTO(4L, new ArrayList<>(Arrays.asList(
                new Seat(3, 10, false),
                new Seat(3, 11, false)))), "filip.chmielewski@poczta.pl");
        reservationService.makeReservation(new ReservationDTO(2L, new ArrayList<>(Arrays.asList(
                new Seat(5, 8, false),
                new Seat(5, 9, false)))), "diana.czajka@poczta.pl");
        reservationService.makeReservation(new ReservationDTO(1L, new ArrayList<>(Arrays.asList(
                new Seat(2, 5, false),
                new Seat(2, 6, false),
                new Seat(2, 7, false)))), "sylwester.lis@poczta.pl");
        reservationService.makeReservation(new ReservationDTO(1L, new ArrayList<>(Arrays.asList(
                new Seat(4, 10, false)))), "mateusz.wlodarczyk@poczta.pl");
        reservationService.makeReservation(new ReservationDTO(6L, new ArrayList<>(Arrays.asList(
                new Seat(3, 6, false)))), "gracjan.pakulski@poczta.pl");
        reservationService.makeReservation(new ReservationDTO(7L, new ArrayList<>(Arrays.asList(
                new Seat(6, 16, false),
                new Seat(6, 17, false)))), "mateusz.wlodarczyk@poczta.pl");
        reservationService.makeReservation(new ReservationDTO(5L, new ArrayList<>(Arrays.asList(
                new Seat(7, 14, false),
                new Seat(7, 15, false)))), "filip.chmielewski@poczta.pl");
        reservationService.makeReservation(new ReservationDTO(1L, new ArrayList<>(Arrays.asList(
                new Seat(4, 11, false)))), "gracjan.pakulski@poczta.pl");
    }

}
