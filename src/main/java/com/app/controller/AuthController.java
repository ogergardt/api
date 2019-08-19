package com.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.entity.User;
import com.app.exception.DuplicateException;
import com.app.exception.InvalidPasswordException;
import com.app.exception.UserNotFoundException;
import com.app.security.auth.JwtAuthenticationRequest;
import com.app.security.auth.JwtAuthenticationResponse;
import com.app.security.auth.JwtUser;
import com.app.security.auth.JwtUtil;
import com.app.service.UserService;


@RestController
public class AuthController extends BaseController {

    @Value("${auth.header}")
    private String tokenHeader;

    public final static String SIGNUP_URL = "/api/auth/signup";
    public final static String SIGNIN_URL = "/api/auth/signin";
    public final static String REFRESH_TOKEN_URL = "/api/auth/token/refresh";

    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    private UserDetailsService userDetailsService;
    private UserService userService;

    /**
     * Injects AuthenticationManager instance
     * @param authenticationManager to inject
     */
    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    /**
     * Injects JwtUtil instance
     * @param jwtUtil to inject
     */
    @Autowired
    public void setJwtTokenUtil(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Bean
    private PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @PostMapping(SIGNUP_URL)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
            throws AuthenticationException {

        String name = authenticationRequest.getUsername();
        String email = authenticationRequest.getEmail();
        String password = authenticationRequest.getPassword();
        LOG.info("[POST] CREATING TOKEN FOR User " + name + " [" + email + "]");
        String role  = new String("user"); //enum: [ "user", "admin" ]
        JwtUser userDetails;
        try {
        	userService.save(new User( name, email, password, role ));
            userDetails = (JwtUser) userDetailsService.loadUserByUsername(name);
        } catch (DuplicateKeyException ex) {
        	LOG.error(ex.getMessage());
        	throw new DuplicateException();
        } catch (UsernameNotFoundException ex) {
            LOG.error(ex.getMessage());
            throw new UserNotFoundException();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(name, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping(SIGNIN_URL)
    public ResponseEntity<?> getAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
            throws AuthenticationException {

        String name = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        LOG.info("[POST] GETTING TOKEN FOR User " + name);
        JwtUser userDetails;

        try {
            userDetails = (JwtUser) userDetailsService.loadUserByUsername(name);
        } catch (UsernameNotFoundException ex) {
            LOG.error(ex.getMessage());
            throw new UserNotFoundException();
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
            return new ResponseEntity<>(ex, HttpStatus.BAD_REQUEST);
        }

        if(!passwordEncoder().matches(password, userDetails.getPassword())) {
            throw new InvalidPasswordException();
        }

        final Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(name, password)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(token));
    }

    @PostMapping(REFRESH_TOKEN_URL)
    public ResponseEntity<JwtAuthenticationResponse> refreshAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        LOG.info("[POST] REFRESHING TOKEN");
        String refreshedToken = jwtUtil.refreshToken(token);
        return ResponseEntity.ok(new JwtAuthenticationResponse(refreshedToken));
    }
    
}
