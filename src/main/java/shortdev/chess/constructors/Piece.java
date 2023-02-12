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
        this.x = x;
        this.y = y;
        this.type = type;
        this.color = color;
        findPossibleMoves();
    }

    public void findPossibleMoves() {
        switch (getType().getName()) {
            case "PAWN":
                getPawnMoves();
            case "ROOK":
                getRookMoves();
            case "KNIGHT":
                getKnightMoves();
            case "BISHOP":
                getBishopMoves();
            case "QUEEN":
                getQueenMoves();
            case "KING":
                getKingMoves();
        }
    }

    public Set<Move> getPawnMoves() {
        Set<Move> output;
        if ()
    }

    public Set<Move> getRookMoves() {
        Set<Move> output;
        if ()
    }

    public Set<Move> getKnightMoves() {
        Set<Move> output;
        if ()
    }

    public Set<Move> getBishopMoves() {
        Set<Move> output;
        if ()
    }

    public Set<Move> getQueenMoves() {
        Set<Move> output;
        if ()
    }

    public Set<Move> getKingMoves() {
        Set<Move> output;
        if ()
    }

    public boolean occupiedBySelf(int x, int y) {
        for
    }

    public boolean occupiedByEnemy(int x, int y) {
        if
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

    public PieceType getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

}
