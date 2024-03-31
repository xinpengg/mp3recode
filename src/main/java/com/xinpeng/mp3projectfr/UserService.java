package com.xinpeng.mp3projectfr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void register(User user) {
        userRepository.save(user);
    }

    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
