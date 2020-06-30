package com.mobile.caro.Board;

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

    public Board(Board board) {
        int[][] source = board.getMatrix();
        matrix = new int[source.length][source.length];
        for (int i = 0; i < matrix.length; i++) {
            System.arraycopy(source[i], 0, matrix[i], 0, matrix.length);
        }
    }

    public int getLastMove() {
        if (history.isEmpty()) {
            return -1;
        } else {
            return history.get(history.size() - 1);
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

    public boolean isOngoing() {
        return status == Status.ONGOING;
    }

    public boolean isFirstPlayerTurn() {
        return firstPlayerTurn;
    }

    public boolean select(int x, int y) {
        if (!isInRange(x, y) || !isEmptyAt(x, y)) {
            return false;
        }
        matrix[y][x] = firstPlayerTurn ? VALUE_X : VALUE_O;
        availableMove--;
        history.add(y * getSize() + x);
        firstPlayerTurn = !firstPlayerTurn;
        if (checkBoard(x, y)) {
            status = firstPlayerTurn ? Status.P2_WIN : Status.P1_WIN;
            return true;
        }
        if (availableMove == 0) {
            status = Status.EVEN;
        }
        return true;
    }

    public int getTotalMoves() {
        return history.size();
    }

    public void undo() {
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
        for (int i = -1; i <= 1; i++) {
            for (int j = 0; j <= 1; j++) {
                if ((i == 0 && j == 0)) {
                    continue;
                }
                int count = 1;
                for (int a = 1; a < 5; a++) {
                    int tempX = x + a * i;
                    int tempY = y + a * j;
                    if (isInRange(tempX, tempY) && matrix[tempY][tempX] == matrix[y][x]) {
                        count++;
                    } else {
                        break;
                    }
                }
                for (int a = 1; a < 5; a++) {
                    int tempX = x - a * i;
                    int tempY = y - a * j;
                    if (isInRange(tempX, tempY) && matrix[tempY][tempX] == matrix[y][x]) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count >= 5) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isInRange(int x, int y) {
        return x >= 0 && x < matrix.length && y >= 0 && y < matrix.length;
    }

    public Status getStatus() {
        return status;
    }


}