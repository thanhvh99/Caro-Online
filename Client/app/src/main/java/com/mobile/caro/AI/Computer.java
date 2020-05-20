package com.mobile.caro.AI;

import android.graphics.Point;

import com.mobile.caro.Board.Board;

import java.util.ArrayList;

public class Computer {

    private EvalBoard eBoard;
    private Board board;
    private int x, y;

    private int maxDepth = 3;
    private int maxMove = 4;

    private int[] AScore = {0, 4, 27, 256, 1458};
    private int[] DScore = {0, 2, 9, 99, 769};

    private Point goPoint;

    public Computer(Board board) {
        this.board = board;
        this.eBoard = new EvalBoard(board.getSize());
    }

    private void evalChessBoard(boolean isPlayer) {
        int row, col;
        int ePC, eHuman;
        eBoard.resetBoard();

        for (row = 0; row < eBoard.size; row++) {
            for (col = 0; col < eBoard.size - 4; col++) {
                ePC = 0;
                eHuman = 0;
                for (int i = 0; i < 5; i++) {
                    if (board.getValueAt(col + i, row) == Board.VALUE_X) {
                        eHuman++;
                    }
                    if (board.getValueAt(col + i, row) == Board.VALUE_O) {
                        ePC++;
                    }
                }
                if (eHuman * ePC == 0 && eHuman != ePC) {
                    for (int i = 0; i < 5; i++) {
                        if (board.getValueAt(col + i, row) == Board.VALUE_BLANK) {
                            if (eHuman == 0) {
                                if (isPlayer) {
                                    eBoard.EBoard[row][col + i] += DScore[ePC];
                                } else {
                                    eBoard.EBoard[row][col + i] += AScore[ePC];
                                }
                            }
                            if (ePC == 0) {
                                if (isPlayer) {
                                    eBoard.EBoard[row][col + i] += AScore[eHuman];
                                } else {
                                    eBoard.EBoard[row][col + i] += DScore[eHuman];
                                }
                            }
                            if (eHuman == 4 || ePC == 4) {
                                eBoard.EBoard[row][col + i] *= 2;
                            }
                        }
                    }
                }
            }
        }

        for (row = 0; row < eBoard.size - 4; row++) {
            for (col = 0; col < eBoard.size; col++) {
                ePC = 0;
                eHuman = 0;
                for (int i = 0; i < 5; i++) {
                    if (board.getValueAt(col, row + i) == Board.VALUE_X) {
                        eHuman++;
                    }
                    if (board.getValueAt(col, row + i) == Board.VALUE_O) {
                        ePC++;
                    }
                }
                if (eHuman * ePC == 0 && eHuman != ePC)
                    for (int i = 0; i < 5; i++) {
                        if (board.getValueAt(col, row + i) == Board.VALUE_BLANK) {
                            if (eHuman == 0) {
                                if (isPlayer) {
                                    eBoard.EBoard[row + i][col] += DScore[ePC];
                                } else {
                                    eBoard.EBoard[row + i][col] += AScore[ePC];
                                }
                            }
                            if (ePC == 0) {
                                if (isPlayer) {
                                    eBoard.EBoard[row + i][col] += AScore[eHuman];
                                } else {
                                    eBoard.EBoard[row + i][col] += DScore[eHuman];
                                }
                            }
                            if (eHuman == 4 || ePC == 4)
                                eBoard.EBoard[row + i][col] *= 2;
                        }

                    }
            }
        }

        for (row = 0; row < eBoard.size - 4; row++) {
            for (col = 0; col < eBoard.size - 4; col++) {
                ePC = 0;
                eHuman = 0;
                for (int i = 0; i < 5; i++) {
                    if (board.getValueAt(col + i, row + i) == Board.VALUE_X) {
                        eHuman++;
                    }
                    if (board.getValueAt(col + i, row + i) == Board.VALUE_O) {
                        ePC++;
                    }
                }
                if (eHuman * ePC == 0 && eHuman != ePC) {
                    for (int i = 0; i < 5; i++) {
                        if (board.getValueAt(col + i, row + i) == Board.VALUE_BLANK) // Neu o chua duoc danh
                        {
                            if (eHuman == 0) {
                                if (isPlayer) {
                                    eBoard.EBoard[row + i][col + i] += DScore[ePC];
                                } else {
                                    eBoard.EBoard[row + i][col + i] += AScore[ePC];
                                }
                            }
                            if (ePC == 0) {
                                if (isPlayer) {
                                    eBoard.EBoard[row + i][col + i] += AScore[eHuman];
                                } else {
                                    eBoard.EBoard[row + i][col + i] += DScore[eHuman];
                                }
                            }
                            if (eHuman == 4 || ePC == 4)
                                eBoard.EBoard[row + i][col + i] *= 2;
                        }
                    }
                }
            }
        }

        for (row = 4; row < eBoard.size; row++) {
            for (col = 0; col < eBoard.size - 4; col++) {
                ePC = 0;
                eHuman = 0;
                for (int i = 0; i < 5; i++) {
                    if (board.getValueAt(col + i, row - i) == Board.VALUE_X) {
                        eHuman++;
                    }
                    if (board.getValueAt(col + i, row - i) == Board.VALUE_O) {
                        ePC++;
                    }
                }
                if (eHuman * ePC == 0 && eHuman != ePC) {
                    for (int i = 0; i < 5; i++) {
                        if (board.getValueAt(col + i, row - i) == Board.VALUE_BLANK) {
                            if (eHuman == 0)
                                if (isPlayer) {
                                    eBoard.EBoard[row - i][col + i] += DScore[ePC];
                                } else {
                                    eBoard.EBoard[row - i][col + i] += AScore[ePC];
                                }
                            if (ePC == 0) {
                                if (isPlayer) {
                                    eBoard.EBoard[row - i][col + i] += AScore[eHuman];
                                } else {
                                    eBoard.EBoard[row - i][col + i] += DScore[eHuman];
                                }
                            }
                            if (eHuman == 4 || ePC == 4) {
                                eBoard.EBoard[row - i][col + i] *= 2;
                            }
                        }
                    }
                }
            }
        }
    }

