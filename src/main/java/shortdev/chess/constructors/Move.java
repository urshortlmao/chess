package shortdev.chess.constructors;

import shortdev.chess.Chess;

public class Move {

    private Chess plugin;

    private int x1, y1, x2, y2;

    private Piece piece;

    public Move(Piece piece, int x1, int y1, int x2, int y2) {
        this.piece = piece;
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public int getBeforeX() {
        return x1;
    }

    public int getAfterX() {
        return x2;
    }

    public int getBeforeY() {
        return y1;
    }

    public int getAfterY() {
        return y2;
    }

    public Piece getPiece() {
        return piece;
    }

}
