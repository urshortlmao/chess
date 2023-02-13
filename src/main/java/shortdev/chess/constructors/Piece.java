package shortdev.chess.constructors;

import shortdev.chess.Chess;

import java.util.*;

public class Piece {

    private Chess plugin;

    private static Set<Move> possibleMoves;

    private static int x,y;

    private static PieceType type;

    private static String color;

    public Piece(int x, int y, PieceType type, String color) {
        Piece.x = x;
        Piece.y = y;
        Piece.type = type;
        Piece.color = color;
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
        Piece.x = x;
    }

    public void setY(int y) {
        Piece.y = y;
    }

    public PieceType getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

}
