package pl.connectis.cinemareservationsapp.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.connectis.cinemareservationsapp.dto.ReservationDTO;
import pl.connectis.cinemareservationsapp.dto.TicketDTO;
import pl.connectis.cinemareservationsapp.exceptions.BadRequestException;
import pl.connectis.cinemareservationsapp.exceptions.ResourceNotFoundException;
import pl.connectis.cinemareservationsapp.mapper.TicketMapper;
import pl.connectis.cinemareservationsapp.model.Movie;
import pl.connectis.cinemareservationsapp.model.Seat;
import pl.connectis.cinemareservationsapp.model.Session;
import pl.connectis.cinemareservationsapp.model.Ticket;
import pl.connectis.cinemareservationsapp.repository.MovieRepository;
import pl.connectis.cinemareservationsapp.repository.SessionRepository;
import pl.connectis.cinemareservationsapp.repository.TicketRepository;
import pl.connectis.cinemareservationsapp.repository.UserRepository;
import pl.connectis.cinemareservationsapp.security.AuthenticationFacade;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@Slf4j
@Service
public class ReservationService {

    private final AuthenticationFacade authenticationFacade;
    private final SessionRepository sessionRepository;
    private final TicketRepository ticketRepository;
    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final TicketMapper ticketMapper;

    public ReservationService(AuthenticationFacade authenticationFacade,
                              SessionRepository sessionRepository,
                              TicketRepository ticketRepository,
                              MovieRepository movieRepository,
                              UserRepository userRepository,
                              TicketMapper ticketMapper) {
        this.authenticationFacade = authenticationFacade;
        this.sessionRepository = sessionRepository;
        this.ticketRepository = ticketRepository;
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.ticketMapper = ticketMapper;
    }

    public List<TicketDTO> makeReservation(ReservationDTO reservationDTO) {
        String username = authenticationFacade.getAuthentication().getName();
        return makeReservation(reservationDTO, username);
    }

    @Transactional
    public List<TicketDTO> makeReservation(ReservationDTO reservationDTO, String username) {
        validateSessionExists(reservationDTO.getSessionId());
        validateSeats(reservationDTO);
        validateSeatsAreNotAlreadyReserved(reservationDTO);
        validateClientAge(reservationDTO, username);
        List<Seat> seatsFromReservation = reservationDTO.getReservedSeats();
        log.info(seatsFromReservation.toString());
        List<Ticket> ticketsForReservation = mapTicketsFromReservationDTO(reservationDTO, username);
        for (Ticket ticket : ticketsForReservation) {
            ticket.getSeat().setSold(true);
        }
        List<Ticket> savedTickets = ticketRepository.saveAll(ticketsForReservation);
        savedTickets.forEach(savedTicket ->
                log.info("ticket {id=" + savedTicket.getId() + "} was added: " + savedTicket.toString()));
        return ticketMapper.mapDTOFromEntity(savedTickets);
    }

    private void validateSeatsAreNotAlreadyReserved(ReservationDTO reservationDTO) {
        List<Seat> seatsFromReservation = reservationDTO.getReservedSeats();
        Map<String, Seat> seatsFromSession = getSessionById(reservationDTO.getSessionId()).getSeats();
        for (Seat seatFromReservation : seatsFromReservation) {
            String mapKey = seatFromReservation.getRowNumber() + "x" + seatFromReservation.getSeatNumber();
            Seat seatFromSession = seatsFromSession.get(mapKey);
            if (seatFromSession.isSold()) {
                throw new BadRequestException("seat " + seatFromReservation.getSeatNumber() +
                        " in row " + seatFromReservation.getRowNumber() + " is reserved");
            }
        }
    }

    private void validateSessionExists(Long sessionId) {
        if (!sessionRepository.findById(sessionId).isPresent()) {
            throw new ResourceNotFoundException("session {id=" + sessionId + "} was not found");
        }
    }

