package com.xinpeng.mp3projectfr;

class Car extends Vehicle {


        private int doors;

        Car(String brand, int doors) {
            super(brand);
            this.doors = doors;
        }

        @Override
        public void honk() {
            super.honk(); // Calls the superclass method
            System.out.println("Car honk!");
        }

        @Override
        public String toString() {
            return "Car(brand=" + getBrand() + ", doors=" + doors + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (!super.equals(obj)) return false;
            if (getClass() != obj.getClass()) return false;
            Car car = (Car) obj;
            return doors == car.doors;
        }
    }
}