    private int maxValue(int alpha, int beta, int depth) {

        eBoard.maxPos();
        int value = eBoard.evaluationBoard;
        if (depth >= maxDepth) {
            return value;
        }
        evalChessBoard(false);
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < maxMove; i++) {
            Point node = eBoard.maxPos();
            if (node != null) {
                list.add(node);
                eBoard.EBoard[node.y][node.x] = Board.VALUE_BLANK;
            } else {
                break;
            }
        }
        int v = Integer.MIN_VALUE;
        for (int i = 0; i < list.size(); i++) {
            Point com = list.get(i);
            board.setValueAt(com.x, com.y, Board.VALUE_O);
            v = Math.max(v, minValue(alpha, beta, depth+1));
            board.setValueAt(com.x, com.y, Board.VALUE_BLANK);
            if(v >= beta || board.checkBoard(com.x, com.y)) {
                goPoint = com;
                return v;
            }
            alpha = Math.max(alpha, v);
        }

        return v;
    }

    private int minValue(int alpha, int beta, int depth) {
        eBoard.maxPos();
        int value = eBoard.evaluationBoard;
        if (depth >= maxDepth) {
            return value;
        }
        evalChessBoard(true);
        ArrayList<Point> list = new ArrayList<>();
        for (int i = 0; i < maxMove; i++) {
            Point node = eBoard.maxPos();
            if (node != null) {
                list.add(node);
                eBoard.EBoard[node.y][node.x] = Board.VALUE_BLANK;
            } else {
                break;
            }
        }
        int v = Integer.MAX_VALUE;
        for (int i = 0; i < list.size(); i++) {
            Point com = list.get(i);
            board.setValueAt(com.x, com.y, Board.VALUE_X);
            v = Math.min(v, maxValue(alpha, beta, depth + 1));
            board.setValueAt(com.x, com.y, Board.VALUE_BLANK);
            if (v <= alpha || board.checkBoard(com.x, com.y)) {
                return v;
            }
            beta = Math.min(beta, v);
        }
        return v;
    }

    public Point AI() {
        minValue(0, 1, 0);
        Point temp = goPoint;
        if (temp != null) {
            x = temp.x;
            y = temp.y;
        }
        return new Point(x, y);
    }
}
