package com.xinpeng.mp3projectfr;

import java.util.ArrayList;

public class BRUH {
    public void idk() {
        ArrayList<Integer> list = new ArrayList<>();


        list.add(5);
        list.add(3);
        list.add(1, 4); // Using add(int index, E obj)

        System.out.println("Initial List:");
        printList(list);

        list.set(2, 6); // Using set(int index, E obj)

        // Removing an element during traversal
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == 4) { // Using get(int index)
                list.remove(i); // Using remove(int index)
                // Decrement i to adjust for the shifted indices
                i--;
            }
        }

        System.out.println("\nList after removing an element:");
        printList(list);

        // Sorting the list using selection sort
        selectionSort(list);
        System.out.println("\nList after selection sort:");
        printList(list);

        // Adding more elements and sorting using insertion sort for demonstration
        list.add(2);
        list.add(0);
        insertionSort(list);
        System.out.println("\nList after insertion sort:");
        printList(list);
    }

    private static void printList(ArrayList<Integer> list) {
        for (Integer item : list) {
            System.out.println(item);
        }
    }

    private static void selectionSort(ArrayList<Integer> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(j) < list.get(minIndex)) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                int temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }
    }

    private static void insertionSort(ArrayList<Integer> list) {
        for (int i = 1; i < list.size(); i++) {
            int key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j) > key) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    public void idkagain() {
        Vehicle v1 = new Car("Toyota", 4);
        Vehicle v2 = new Truck("Ford", 1000);

        v1.honk();

        Vehicle[] vehicles = {v1, v2};

        // Using an ArrayList of superclass references to store subclass objects
        ArrayList<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(v1);
        vehicleList.add(v2);

        for (Vehicle v : vehicles) {
            System.out.println(v);
        }

        for (Vehicle v : vehicleList) {
            System.out.println(v);
        }
    }
}
