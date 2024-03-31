package com.xinpeng.mp3projectfr;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    // Here, String is the type of User's id. Adjust if necessary.
}
