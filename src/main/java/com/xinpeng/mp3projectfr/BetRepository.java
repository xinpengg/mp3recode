package com.xinpeng.mp3projectfr;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BetRepository extends JpaRepository<Bet, Long> {
    // You can define custom database queries here if needed
}
