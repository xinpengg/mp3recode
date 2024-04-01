package com.xinpeng.mp3projectfr;

public class ResolveBet {

    private Long betId; // Assuming betId is of type Long
    private String winningOutcome;

    // Empty constructor is often required for form-backing beans
    public ResolveBet() {
    }

    // Getters and setters
    public Long getBetId() {
        return betId;
    }

    public void setBetId(Long betId) {
        this.betId = betId;
    }

    public String getWinningOutcome() {
        return winningOutcome;
    }

    public void setWinningOutcome(String winningOutcome) {
        this.winningOutcome = winningOutcome;
    }
}
