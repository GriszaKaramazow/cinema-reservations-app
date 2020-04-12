package pl.connectis.cinemareservationsapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.connectis.cinemareservationsapp.dto.UserDTO;
import pl.connectis.cinemareservationsapp.service.UserService;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addClient(@Valid @RequestBody UserDTO userDTO) {
        return userService.createAccount(userDTO, false);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDTO addEmployee(@Valid @RequestBody UserDTO userDTO) {
        return userService.createAccount(userDTO, true);
    }

    @GetMapping("/myaccount")
    public UserDTO getLoggedUser() {
        return userService.getLoggedUser();
    }

    @PutMapping("/myaccount")
    public UserDTO updateUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.updateUser(userDTO);
    }

    @GetMapping("/client")
    public List<UserDTO> getClientByExample(@RequestParam Map<String, String> requestParam) {
        return userService.getClientByExample(requestParam);
    }

}
