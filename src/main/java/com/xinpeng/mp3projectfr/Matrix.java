package com.xinpeng.mp3projectfr;

public class Matrix {
    static class Cell {
        int value;

        Cell(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return Integer.toString(value);
        }
    }
    public void idk() {
        Cell[][] matrix = {
                { new Cell(1), new Cell(2), new Cell(3) },
                { new Cell(4), new Cell(5), new Cell(6) },
                { new Cell(7), new Cell(8), new Cell(9) }
        };

        // Row-major order traversal using nested for loops
        System.out.println("Row-major order traversal:");
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }

        // Column-major order traversal
        System.out.println("\nColumn-major order traversal:");
        for (int i = 0; i < matrix[0].length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                System.out.print(matrix[j][i] + " ");
            }
            System.out.println();
        }

        // Row-major order traversal using enhanced for loops
        System.out.println("\nRow-major order traversal with enhanced for loops:");
        for (Cell[] row : matrix) {
            for (Cell cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

    }
}
