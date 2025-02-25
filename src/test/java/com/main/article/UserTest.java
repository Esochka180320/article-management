package com.main.article;

import com.main.article.model.User;
import com.main.article.model.UserRole;
import com.main.article.repository.UserRepository;
import com.main.article.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.naming.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        // Ініціалізація моків
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        user.setRole(UserRole.USER);
    }

    @Test
    void testCreateUser() {
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals(user.getUsername(), createdUser.getUsername());
        assertEquals(user.getPassword(), createdUser.getPassword());
    }

    @Test
    void testAuthenticateUserSuccess() throws AuthenticationException {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(user));

        User authenticatedUser = userService.authenticate(user);

        assertNotNull(authenticatedUser);
        assertEquals(user.getUsername(), authenticatedUser.getUsername());
    }

    @Test
    void testAuthenticateUserFailure() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.empty());

        assertThrows(AuthenticationException.class, () -> userService.authenticate(user));
    }

    @Test
    void testAuthenticateWrongPassword() {
        User foundUser = new User();
        foundUser.setUsername("testUser");
        foundUser.setPassword("wrongPassword");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(java.util.Optional.of(foundUser));

        assertThrows(AuthenticationException.class, () -> userService.authenticate(user));
    }

    @Test
    void testDeleteUser() {
        doNothing().when(userRepository).deleteById(anyLong());

        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).deleteById(user.getId());
    }
}
