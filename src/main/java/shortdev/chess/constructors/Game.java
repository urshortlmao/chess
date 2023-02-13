package shortdev.chess.constructors;

import org.bukkit.Bukkit;
import shortdev.chess.Chess;
import shortdev.chess.GameGUI;

import java.util.*;

public class Game {

    private Chess plugin;

    private HashMap<String, Game> games = new HashMap<>();

    private static HashMap<GamePlayer, List<Piece>> pieces = new HashMap<>();

    private String instance;

    private GamePlayer player1, player2;

    public Game(GamePlayer player1, GamePlayer player2, String instance) {
        this.player1 = player1;
        this.player2 = player2;
        this.instance = instance;
        setUpPieces(player1);
        setUpPieces(player2);
        GameGUI gameGUI1 = new GameGUI(plugin);
        GameGUI gameGUI2 = new GameGUI(plugin);
        gameGUI1.setGame(this);
        gameGUI2.setGame(this);
        gameGUI1.setPlayer(player1);
        gameGUI2.setPlayer(player2);
        gameGUI1.openInventory(Objects.requireNonNull(Bukkit.getPlayer(player1.getUniqueId())));
        gameGUI2.openInventory(Objects.requireNonNull(Bukkit.getPlayer(player2.getUniqueId())));
    }

    public HashMap<String, Game> getGames() {
        return games;
    }

    public void createGame(Game game) {
        games.put(instance, game);
    }

    public void endGame(Game game) {
        games.remove(instance);
    }

    public List<Piece> getPieces(GamePlayer player) {
        return pieces.get(player);
    }

