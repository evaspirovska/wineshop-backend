package com.systems.integrated.wineshopbackend.service.impl;

import com.systems.integrated.wineshopbackend.models.exceptions.EntityNotFoundException;
import com.systems.integrated.wineshopbackend.models.users.AuthToken;
import com.systems.integrated.wineshopbackend.models.users.DTO.UserDTO;
import com.systems.integrated.wineshopbackend.models.users.Postman;
import com.systems.integrated.wineshopbackend.models.enumerations.Role;
import com.systems.integrated.wineshopbackend.models.users.User;
import com.systems.integrated.wineshopbackend.repository.PostmanJPARepository;
import com.systems.integrated.wineshopbackend.repository.UserJPARepository;
import com.systems.integrated.wineshopbackend.service.intef.AuthTokenService;
import com.systems.integrated.wineshopbackend.service.intef.EmailService;
import com.systems.integrated.wineshopbackend.service.intef.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserJPARepository userRepository;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;
    private final AuthTokenService authTokenService;
    private final PostmanJPARepository postmanRepository;

    @Value("${wineShop.mail.url}")
    private String url;

    public static final String CREATE_USER_SUBJECT = "User created";
    public static final String CREATE_USER_CONTENT =
            "Dear " + "%s,\n\n"
                    + "Your account has been created. Please click on the link to change your password: \n\n"
                    + "%s/%s,\n\n\n"
                    + "Best regards,\n"
                    + "WineShop mk";

    public static final String RESET_PASSWORD_SUBJECT = "Password reset";
    public static final String RESET_PASSWORD_CONTENT =
            "Dear " + "%s,\n\n"
                    + "A password reset has benn requested. Please click on the link to reset your password: \n\n"
                    + "%s/%s,\n\n\n"
                    + "Best regards,\n"
                    + "WineShop mk";

    @Override
    public List<User> getUsers() {
        List<User> users = userRepository.findAll();

        return users;
    }

    @Override
    public User getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id: %s not found", userId)));

        return user;
    }

    @Override
    public User getUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with email: %s not found", email)));
        return user;
    }

    @Override
    public User createUser(UserDTO userDTO) throws MessagingException {
        if (userRepository.existsUserByUsername(userDTO.getUsername())) {
            throw new RuntimeException(String.format("User with this username: %s already exists", userDTO.getUsername()));
        }
        if (userRepository.existsUserByEmail(userDTO.getEmail())) {
            throw new RuntimeException(String.format("User with this email: %s already exists", userDTO.getEmail()));
        }
        String newPassword = UUID.randomUUID().toString();

        User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(encoder.encode(newPassword))
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .role(userDTO.getRole())
                .dateCreated(LocalDateTime.now())
                .build();

        user = userRepository.save(user);

        AuthToken authToken = authTokenService.createAuthToken(user.getId(), "CREATE_USER");

        emailService.sendEmail(CREATE_USER_SUBJECT, user.getEmail(),
                String.format(CREATE_USER_CONTENT, user.getUsername(), url, authToken.getToken()));
        return user;
    }

    @Override
    public User signUp(UserDTO userDTO) {
        if (userRepository.existsUserByUsername(userDTO.getUsername())) {
            throw new RuntimeException(String.format("User with this username: %s already exists", userDTO.getUsername()));
        }
        if (userRepository.existsUserByEmail(userDTO.getEmail())) {
            throw new RuntimeException(String.format("User with this email: %s already exists", userDTO.getEmail()));
        }

        User user = User.builder()
                .email(userDTO.getEmail())
                .username(userDTO.getUsername())
                .password(encoder.encode(userDTO.getPassword()))
                .name(userDTO.getName())
                .surname(userDTO.getSurname())
                .role(Role.ROLE_USER)
                .dateCreated(LocalDateTime.now())
                .build();

        userRepository.save(user);
        return user;
    }

    @Override
    public User updateUser(UserDTO userDTO) {
        User userToUpdate = this.getUserById(userDTO.getId());
        userToUpdate.setEmail(userDTO.getEmail());
        userToUpdate.setName(userDTO.getName());
        userToUpdate.setSurname(userDTO.getSurname());
        userToUpdate.setUsername(userDTO.getUsername());
        userToUpdate.setPassword(userDTO.getPassword());
        userToUpdate.setRole(userDTO.getRole());

        userRepository.save(userToUpdate);

        return userToUpdate;
    }

    @Override
    public User changeUserPassword(User user, String newPassword) {
        String encodedPassword = encoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userRepository.save(user);

        return user;
    }

    @Override
    public void deleteUserById(Long userId) {
        User user = this.getUserById(userId);
        userRepository.delete(user);
    }

    @Override
    public void resetUserPassword(User user) {
        AuthToken authToken = authTokenService.findByUserId(user.getId());

        if (Objects.isNull(authToken)) {
            authToken = authTokenService.createAuthToken(user.getId(), "RESET_PASSWORD");
        } else {
            authToken = authTokenService.updateAuthToken(authToken);
        }

        try {
            emailService.sendEmail(RESET_PASSWORD_SUBJECT, user.getEmail(),
                    String.format(RESET_PASSWORD_CONTENT, user.getUsername(), url, authToken.getToken()));
        } catch (MessagingException exception) {
        }
    }

    @Override
    public void createPostman(User user, String city) {
        Postman pc = new Postman(user, city);
        this.postmanRepository.save(pc);
    }
}
