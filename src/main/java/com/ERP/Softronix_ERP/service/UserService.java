package com.ERP.Softronix_ERP.service;

import com.ERP.Softronix_ERP.exception.EmailExistException;
import com.ERP.Softronix_ERP.exception.UserNotFoundException;
import com.ERP.Softronix_ERP.exception.UsernameExistException;
import com.ERP.Softronix_ERP.model.User;

import javax.mail.MessagingException;
import java.util.List;

public interface UserService {
    User register(String firstName, String lastName, String userName, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException;
    List<User> getUsers();
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}
