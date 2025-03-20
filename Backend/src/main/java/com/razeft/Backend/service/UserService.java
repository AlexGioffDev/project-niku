package com.razeft.Backend.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.razeft.Backend.dto.UserDTO;
import com.razeft.Backend.entity.User;
import com.razeft.Backend.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public ResponseEntity<Map<String, Object>> register(UserDTO userDTO) {

        User user = new User();

        user.setUsername(userDTO.getUsername());
        user.setPassword(encoder.encode(userDTO.getPassword()));
        user.setRole("USER");

        User savedUser = userRepository.save(user);

        Map<String, Object> response = new HashMap<>();
        response.put("user", savedUser);
        response.put("message", "User registered successfully");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, Object>> verify(UserDTO userDTO) {
        Map<String, Object> response = new HashMap<>();
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

            User user = (User) authentication.getPrincipal();

            String token = jwtService.generateToken(user);
            Date expirationDate = jwtService.getExpirationDate(token);

            response.put("token", token);
            response.put("expirationDate", expirationDate);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "Invalid credentials");
            return ResponseEntity.status(401).body(response);
        }
    }

}
