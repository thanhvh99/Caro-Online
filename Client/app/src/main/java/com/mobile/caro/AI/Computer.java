package com.mobile.caro.AI;

import android.graphics.Point;

import com.mobile.caro.Board.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class Computer {

    private static final int[] ATTACK_POINT = { 0, 20, 200, 2000, 20000 };
    private static final int[] DEFENSE_POINT = { 0, 10, 100, 1000, 10000 };

    private int maxDepth;
    private int maxMove;

    private Point next = new Point();
    private Board board;
    private int value;
    private int size;

    public Computer(Board board, int maxDepth, int maxMove, int value) {
        this.board = board;
        this.maxDepth = maxDepth;
        this.maxMove = maxMove;
        this.value = value;
        size = board.getMatrix().length;
    }

    public void setBoard(Board board) {
        this.board = board;
        size = board.getSize();
    }

    public Point nextPoint() {
        Board temp = board;
        board = new Board(board);
        int[][] matrix = board.getMatrix();
        List<Integer> moves = getPossibleMoves(value);
        int v = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        for (int i = 0; i < moves.size(); i++) {
            int move = moves.get(i);
            int x = move % size;
            int y = move / size;
            matrix[y][x] = value;
            v = Math.max(v, minValue(move, alpha, beta, 1));
            matrix[y][x] = Board.VALUE_BLANK;
            if (v > alpha) {
                next.set(x, y);
                alpha = v;
            }
            if (alpha >= beta) {
                break;
            }
        }
        board = temp;
        return next;
    }

    private int maxValue(int last, int alpha, int beta, int depth) {
        if (board.checkBoard(last % size, last / size)) {
            return Integer.MIN_VALUE / 2;
        }
        if (depth >= maxDepth) {
            return getBoardPoint(value);
        }
        int[][] matrix = board.getMatrix();
        List<Integer> moves = getPossibleMoves(value);
        int v = Integer.MIN_VALUE;
        for (int i = 0; i < moves.size(); i++) {
            int move = moves.get(i);
            int x = move % size;
            int y = move / size;
            matrix[y][x] = value;
            v = Math.max(v, minValue(move, alpha, beta, depth + 1));
            matrix[y][x] = Board.VALUE_BLANK;
            if (v > alpha) {
                alpha = v;
            }
            if (alpha >= beta) {
                break;
            }
        }
        return v;
    }

    private int minValue(int last, int alpha, int beta, int depth) {
        if (board.checkBoard(last % size, last / size)) {
            return Integer.MAX_VALUE / 2;
        }
        if (depth >= maxDepth) {
            return getBoardPoint(value);
        }
        int[][] matrix = board.getMatrix();
        List<Integer> moves = getPossibleMoves(3 - value);
        int v = Integer.MAX_VALUE;
        for (int i = 0; i < moves.size(); i++) {
            int move = moves.get(i);
            int x = move % size;
            int y = move / size;
            matrix[y][x] = 3 - value;
            v = Math.min(v, maxValue(move, alpha, beta, depth + 1));
            matrix[y][x] = Board.VALUE_BLANK;
            if (v < beta) {
                beta = v;
            }
            if (alpha >= beta) {
                break;
            }
        }
        return v;
    }

    private int getBoardPoint(int value) {
        int[][] matrix1 = evalBoard(value);
        int[][] matrix2 = evalBoard(3 - value);

        int result = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                result += matrix1[i][j] - matrix2[i][j];
            }
        }
        return result;
    }

    private List<Integer> getPossibleMoves(int value) {
        int[][] matrix = evalBoard(value);
        PriorityQueue<Move> queue = new PriorityQueue<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (matrix[i][j] != 0) {
                    queue.add(new Move(j, i, matrix[i][j]));
                    if (queue.size() > maxMove) {
                        queue.poll();
                    }
                }
            }
        }
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            Move move = queue.poll();
            result.add(0, move.y * size + move.x);
        }
        return result;
    }

    private int[][] evalBoard(int value) {
        int[][] matrix = this.board.getMatrix();
        int[][] result = new int[size][size];

        int row, col, countX, countO;

        for (row = 0; row < size; row++) {
            for (col = 0; col < size - 4; col++) {
                countX = 0;
                countO = 0;
                for (int i = 0; i < 5; i++) {
                    if (matrix[row][col + i] == Board.VALUE_X) {
                        countX++;
                    }
                    if (matrix[row][col + i] == Board.VALUE_O) {
                        countO++;
                    }
                }
                if (countX * countO == 0 && countX != countO) {
                    for (int i = 0; i < 5; i++) {
                        if (matrix[row][col + i] != Board.VALUE_BLANK) {
                            continue;
                        }
                        if (countX == 0) {
                            if (value == Board.VALUE_X) {
                                result[row][col + i] += DEFENSE_POINT[countO];
                            } else {
                                result[row][col + i] += ATTACK_POINT[countO];
                            }
                        }
                        if (countO == 0) {
                            if (value == Board.VALUE_X) {
                                result[row][col + i] += ATTACK_POINT[countX];
                            } else {
                                result[row][col + i] += DEFENSE_POINT[countX];
                            }
                        }
                        if (countO == 4 || countX == 4) {
                            result[row][col + i] *= 10;
                        }
                    }
                }
            }
        }

        for (row = 0; row < size - 4; row++) {
            for (col = 0; col < size; col++) {
                countX = 0;
                countO = 0;
                for (int i = 0; i < 5; i++) {
                    if (matrix[row + i][col] == Board.VALUE_X) {
                        countX++;
                    }
                    if (matrix[row + i][col] == Board.VALUE_O) {
                        countO++;
                    }
                }
                if (countX * countO == 0 && countX != countO) {
                    for (int i = 0; i < 5; i++) {
                        if (matrix[row + i][col] != Board.VALUE_BLANK) {
                            continue;
                        }
                        if (countX == 0) {
                            if (value == Board.VALUE_X) {
                                result[row + i][col] += DEFENSE_POINT[countO];
                            } else {
                                result[row + i][col] += ATTACK_POINT[countO];
                            }
                        }
                        if (countO == 0) {
                            if (value == Board.VALUE_X) {
                                result[row + i][col] += ATTACK_POINT[countX];
                            } else {
                                result[row + i][col] += DEFENSE_POINT[countX];
                            }
                        }
                        if (countO == 4 || countX == 4) {
                            result[row + i][col] *= 10;
                        }
                    }
                }
            }
        }

        for (row = 0; row < size - 4; row++) {
            for (col = 0; col < size - 4; col++) {
                countX = 0;
                countO = 0;
                for (int i = 0; i < 5; i++) {
                    if (matrix[row + i][col + i] == Board.VALUE_X) {
                        countX++;
                    }
                    if (matrix[row + i][col + i] == Board.VALUE_O) {
                        countO++;
                    }
                }
                if (countX * countO == 0 && countX != countO) {
                    for (int i = 0; i < 5; i++) {
                        if (matrix[row + i][col + i] != Board.VALUE_BLANK) {
                            continue;
                        }
                        if (countX == 0) {
                            if (value == Board.VALUE_X) {
                                result[row + i][col + i] += DEFENSE_POINT[countO];
                            } else {
                                result[row + i][col + i] += ATTACK_POINT[countO];
                            }
                        }
                        if (countO == 0) {
                            if (value == Board.VALUE_X) {
                                result[row + i][col + i] += ATTACK_POINT[countX];
                            } else {
                                result[row + i][col + i] += DEFENSE_POINT[countX];
                            }
                        }
                        if (countO == 4 || countX == 4) {
                            result[row + i][col + i] *= 10;
                        }
                    }
                }
            }
        }

        for (row = 4; row < size; row++) {
            for (col = 0; col < size - 4; col++) {
                countX = 0;
                countO = 0;
                for (int i = 0; i < 5; i++) {
                    if (matrix[row - i][col + i] == Board.VALUE_X) {
                        countX++;
                    }
                    if (matrix[row - i][col + i] == Board.VALUE_O) {
                        countO++;
                    }
                }
                if (countX * countO == 0 && countX != countO) {
                    for (int i = 0; i < 5; i++) {
                        if (matrix[row - i][col + i] != Board.VALUE_BLANK) {
                            continue;
                        }
                        if (countX == 0) {
                            if (value == Board.VALUE_X) {
                                result[row - i][col + i] += DEFENSE_POINT[countO];
                            } else {
                                result[row - i][col + i] += ATTACK_POINT[countO];
                            }
                        }
                        if (countO == 0) {
                            if (value == Board.VALUE_X) {
                                result[row - i][col + i] += ATTACK_POINT[countX];
                            } else {
                                result[row - i][col + i] += DEFENSE_POINT[countX];
                            }
                        }
                        if (countO == 4 || countX == 4) {
                            result[row - i][col + i] *= 10;
                        }
                    }
                }
            }
        }
        return result;
    }

    public static String get2DArrayPrint(int[][] matrix) {
        StringBuilder output = new StringBuilder();
        for (int[] ints : matrix) {
            for (int anInt : ints) {
                output.append(anInt).append("\t");
            }
            output.append("\n");
        }
        return output.toString();
    }
}
