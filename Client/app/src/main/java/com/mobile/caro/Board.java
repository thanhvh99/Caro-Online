package com.mobile.caro;

public class Board {

    public static final int DEFAULT_SIZE = 19;

    public static final int VALUE_EMPTY = 0;
    public static final int VALUE_X = 1;
    public static final int VALUE_O = 2;

    private int[][] matrix;

    public Board(int size) {
        matrix = new int[size][size];
    }

    public Board(int[][] matrix) {
        this.matrix = matrix;
    }

    public int getSize() {
        return matrix.length;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public boolean isEmptyAt(int x, int y) {
        return isInRange(x, y) && matrix[y][x] == VALUE_EMPTY;
    }

    public void setValueAt(int x, int y, int value) {
        matrix[y][x] = value;
    }

    private boolean isInRange(int x, int y) {
        return x >= 0 && x < matrix.length && y >= 0 && y < matrix.length;
    }
}
