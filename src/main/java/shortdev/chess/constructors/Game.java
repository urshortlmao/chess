package shortdev.chess.constructors;

import shortdev.chess.Chess;

import java.util.*;

public class Game {

    private Chess plugin;

    private List<Game> games = new ArrayList<>();

    private HashMap<GamePlayer, List<Piece>> pieces = new HashMap<>();

    private GamePlayer player1, player2;

    public Game(GamePlayer player1, GamePlayer player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public List<Game> getGames() {
        return games;
    }

    public void createGame(Game game) {
        games.add(game);
    }

    public void endGame(Game game) {
        games.remove(game);
    }

    public List<Piece> getPieces(GamePlayer player) {
        return pieces.get(player);
    }

    public void setUpPieces(GamePlayer player) {
        List<Piece> initialSetup = new ArrayList<>();
        if (player.isColor("WHITE")) {
            for (int i = 1; i <= 8; i++) {
                initialSetup.add(new Piece(7, i, new PieceType("PAWN"), "WHITE"));
                if (i == 1 || i == 8) {
                    initialSetup.add(new Piece(8, i, new PieceType("ROOK"), "WHITE"));
                }
                if (i == 2 || i == 7) {
                    initialSetup.add(new Piece(8, i, new PieceType("KNIGHT"), "WHITE"));
                }
                if (i == 3 || i == 6) {
                    initialSetup.add(new Piece(8, i, new PieceType("BISHOP"), "WHITE"));
                }
                if (i == 4) {
                    initialSetup.add(new Piece(8, i, new PieceType("QUEEN"), "WHITE"));
                }
                if (i == 5) {
                    initialSetup.add(new Piece(8, i, new PieceType("KING"), "WHITE"));
                }
            }
        }
        if (player.isColor("BLACK")) {
            for (int i = 1; i <= 8; i++) {
                initialSetup.add(new Piece(2, i, new PieceType("PAWN"), "BLACK"));
                if (i == 1 || i == 8) {
                    initialSetup.add(new Piece(1, i, new PieceType("ROOK"), "BLACK"));
                }
                if (i == 2 || i == 7) {
                    initialSetup.add(new Piece(1, i, new PieceType("KNIGHT"), "BLACK"));
                }
                if (i == 3 || i == 6) {
                    initialSetup.add(new Piece(1, i, new PieceType("BISHOP"), "BLACK"));
                }
                if (i == 4) {
                    initialSetup.add(new Piece(1, i, new PieceType("QUEEN"), "BLACK"));
                }
                if (i == 5) {
                    initialSetup.add(new Piece(1, i, new PieceType("KING"), "BLACK"));
                }
            }
        }
        pieces.put(player, initialSetup);
    }

    public GamePlayer getPlayer1() {
        return player1;
    }

    public GamePlayer getPlayer2() {
        return player2;
    }

    public GamePlayer getOpponent(GamePlayer player) {
        if (player == player1) {
            return player2;
        }
        if (player == player2) {
            return player1;
        }
        return null;
    }

    public HashMap<Piece, Set<Move>> getPossibleMoves(GamePlayer player) {
        if (!player.inCheck()) {
            for (Piece piece : pieces.get(player)) {

            }
        }
    }

}
