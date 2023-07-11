package com.ERP.Softronix_ERP.service;

import com.ERP.Softronix_ERP.exception.EmailExistException;
import com.ERP.Softronix_ERP.exception.EmailNotFoundException;
import com.ERP.Softronix_ERP.exception.UserNotFoundException;
import com.ERP.Softronix_ERP.exception.UsernameExistException;
import com.ERP.Softronix_ERP.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String userName, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
    User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
    void deleteUser(Long id);
    void resetPassword(String email) throws MessagingException, EmailNotFoundException;
    User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException;
}
