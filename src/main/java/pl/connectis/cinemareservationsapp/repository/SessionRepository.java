package pl.connectis.cinemareservationsapp.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.connectis.cinemareservationsapp.model.Session;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long> {

    List<Session> findById(long id);
}