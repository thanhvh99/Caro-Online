package com.mobile.caro;

public class Board {

    public static final int VALUE_NULL = 0;
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
}
