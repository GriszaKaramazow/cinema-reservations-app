package pl.connectis.cinemareservationsapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.connectis.cinemareservationsapp.dto.TicketDTO;
import pl.connectis.cinemareservationsapp.service.TicketService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/mytickets")
    public List<TicketDTO> getMyTickets(@RequestParam Map<String, String> requestParam) {
        return ticketService.getMyTicketsByExample(requestParam);
    }

    @GetMapping("/ticket")
    public List<TicketDTO> getTicketByExample(@RequestParam Map<String, String> requestParams) {
        return ticketService.getTicketsByExample(requestParams);
    }

    @DeleteMapping("/ticket")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTicket(@RequestParam Long id) {
        ticketService.deleteById(id);
    }

}
