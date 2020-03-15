package pl.connectis.cinemareservationsapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.connectis.cinemareservationsapp.dto.UserDTO;
import pl.connectis.cinemareservationsapp.exceptions.BadRequestException;
import pl.connectis.cinemareservationsapp.exceptions.ResourceNotFoundException;
import pl.connectis.cinemareservationsapp.model.User;
import pl.connectis.cinemareservationsapp.security.IAuthenticationFacade;
import pl.connectis.cinemareservationsapp.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @GetMapping("/myaccount")
    public UserDTO currentUserNameSimple() {

        return userService.getUser();

    }

    @GetMapping("all")
    public Iterable<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping
    public User getUserByUsername(@RequestParam String username) {
        User user = userService.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("user {username=" + username + "} was not found");
        }
        return user;
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestParam String username, @Valid @RequestBody User user) {
        User existingUser = userService.findByUsername(username);
        if (existingUser == null) {
            throw new ResourceNotFoundException("user {username=" + username + "} was not found");
        } else if (!user.getUsername().equals(existingUser.getUsername())) {
            throw new BadRequestException(
                    "{username=" + user.getUsername() + "} does not correspond to user of {username=" + username + "}");
        } else {
            return new ResponseEntity<>(userService.updateById(username, user), HttpStatus.CREATED);
        }
    }

}
