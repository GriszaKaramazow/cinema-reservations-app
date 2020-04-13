package pl.connectis.cinemareservationsapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.connectis.cinemareservationsapp.model.Room;
import pl.connectis.cinemareservationsapp.service.RoomService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public List<Room> getRoomsByExample(@RequestParam Map<String, String> requestParam) {
        return roomService.findRoom(requestParam);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Room addRoom(@Valid @RequestBody Room room) {
        return roomService.save(room);
    }

    @PutMapping
    public Room updateRoom(@Valid @RequestBody Room room) {
        return roomService.updateById(room);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRoom(@RequestParam Long id) {
        roomService.deleteById(id);
    }

}
