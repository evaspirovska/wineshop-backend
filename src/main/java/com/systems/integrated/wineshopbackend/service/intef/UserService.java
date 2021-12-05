package com.systems.integrated.wineshopbackend.service.intef;

import com.systems.integrated.wineshopbackend.models.users.DTO.UserDTO;
import com.systems.integrated.wineshopbackend.models.users.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {

    List<User> getUsers();

    User getUserById(Long userId);

    User getUserByEmail(String email);

    User createUser(UserDTO newUser) throws MessagingException;

    User signUp(UserDTO newUser);

    User updateUser(UserDTO userDTO);

    User changeUserPassword (User User, String newPassword);

    void deleteUserById(Long userId);

    void resetUserPassword(User user);
}
