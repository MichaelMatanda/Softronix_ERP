package com.ERP.Softronix_ERP.service.Impl;

import com.ERP.Softronix_ERP.enums.Role;
import com.ERP.Softronix_ERP.exception.EmailExistException;
import com.ERP.Softronix_ERP.exception.EmailNotFoundException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import static com.ERP.Softronix_ERP.constants.FileConstants.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(userName);
        user.setEmail(email);
        user.setJoinDate(new Date());
        user.setPassword(encodePassword(password));
        user.setActive(true);
        user.setNotLocked(true);
        user.setRole(Role.ROLE_USER.name());
        user.setAuthorities(Role.ROLE_USER.getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImpageUrl(userName));
        userRepository.save(user);
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>New user password: {}", password);
//        emailService.sendNewPasswordEmail(firstName, password, email);
        return user;
    }
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findUserByUsername(username);
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    @Override
    public User addNewUser(String firstName, String lastName, String username, String email, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException {
        validateNewUsernameAndEmail(StringUtils.EMPTY, username, email );
        User user = new User();
        String password = generatePassword();
        user.setUserId(generateUserId());
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setJoinDate(new Date());
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(encodePassword(password));
        user.setActive(isActive);
        user.setNotLocked(isNotLocked);
        user.setRole(getRoleEnumName(role).name());
        user.setAuthorities(getRoleEnumName(role).getAuthorities());
        user.setProfileImageUrl(getTemporaryProfileImpageUrl(username));
        userRepository.save(user);
        saveProfileImage(user, profileImage);
        return user;
    }
    @Override
    public User updateUser(String currentUsername, String newFirstName, String newLastName, String newUsername, String newEmail, String role, boolean isNotLocked, boolean isActive, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException {;
        User currentUser = validateNewUsernameAndEmail(currentUsername, newUsername, newEmail);
        currentUser.setFirstName(newFirstName);
        currentUser.setLastName(newLastName);
        currentUser.setUsername(newUsername);
        currentUser.setEmail(newEmail);
        currentUser.setActive(isActive);
        currentUser.setNotLocked(isNotLocked);
        currentUser.setRole(getRoleEnumName(role).name());
        userRepository.save(currentUser);
        saveProfileImage(currentUser, profileImage);
        return currentUser;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void resetPassword(String email) throws MessagingException, EmailNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if (user == null){
            throw new EmailNotFoundException("No user found by email: " + email);
        }
        String password = generatePassword();
        user.setPassword(encodePassword(password));
        userRepository.save(user);
        emailService.sendNewPasswordEmail(user.getFirstName(), password, user.getEmail());
    }
    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User updateProfileImage(String username, MultipartFile profileImage) throws UserNotFoundException, EmailExistException, UsernameExistException, IOException {
        User user = validateNewUsernameAndEmail(username, null, null);
        saveProfileImage(user, profileImage);
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

    private String getTemporaryProfileImpageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/image/profile/temp" + username).toUriString();
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
    private void saveProfileImage(User user, MultipartFile profileImage) throws IOException {
        if (profileImage != null){
            Path userFolder = Paths.get(USER_FOLDER + user.getUsername()).toAbsolutePath().normalize();
            if (Files.exists(userFolder)){
                Files.createDirectories(userFolder);
                log.info("Directory created:{}", DIRECTORY_CREATED + userFolder);
            }
            Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + DOT + JPG_EXTENSION));
            Files.copy(profileImage.getInputStream(), userFolder.resolve(user.getUsername() + DOT + JPG_EXTENSION), REPLACE_EXISTING);
            user.setProfileImageUrl(setProfileImageUrl(user.getUsername()));
            userRepository.save(user);
            log.info("File saved in file system:{}", profileImage.getOriginalFilename());
        }
    }

    private String setProfileImageUrl(String username) {
        return ServletUriComponentsBuilder.fromCurrentContextPath().path(USER_IMAGE_PATH + username + FORWARD_SLASH + username + DOT + JPG_EXTENSION).toUriString();

    }

    private Role getRoleEnumName(String role) {
        return Role.valueOf(role.toUpperCase());
    }
}
