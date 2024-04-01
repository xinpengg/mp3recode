package com.xinpeng.mp3projectfr;

public class Market {
    private double yesLiquidity;
    private double noLiquidity;

    public Market() {
        this.yesLiquidity = 0;
        this.noLiquidity = 0;
    }
    public Market(int liquidites) {
        this.yesLiquidity = liquidites;
        this.noLiquidity = liquidites;
    }

    public Market(double yesLiquidity, double noLiquidity) {
        this.yesLiquidity = yesLiquidity;
        this.noLiquidity = noLiquidity;

    }

    public double buyShares(String outcome, int shares) {
        outcome = outcome.toUpperCase();

        double cost = 0;
        double k = yesLiquidity * noLiquidity;

        for (int i = 0; i < shares; i++) {
            if ("YES".equalsIgnoreCase(outcome)) {
                yesLiquidity += 1;
                noLiquidity = k / yesLiquidity;
            } else if ("NO".equalsIgnoreCase(outcome)) {
                noLiquidity += 1;
                yesLiquidity = k / noLiquidity;
            } else {
                return -1;
            }

            double pricePerShare = getPrice(outcome);
            cost += pricePerShare;
        }

        double fee = cost * 0.001;
        cost += fee;

        return cost;
    }


    public double getPrice(String outcome) {
        double totalLiquidity = yesLiquidity + noLiquidity;
        // Debugging print statements
        return "YES".equalsIgnoreCase(outcome) ? yesLiquidity / totalLiquidity : noLiquidity / totalLiquidity;
    }

    public void updatePrices(double totalLiquidity) {
        yesLiquidity = totalLiquidity * (yesLiquidity / (yesLiquidity + noLiquidity));
        noLiquidity = totalLiquidity * (noLiquidity / (yesLiquidity + noLiquidity));
    }
    public double getYesPrice() {
        double yesLiquidity = getYesLiquidity();
        double noLiquidity = getNoLiquidity();
        double totalLiquidity = yesLiquidity + noLiquidity;
        return yesLiquidity / totalLiquidity;
    }

    public double getNoPrice() {
        double yesLiquidity = getYesLiquidity();
        double noLiquidity = getNoLiquidity();
        double totalLiquidity = yesLiquidity + noLiquidity;
        return noLiquidity / totalLiquidity;
    }


    public double sellShares(String outcome, int shares) {
        double cost = 0;
        double k = yesLiquidity * noLiquidity; // Constant product

        for (int i = 0; i < shares; i++) {
            if ("YES".equalsIgnoreCase(outcome)) {
                if (yesLiquidity <= 1) {
                    return -1; // Not enough liquidity to sell
                }
                yesLiquidity -= 1;
                noLiquidity = k / yesLiquidity;
            } else if ("NO".equalsIgnoreCase(outcome)) {
                if (noLiquidity <= 1) {
                    return -1; // Not enough liquidity to sell
                }
                noLiquidity -= 1;
                yesLiquidity = k / noLiquidity;
            } else {
                return -1;
            }

            double pricePerShare = getPrice(outcome);
            cost += pricePerShare;
        }

        double fee = cost * 0.001;
        cost -= fee;

        return cost;
    }

    public double getYesLiquidity() {
        return yesLiquidity;
    }
    public double getNoLiquidity() {
        return noLiquidity;
    }
}