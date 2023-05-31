package com.ERP.Softronix_ERP.controller;

import com.ERP.Softronix_ERP.Security.JWTUtility.JWTTokenProvider;
import com.ERP.Softronix_ERP.exception.EmailExistException;
import com.ERP.Softronix_ERP.exception.ExceptionHandling;
import com.ERP.Softronix_ERP.exception.UserNotFoundException;
import com.ERP.Softronix_ERP.exception.UsernameExistException;
import com.ERP.Softronix_ERP.model.User;
import com.ERP.Softronix_ERP.model.UserPrincipal;
import com.ERP.Softronix_ERP.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

import static com.ERP.Softronix_ERP.constants.SecurityConstants.JWT_TOKEN_HEADER;

@RestController
@RequestMapping(path = {"/", "/user"})
public class UserController extends ExceptionHandling {
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, AuthenticationManager authenticationManager, JWTTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        authenticate(user.getUsername(), user.getPassword());
        User loginUser = userService.findUserByUsername(user.getUsername());
        UserPrincipal userPrincipal = new UserPrincipal(loginUser);
        HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        return new ResponseEntity<>(loginUser, jwtHeader, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) throws EmailExistException, UserNotFoundException, UsernameExistException, MessagingException {
        User newUser = userService.register(user.getFirstName(), user.getLastName(), user.getUsername(), user.getEmail());
        return new ResponseEntity<>(newUser, HttpStatus.OK);
    }

    private HttpHeaders getJwtHeader(UserPrincipal userPrincipal) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(userPrincipal));
        return headers;
    }

    private void authenticate(String username, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}
