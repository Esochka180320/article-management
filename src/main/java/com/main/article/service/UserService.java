package com.main.article.service;

import com.main.article.model.User;
import com.main.article.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User authenticate(User user) throws AuthenticationException {
        Optional<User> optionalUser = userRepository.findByUsername(user.getUsername());

        if (optionalUser.isEmpty()) {
            throw new AuthenticationException();
        }

        User foundUser = optionalUser.get();

        if (!user.getPassword().equals(foundUser.getPassword())) {
            throw new AuthenticationException();
        }

        return foundUser;
    }
}
