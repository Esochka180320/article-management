package com.main.article.controller;

import com.main.article.model.User;
import com.main.article.security.JwtUtil;
import com.main.article.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Користувачі", description = "Контролер для керування користувачами (реєстрація, автентифікація, видалення)")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Operation(summary = "Реєстрація нового користувача", description = "Додає нового користувача в систему")
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Parameter(description = "Дані користувача для реєстрації") @RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @Operation(summary = "Авторизація користувача", description = "Перевіряє облікові дані користувача і повертає JWT токен")
    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(
            @Parameter(description = "Облікові дані користувача") @RequestBody User user) {
        try {
            User authenticatedUser = userService.authenticate(user);
            if (authenticatedUser != null) {
                String token = jwtUtil.generateToken(authenticatedUser.getUsername(), authenticatedUser.getRole());
                return ResponseEntity.ok(token);
            }
            return ResponseEntity.status(401).body("Invalid credentials");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @Operation(summary = "Видалення користувача", description = "Видаляє користувача за його ID")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID користувача, якого потрібно видалити") @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
