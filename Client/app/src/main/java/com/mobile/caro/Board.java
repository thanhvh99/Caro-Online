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

    public int checkBoard(int x, int y) {
        int xTemp;
        int yTemp;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int count = 0;
                for (int a = 0; a < 5; a++) {
                    xTemp = x + a * i;
                    yTemp = y + a * j;
                    if (isInRange(xTemp, yTemp) && matrix[yTemp][xTemp] == matrix[y][x]) {
                        count++;
                    } else {
                        break;
                    }
                }
                if (count == 5) {
                    return matrix[y][x];
                }
            }
        }
        return VALUE_EMPTY;
    }

    public int checkBoard() {
        int size = matrix.length;
        int x;
        int y;
        int xCount;
        int oCount;

        for (int i = 0; i < size; i++) {
            x = i;
            y = 0;
            xCount = 0;
            oCount = 0;
            while (isInRange(x, y)) {
                switch (matrix[y][x]) {
                    case VALUE_EMPTY:
                        xCount = 0;
                        oCount = 0;
                        break;
                    case VALUE_O:
                        oCount++;
                        xCount = 0;
                        break;
                    case VALUE_X:
                        oCount = 0;
                        xCount++;
                        break;
                }
                if (oCount == 5) {
                    return VALUE_O;
                }
                if (xCount == 5) {
                    return VALUE_X;
                }
                y++;
            }

            x = 0;
            y = i;
            xCount = 0;
            oCount = 0;
            while (isInRange(x, y)) {
                switch (matrix[y][x]) {
                    case VALUE_EMPTY:
                        xCount = 0;
                        oCount = 0;
                        break;
                    case VALUE_O:
                        oCount++;
                        xCount = 0;
                        break;
                    case VALUE_X:
                        oCount = 0;
                        xCount++;
                        break;
                }
                if (oCount == 5) {
                    return VALUE_O;
                }
                if (xCount == 5) {
                    return VALUE_X;
                }
                x++;
            }
        }

        for (int i = 0; i < size; i++) {
            x = i;
            y = 0;
            xCount = 0;
            oCount = 0;
            while (isInRange(x, y)) {
                switch (matrix[y][x]) {
                    case VALUE_EMPTY:
                        xCount = 0;
                        oCount = 0;
                        break;
                    case VALUE_O:
                        oCount++;
                        xCount = 0;
                        break;
                    case VALUE_X:
                        oCount = 0;
                        xCount++;
                        break;
                }
                if (oCount == 5) {
                    return VALUE_O;
                }
                if (xCount == 5) {
                    return VALUE_X;
                }
                x++;
                y++;
            }

            x = 0;
            y = i;
            xCount = 0;
            oCount = 0;
            while (isInRange(x, y)) {
                switch (matrix[y][x]) {
                    case VALUE_EMPTY:
                        xCount = 0;
                        oCount = 0;
                        break;
                    case VALUE_O:
                        oCount++;
                        xCount = 0;
                        break;
                    case VALUE_X:
                        oCount = 0;
                        xCount++;
                        break;
                }
                if (oCount == 5) {
                    return VALUE_O;
                }
                if (xCount == 5) {
                    return VALUE_X;
                }
                x++;
                y++;
            }

            x = size - i - 1;
            y = 0;
            xCount = 0;
            oCount = 0;
            while (isInRange(x, y)) {
                switch (matrix[y][x]) {
                    case VALUE_EMPTY:
                        xCount = 0;
                        oCount = 0;
                        break;
                    case VALUE_O:
                        oCount++;
                        xCount = 0;
                        break;
                    case VALUE_X:
                        oCount = 0;
                        xCount++;
                        break;
                }
                if (oCount == 5) {
                    return VALUE_O;
                }
                if (xCount == 5) {
                    return VALUE_X;
                }
                x--;
                y++;
            }

            x = size - 1;
            y = i;
            xCount = 0;
            oCount = 0;
            while (isInRange(x, y)) {
                switch (matrix[y][x]) {
                    case VALUE_EMPTY:
                        xCount = 0;
                        oCount = 0;
                        break;
                    case VALUE_O:
                        oCount++;
                        xCount = 0;
                        break;
                    case VALUE_X:
                        oCount = 0;
                        xCount++;
                        break;
                }
                if (oCount == 5) {
                    return VALUE_O;
                }
                if (xCount == 5) {
                    return VALUE_X;
                }
                x--;
                y++;
            }
        }
        return VALUE_EMPTY;
    }

    private boolean isInRange(int x, int y) {
        return x >= 0 && x < matrix.length && y >= 0 && y < matrix.length;
    }
}
