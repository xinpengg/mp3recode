package com.xinpeng.mp3projectfr;

public class Vehicle {
    private String brand;
    public Vehicle(String brand) {
        this.brand = brand;
        System.out.println("Vehicle constructor called");
    }

    public Vehicle() {
        System.out.println("Vehicle constructor called");
    }

    public void honk() {
        System.out.println("Vehicle honk!");
    }


    public String toString() {
        return "Vehicle(brand=" + brand + ")";
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return brand.equals(vehicle.brand);
    }
    public String getBrand() {
        return brand;
    }
}
