package com.mobile.caro.AI;

import java.util.Random;

public class Move implements Comparable<Move> {

    private static Random random = new Random();

    public int x;
    public int y;
    public int point;

    public Move(int x, int y, int point) {
        this.x = x;
        this.y = y;
        this.point = point;
    }

    @Override
    public int compareTo(Move o) {
        if (point == o.point) {
            return random.nextBoolean() ? 1 : -1;
        }
        return point - o.point;
    }
}