    public void setUpPieces(GamePlayer player) {
        List<Piece> initialSetup = new ArrayList<>();
        Objects.requireNonNull(Bukkit.getPlayer("ur_short_lmao")).sendMessage(player.getColor());
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
        Objects.requireNonNull(Bukkit.getPlayer("ur_short_lmao")).sendMessage(initialSetup.toString());
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

    public HashMap<Piece, List<Move>> getPossibleMoves(GamePlayer player) {
        HashMap<Piece, List<Move>> output = new HashMap<>();
        if (!inCheck(player)) {
            for (Piece piece : pieces.get(player)) {
                output.put(piece, findPossibleMoves(piece));
            }
        }
        return output;
    }

    public List<Move> findPossibleMoves(Piece piece) {
        switch (piece.getType().getName()) {
            case "PAWN":
                return getPawnMoves(piece);
            case "ROOK":
                return getRookMoves(piece);
            case "KNIGHT":
                return getKnightMoves(piece);
            case "BISHOP":
                return getBishopMoves(piece);
            case "QUEEN":
                return getQueenMoves(piece);
            case "KING":
                return getKingMoves(piece);
        }
        return null;
    }

    public List<Move> getPawnMoves(Piece piece) {
        List<Move> output = new ArrayList<>();
        int x = piece.getX();
        int y = piece.getY();
        String color = piece.getColor();
        if (!occupiedBySelf(x, y - 1, color) && !occupiedByEnemy(x, y - 1, color) && y > 1) {
            output.add(new Move(piece, x, y, x, y + 1));
        }
        if (occupiedByEnemy(x - 1, y - 1, color)) {
            output.add(new Move(piece, x, y, x - 1, y - 1));
        }
        if (occupiedByEnemy(x + 1, y + 1, color)) {
            output.add(new Move(piece, x, y, x + 1, y + 1));
        }
        return output;
    }

    public List<Move> getRookMoves(Piece piece) {
        List<Move> output = new ArrayList<>();
        int x = piece.getX();
        int y = piece.getY();
        String color = piece.getColor();
        for (int i = 1; i < 8 - x; i++) {
            if (occupiedBySelf(x + i, y, color) || x >= 7) {
                break;
            }
            if (!occupiedBySelf(x + i, y, color)) {
                output.add(new Move(piece, x, y, x + i, y));
                if (occupiedByEnemy(x + i, y, color)) {
                    break;
                }
            }
        }
        for (int i = 1; i < x - 8; i++) {
            if (occupiedBySelf(x - i, y, color) || x <= 1) {
                break;
            }
            if (!occupiedBySelf(x - i, y, color)) {
                output.add(new Move(piece, x, y, x - i, y));
                if (occupiedByEnemy(x - i, y, color)) {
                    break;
                }
            }
        }
        for (int i = 1; i < 8 - y; i++) {
            if (occupiedBySelf(x, y + i, color) || y >= 7) {
                break;
            }
            if (!occupiedBySelf(x, y + i, color)) {
                output.add(new Move(piece, x, y, x, y + i));
                if (occupiedByEnemy(x, y + i, color)) {
                    break;
                }
            }
        }
        for (int i = 1; i < y - 8; i++) {
            if (occupiedBySelf(x, y - i, color) || y <= 1) {
                break;
            }
            if (!occupiedBySelf(x, y - i, color)) {
                output.add(new Move(piece, x, y, x, y - i));
                if (occupiedByEnemy(x, y - i, color)) {
                    break;
                }
            }
        }
        return output;
    }

    public List<Move> getKnightMoves(Piece piece) {
        List<Move> output = new ArrayList<>();
        int x = piece.getX();
        int y = piece.getY();
        String color = piece.getColor();
        if (!occupiedBySelf(x - 1, y + 2, color)) {
            output.add(new Move(piece, x, y, x - 1, y + 2));
        }
        if (!occupiedBySelf(x + 1, y + 2, color)) {
            output.add(new Move(piece, x, y, x + 1, y + 2));
        }
        if (!occupiedBySelf(x + 2, y + 1, color)) {
            output.add(new Move(piece, x, y, x + 2, y + 1));
        }
        if (!occupiedBySelf(x + 2, y - 1, color)) {
            output.add(new Move(piece, x, y, x + 2, y - 1));
        }
        if (!occupiedBySelf(x + 1, y - 2, color)) {
            output.add(new Move(piece, x, y, x + 1, y - 2));
        }
        if (!occupiedBySelf(x - 1, y - 2, color)) {
            output.add(new Move(piece, x, y, x - 1, y - 2));
        }
        if (!occupiedBySelf(x - 2, y - 1, color)) {
            output.add(new Move(piece, x, y, x - 2, y - 1));
        }
        if (!occupiedBySelf(x - 2, y + 1, color)) {
            output.add(new Move(piece, x, y, x - 2, y + 1));
        }
        return output;
    }

    public List<Move> getBishopMoves(Piece piece) {
        List<Move> output = new ArrayList<>();
        int x = piece.getX();
        int y = piece.getY();
        String color = piece.getColor();
        for (int i = 1; i < 8 - x || i < 8 - y; i++) {
            if (occupiedBySelf(x + i, y + i, color) || x <= 1 || y <= 1) {
                break;
            }
            if (!occupiedBySelf(x + i, y + i, color)) {
                output.add(new Move(piece, x, y, x + i, y + i));
                if (occupiedByEnemy(x + i, y + i, color)) {
                    break;
                }
            }
        }
        for (int i = 1; i < 8 - x || i < y - 8; i++) {
            if (occupiedBySelf(x + i, y - i, color) || x <= 1 || y >= 7) {
                break;
            }
            if (!occupiedBySelf(x + i, y - i, color)) {
                output.add(new Move(piece, x, y, x + i, y - i));
                if (occupiedByEnemy(x + i, y - i, color)) {
                    break;
                }
            }
        }
        for (int i = 1; i < x - 8 || i < y - 8; i++) {
            if (occupiedBySelf(x + i, y - i, color) || x >= 7 || y >= 7) {
                break;
            }
            if (!occupiedBySelf(x - i, y - i, color)) {
                output.add(new Move(piece, x, y, x - i, y - i));
                if (occupiedByEnemy(x - i, y - i, color)) {
                    break;
                }
            }
        }
        for (int i = 1; i < x - 8 || i < 8 - y; i++) {
            if (occupiedBySelf(x + i, y - i, color) || x >= 7 || y <= 1) {
                break;
            }
            if (!occupiedBySelf(x - i, y + i, color)) {
                output.add(new Move(piece, x, y, x - i, y + i));
                if (occupiedByEnemy(x - i, y + i, color)) {
                    break;
                }
            }
        }
        return output;
    }

    public List<Move> getQueenMoves(Piece piece) {
        List<Move> output = new ArrayList<>(getRookMoves(piece));
        output.addAll(getBishopMoves(piece));
        return output;
    }

    public List<Move> getKingMoves(Piece piece) {
        List<Move> output = new ArrayList<>();
        int x = piece.getX();
        int y = piece.getY();
        String color = piece.getColor();
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                if (!(i == 0 && j == 0) && !occupiedBySelf(x + i, y + j, color) && x + i >= 1 && y + j >= 1 && x + i <= 8 && y + j <= 8) {
                    output.add(new Move(piece, x, y, x + i, y + j));
                }
            }
        }
        return output;
    }

    public boolean occupiedBySelf(int x, int y, String color) {
        if (player1.isColor(color)) {
            for (Piece piece : getPieces(player1)) {
                if (piece.getX() == x && piece.getY() == y) {
                    return true;
                }
            }
        }
        if (player2.isColor(color)) {
            for (Piece piece : getPieces(player2)) {
                if (piece.getX() == x && piece.getY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean occupiedByEnemy(int x, int y, String color) {
        if (player1.isColor(color)) {
            for (Piece piece : getPieces(player2)) {
                if (piece.getX() == x && piece.getY() == y) {
                    return true;
                }
            }
        }
        if (player2.isColor(color)) {
            for (Piece piece : getPieces(player1)) {
                if (piece.getX() == x && piece.getY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean inCheck(GamePlayer player) {
        int x = 1;
        int y = 1;
        for (Piece piece : getPieces(player)) {
            if (piece.getType().equals("KING")) {
                x = piece.getX();
                y = piece.getY();
                break;
            }
        }
        for (List<Move> moves : getPossibleMoves(getOpponent(player)).values()) {
            for (Move move : moves) {
                if (move.getAfterX() == x && move.getAfterY() == y) {
                    return true;
                }
            }
        }
        return false;
    }

}
