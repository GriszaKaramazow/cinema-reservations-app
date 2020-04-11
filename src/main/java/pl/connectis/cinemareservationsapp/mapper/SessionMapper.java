package pl.connectis.cinemareservationsapp.mapper;

import pl.connectis.cinemareservationsapp.dto.SessionDTO;
import pl.connectis.cinemareservationsapp.model.Movie;
import pl.connectis.cinemareservationsapp.model.Room;
import pl.connectis.cinemareservationsapp.model.Session;

import java.util.ArrayList;
import java.util.List;

public class SessionMapper {

    public SessionDTO mapDTOFromEntity(Session session) {
        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setId(session.getId());
        if (session.getMovie() != null) {
            sessionDTO.setMovieId(session.getMovie().getId());
        }
        if (session.getRoom() != null) {
            sessionDTO.setRoomId(session.getRoom().getId());
        }
        sessionDTO.setStartDate(session.getStartDate());
        sessionDTO.setStartTime(session.getStartTime());
        sessionDTO.setTicketPrice(session.getTicketPrice());
        return sessionDTO;
    }

    public List<SessionDTO> mapDTOFromEntity(List<Session> sessions) {
        List<SessionDTO> sessionDTOs = new ArrayList<>(sessions.size());
        for (Session session : sessions) {
            sessionDTOs.add(mapDTOFromEntity(session));
        }
        return sessionDTOs;
    }

    public Session mapEntityFromDTO(SessionDTO sessionDTO) {
        Session session = new Session();
        if (sessionDTO.getId() != null) {
            session.setId(sessionDTO.getId());
        }
        if (sessionDTO.getMovieId() != null) {
            Movie movie = new Movie();
            movie.setId(sessionDTO.getMovieId());
            session.setMovie(movie);

        }
        if (sessionDTO.getRoomId() != null) {
            Room room = new Room();
            room.setId(sessionDTO.getRoomId());
            session.setRoom(room);
        }
        session.setStartTime(sessionDTO.getStartTime());
        session.setStartDate(sessionDTO.getStartDate());
        session.setTicketPrice(sessionDTO.getTicketPrice());
        return session;
    }

}
