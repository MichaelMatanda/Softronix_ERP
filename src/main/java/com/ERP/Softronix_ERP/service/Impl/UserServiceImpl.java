package com.ERP.Softronix_ERP.service.Impl;

import com.ERP.Softronix_ERP.enums.Role;
import com.ERP.Softronix_ERP.exception.EmailExistException;
import com.ERP.Softronix_ERP.exception.UserNotFoundException;
import com.ERP.Softronix_ERP.exception.UsernameExistException;
import com.ERP.Softronix_ERP.model.User;
import com.ERP.Softronix_ERP.model.UserPrincipal;
import com.ERP.Softronix_ERP.repository.UserRepository;
import com.ERP.Softronix_ERP.service.EmailService;
import com.ERP.Softronix_ERP.service.LoginAttemptService;
import com.ERP.Softronix_ERP.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
@Qualifier("UserDetailsService")
public class UserServiceImpl implements UserService, UserDetailsService {
    public static final String EMAIL_ALREADY_EXIST = "Email already exist";
    public static final String USERNAME_ALREADY_EXIST = "Username already exist";
    public static final String EMAIL_ALREADY_EXIST1 = "Email already exist";
    public static final String USERNAME_ALREADY_EXIST1 = "Username already exist";
    public static final String NO_USER_FOUND_BY_USERNAME = "No user found by username ";
    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private LoginAttemptService loginAttemptService;
    private EmailService emailService;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username);
        if (user == null){
            log.error("User not found by username: " + username);
            throw new UsernameNotFoundException("User not found by username: "+ username);
        }else {
            validateLoginAttempt(user);
            user.setLastLoginDateDisplay(user.getLastLoginDate());
            user.setLastLoginDate(new Date());
            userRepository.save(user);
            UserPrincipal userPrincipal = new UserPrincipal(user);
            log.info("Returning found user by username: " + username);
            return userPrincipal;
        }
    }

    @Override
    public User register(String firstName, String lastName, String userName, String email) throws UserNotFoundException, EmailExistException, UsernameExistException, MessagingException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, userName, email);
        User user = new User();
        user.setUserId(generateUserId());
        String password = generatePassword();
        String encodedPassword = encodePassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(userName);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImpageUrl());
        userRepository.save(user);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>New user password: {}", password);
//        emailService.sendNewPasswordEmail(firstName, password, email);
        return user;
    }

    private void validateLoginAttempt(User user) {
        if (user.isNotLocked()){
            if (loginAttemptService.hasExceededMaxAttempts(user.getUsername())){
                user.setNotLocked(false);
            }else {
                user.setNotLocked(true);
            }

        }else {
            loginAttemptService.evictUserFromLoginAttemptCache(user.getUsername());
        }
    }

    private String getTemporaryProfileImpageUrl() {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile/temp").toUriString();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    private String generateUserId() {
        return RandomStringUtils.randomNumeric(10);
    }

    private User validateNewUsernameAndEmail(String currentUsername, String newUsername, String newEmail) throws UserNotFoundException, UsernameExistException, EmailExistException {
        if (StringUtils.isNotBlank(currentUsername)){
            User currentUser = findUserByUsername(currentUsername);
            if (currentUser == null){
                throw new UserNotFoundException(NO_USER_FOUND_BY_USERNAME + currentUsername);
            }
            User userByNewUsername = findUserByUsername(newUsername);
            if (userByNewUsername != null && !currentUser.getId().equals(userByNewUsername.getId())){
                throw new UsernameExistException(USERNAME_ALREADY_EXIST1);
            }
            User userByNewEmail = findUserByEmail(newEmail);
            if (userByNewEmail != null && !currentUser.getId().equals(userByNewEmail.getId())){
                throw new EmailExistException(EMAIL_ALREADY_EXIST1);
            }
            return currentUser;
        }else {
            User userByUsername = findUserByUsername(newUsername);
            if (userByUsername != null){
                throw new UsernameExistException(USERNAME_ALREADY_EXIST);
            }
            User userByEmail = findUserByEmail(newEmail);
            if (userByEmail != null){
                throw new EmailExistException(EMAIL_ALREADY_EXIST);
            }
            return null;
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }
}
