package org.plyrenderer.client;

/**
 * Author: Jacques Fontignie
 * Date: 5/21/12
 * Time: 4:11 AM
 */
public class Position {

    private int x;
    private int y;

    public Position(){}

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void set(int x, int y) {
        setX(x);
        setY(y);
    }

    public void set(Position p) {
        setX(p.getX());
        setY(p.getY());
    }

    @Override
    public String toString() {
        return "(" + x + ";" + y + ")";
    }
}