    private Session getSessionById(Long sessionId) {
        if (sessionRepository.findById(sessionId).isPresent()) {
            return sessionRepository.findById(sessionId).get();
        }
        throw new ResourceNotFoundException("session {id=" + sessionId + "} was not found");
    }

    private Movie getMovieById(Long movieId) {
        if (movieRepository.findById(movieId).isPresent()) {
            return movieRepository.findById(movieId).get();
        }
        throw new ResourceNotFoundException("movie {id=" + movieId + "} was not found");
    }

    private void validateClientAge(ReservationDTO reservationDTO, String username) {
        Session session = getSessionById(reservationDTO.getSessionId());
        LocalDate sessionDate = session.getStartDate();
                LocalDate birthDate = userRepository.findByUsername(username).getBirthDate();
        int ageLimit = getMovieById(session.getMovie().getId()).getAgeLimit();
        if (birthDate.plusYears(ageLimit).isAfter(sessionDate)) {
            throw new BadRequestException("the user does not meet the age requirements");
        }
    }

    private void validateSeats(ReservationDTO reservationDTO) {
        List<Seat> reservedSeats = reservationDTO.getReservedSeats();
        int[] roomLayout = getRoomLayout(reservationDTO.getSessionId());
        List<Integer> seatsNumbers = new ArrayList<>(reservedSeats.size());
        int rowNumberOfFirstSeat = reservedSeats.get(0).getRowNumber();
        for (Seat seat : reservedSeats) {
            validateReservedSeatFitsRoomLayout(seat, roomLayout, rowNumberOfFirstSeat);
            seatsNumbers.add(seat.getSeatNumber());
        }
        validateReservedSeatsAreNextToEachOther(seatsNumbers);
    }

    private void validateReservedSeatFitsRoomLayout(Seat seat, int[] roomLayout, int row) {
        if (seat.getRowNumber() > roomLayout.length) {
            throw new BadRequestException("reserved seat does not correspond to room layout; row " +
                    seat.getRowNumber() + " out of " + roomLayout.length);
        }
        if (seat.getSeatNumber() > roomLayout[seat.getRowNumber()-1]) {
            throw new BadRequestException("reserved seat does not correspond to room layout; seat " +
                    seat.getSeatNumber() + " out of " + roomLayout[seat.getRowNumber()-1]);
        }
        if (row != seat.getRowNumber()){
            throw new BadRequestException("reserved seats should be in the same row");
        }
    }

    private void validateReservedSeatsAreNextToEachOther(List<Integer> seatNumbers) {
        Collections.sort(seatNumbers);
        if (seatNumbers.get(seatNumbers.size() - 1) - seatNumbers.get(0) != seatNumbers.size() - 1)  {
            throw new BadRequestException("reserved seats should be next to each other");
        }
    }

    private int[] getRoomLayout(Long sessionId) {
        String layoutString = getSessionById(sessionId).getRoom().getLayout();
        String[] layoutStringArray = layoutString.split(",");
        return Stream.of(layoutStringArray).mapToInt(Integer::parseInt).toArray();
    }

    private List<Ticket> mapTicketsFromReservationDTO(ReservationDTO reservationDTO, String username) {
        List<Ticket> tickets = ticketMapper.mapTicketsFromReservationDTO(reservationDTO, username);
        for (Ticket ticket : tickets) {
            ticket.setUser(userRepository.findByUsername(username));
            ticket.setSession(getSessionById(ticket.getSession().getId()));
            Seat seatFromSession = getSeatFromSession(reservationDTO.getSessionId(), ticket.getSeat());
            ticket.setSeat(seatFromSession);
        }
        return tickets;
    }

    private Seat getSeatFromSession(Long sessionId, Seat seatFromReservation) {
        int row = seatFromReservation.getRowNumber();
        int seat = seatFromReservation.getSeatNumber();
        String mapKey = row + "x" + seat;
        Seat seatFromSession = getSessionById(sessionId).getSeats().get(mapKey);
        seatFromSession.setSold(true);
        return seatFromSession;
    }

}
