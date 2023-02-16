package shortdev.chess.constructors;

import shortdev.chess.Chess;

import java.util.*;

public class Piece {

    private Chess plugin;

    private Set<Move> possibleMoves;

    private int x,y;

    private PieceType type;

    private String color;

    private boolean moved = false;

    public Piece(int x, int y, PieceType type, String color) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.color = color;
    }

    public Set<Move> getPossibleMoves() {
        return possibleMoves;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public PieceType getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    public boolean hasMoved() {
        return this.moved;
    }

    public void setMoved() {
        moved = true;
    }

}
