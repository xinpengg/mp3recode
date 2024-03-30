package com.xinpeng.mp3projectfr;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final List<User> users;

    public UserService() {
        this.users = new ArrayList<>();
    }

    public void register(User user) {
        users.add(user);
    }

    public User findUser(String username) {
        for (User user : users) {
            if (user.getId().equals(username)) {
                return user;
            }
        }
        return null;
    }
}
