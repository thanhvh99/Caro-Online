package com.mobile.caro.AI;

import android.graphics.Point;

import com.mobile.caro.Board.Board;

public class EvalBoard {

    public int size;
    public int[][] EBoard;
    public int evaluationBoard = 0;
    public EvalBoard(int size) {
        this.size = size;
        EBoard = new int[size][size];
    }

    public void resetBoard() {
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                EBoard[r][c] = Board.VALUE_BLANK;
    }

    public Point maxPos() {
        int max = 0;
        Point p = new Point();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (EBoard[i][j] > max) {
                    p.y = i;
                    p.x = j;
                    max = EBoard[i][j];
                }
            }
        }
        if (max == 0) {
            return null;
        }
        evaluationBoard = max;
        return p;
    }
}
