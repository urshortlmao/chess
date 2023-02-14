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

    private static HashMap<GamePlayer, Integer> scrollPositions = new HashMap<>();

    public Game(GamePlayer player1, GamePlayer player2, String instance) {
        this.player1 = player1;
        this.player2 = player2;
        scrollPositions.put(player1, 0);
        scrollPositions.put(player2, 0);
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

    public int getScrollPosition(GamePlayer player) {
        return scrollPositions.get(player);
    }

    public void setScrollPosition(GamePlayer player, int position) {
        scrollPositions.put(player, position);
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
        String color = player.getColor();
        int y1, y2;
        if (color.equals("WHITE")) {
            y1 = 2;
            y2 = 1;
        } else {
            y1 = 7;
            y2 = 8;
        }
        //PAWNS
        initialSetup.add(new Piece(1, y1, new PieceType("PAWN"), color));
        initialSetup.add(new Piece(2, y1, new PieceType("PAWN"), color));
        initialSetup.add(new Piece(3, y1, new PieceType("PAWN"), color));
        initialSetup.add(new Piece(4, y1, new PieceType("PAWN"), color));
        initialSetup.add(new Piece(5, y1, new PieceType("PAWN"), color));
        initialSetup.add(new Piece(6, y1, new PieceType("PAWN"), color));
        initialSetup.add(new Piece(7, y1, new PieceType("PAWN"), color));
        initialSetup.add(new Piece(8, y1, new PieceType("PAWN"), color));
        //ROOKS
        initialSetup.add(new Piece(1, y2, new PieceType("ROOK"), color));
        initialSetup.add(new Piece(8, y2, new PieceType("ROOK"), color));
        //KNIGHTS
        initialSetup.add(new Piece(2, y2, new PieceType("KNIGHT"), color));
        initialSetup.add(new Piece(7, y2, new PieceType("KNIGHT"), color));
        //BISHOPS
        initialSetup.add(new Piece(3, y2, new PieceType("BISHOP"), color));
        initialSetup.add(new Piece(6, y2, new PieceType("BISHOP"), color));
        //QUEEN
        initialSetup.add(new Piece(4, y2, new PieceType("QUEEN"), color));
        //KING
        initialSetup.add(new Piece(5, y2, new PieceType("KING"), color));
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
