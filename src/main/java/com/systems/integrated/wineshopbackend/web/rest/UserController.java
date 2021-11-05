package com.systems.integrated.wineshopbackend.web.rest;

import com.systems.integrated.wineshopbackend.models.users.AuthToken;
import com.systems.integrated.wineshopbackend.models.users.DTO.ChangePasswordDTO;
import com.systems.integrated.wineshopbackend.models.users.DTO.ResetPasswordDTO;
import com.systems.integrated.wineshopbackend.models.users.DTO.UserDTO;
import com.systems.integrated.wineshopbackend.models.users.User;
import com.systems.integrated.wineshopbackend.service.intef.AuthTokenService;
import com.systems.integrated.wineshopbackend.service.intef.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.Objects;

@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RestController
@RequestMapping(value = "/api/users")
public class UserController {

    private final UserService userService;
    private final AuthTokenService authTokenService;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping(value = "/{userId}")
    public ResponseEntity<?> getUser(@PathVariable("userId") Long userId) {
        return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Validated UserDTO userDTO) throws MessagingException {
        User user = userService.createUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updateUser(@RequestBody @Validated UserDTO userDTO) {
        User user = userService.updateUser(userDTO);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping(value = "/change-password")
    public ResponseEntity<?> changeUserPassword (@RequestBody @Validated ChangePasswordDTO changePasswordDTO) {
        Long userId = changePasswordDTO.getUserId();
        String newPassword = changePasswordDTO.getNewPassword();

        AuthToken authToken = authTokenService.findByUserId(userId);

        boolean validateToken = authTokenService.validateToken(authToken.getToken());

        if (validateToken) {
            User userToUpdate = userService.getUserById(userId);
            userService.changeUserPassword(userToUpdate, newPassword);

            if (Objects.isNull(userToUpdate)) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            return new ResponseEntity<>("Changed password successfully!", HttpStatus.OK);
        }

        return new ResponseEntity<>("Invalid token", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping(value = "/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(String.format("User [%s] is deleted!", userId), HttpStatus.OK);
    }

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        User user = userService.getUserByEmail(resetPasswordDTO.getEmail());

        if (Objects.isNull(user)) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        userService.resetUserPassword(user);

        return new ResponseEntity<>("Email successfully sent to" + user.getUsername(), HttpStatus.OK);
    }
}
