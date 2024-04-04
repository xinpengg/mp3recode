package com.xinpeng.mp3projectfr;

public class Truck extends Vehicle {
    private double payload;

    public Truck(String brand, double payload) {
        super(brand); // Implicitly calls superclass's no-argument constructor
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "Truck(brand=" + getBrand() + ", payload=" + payload + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Truck truck = (Truck) obj;
        return Double.compare(truck.payload, payload) == 0;
    }
}
