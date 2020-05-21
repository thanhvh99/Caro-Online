package com.mobile.caro.Board;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int VALUE_BLANK = 0;
    public static final int VALUE_X = 1;
    public static final int VALUE_O = 2;

    private List<Integer> history = new ArrayList<>();
    private boolean firstPlayerTurn = true;
    private Status status = Status.ONGOING;
    private int availableMove;
    private int[][] matrix;

    public Board(int size) {
        matrix = new int[size][size];
        availableMove = size * size;
    }

    public Point getLastMove() {
        int value = history.get(history.size() - 1);
        return new Point(value % matrix.length, value / matrix.length);
    }

    public Board(Board board) {
        int[][] matrixToCopy = board.getMatrix();
        matrix = new int[matrixToCopy.length][matrixToCopy.length];
        for (int i = 0; i < matrixToCopy.length; i++) {
            System.arraycopy(matrixToCopy[i], 0, matrix[i], 0, matrixToCopy.length);
        }
    }

    public void setValueAt(int x, int y, int value) {
        if (isInRange(x, y)) {
            matrix[y][x] = value;
        }
    }

    public int getSize() {
        return matrix.length;
    }

    public int[][] getMatrix() {
        return matrix;
    }

    public boolean isEmptyAt(int x, int y) {
        return isInRange(x, y) && matrix[y][x] == VALUE_BLANK;
    }

    public int getValueAt(int x, int y) {
        return matrix[y][x];
    }

    public boolean isOngoing() {
        return status == Status.ONGOING;
    }

    public boolean isFirstPlayerTurn() {
        return firstPlayerTurn;
    }

    public boolean select(int x, int y) {
        if (!isOngoing()) {
            return false;
        }

        if (!isInRange(x, y) || !isEmptyAt(x, y)) {
            return false;
        }
        matrix[y][x] = firstPlayerTurn ? VALUE_X : VALUE_O;
        availableMove--;
        history.add(y * getSize() + x);
        if (checkBoard(x, y)) {
            status = firstPlayerTurn ? Status.P1_WIN : Status.P2_WIN;
            return true;
        }
        if (availableMove == 0) {
            status = Status.EVEN;
        }
        firstPlayerTurn = !firstPlayerTurn;
        return true;
    }

    public void undo() {
        if (!isOngoing()) {
            return;
        }
        if (history.isEmpty()) {
            return;
        }
        int lastMove = history.get(history.size() - 1);
        int y = lastMove / getSize();
        int x = lastMove % getSize();
        matrix[y][x] = VALUE_BLANK;
        firstPlayerTurn = !firstPlayerTurn;
        availableMove++;
        history.remove(history.size() - 1);
    }

    public boolean checkBoard(int x, int y) {
        int count;

        // kiểm tra hàng ngang
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (isInRange(x + i, y) && matrix[y][x + i] == matrix[y][x]) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (isInRange(x - i, y) && matrix[y][x - i] == matrix[y][x]) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }

        // kiểm tra hàng dọc
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (isInRange(x, y + i) && matrix[y + i][x] == matrix[y][x]) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (isInRange(x, y - i) && matrix[y - i][x] == matrix[y][x]) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }

        // kiểm tra hàng chéo \
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (isInRange(x + i, y + i) && matrix[y + i][x + i] == matrix[y][x]) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (isInRange(x - i, y - i) && matrix[y - i][x - i] == matrix[y][x]) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }

        // kiểm tra hàng chéo /
        count = 1;
        for (int i = 1; i < 5; i++) {
            if (isInRange(x - i, y + i) && matrix[y + i][x - i] == matrix[y][x]) {
                count++;
            } else {
                break;
            }
        }
        for (int i = 1; i < 5; i++) {
            if (isInRange(x + i, y - i) && matrix[y - i][x + i] == matrix[y][x]) {
                count++;
            } else {
                break;
            }
        }
        if (count >= 5) {
            return true;
        }

        return false;
    }

    public boolean isInRange(int x, int y) {
        return x >= 0 && x < matrix.length && y >= 0 && y < matrix.length;
    }

    public Status getStatus() {
        return status;
    }

    private boolean hasAvailableMove() {
        return availableMove != 0;
    }





}