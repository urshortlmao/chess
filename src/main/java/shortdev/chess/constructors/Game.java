package shortdev.chess.constructors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import shortdev.chess.GameGUI;

import java.util.*;

public class Game {

    private static TreeMap<String, Game> games = new TreeMap<>();

    private static HashMap<GamePlayer, Piece[][]> pieces = new HashMap<>();

    private HashMap<GamePlayer, Piece[][]> tempPieces = new HashMap<>();

    private String instance;

    private GamePlayer player1, player2;

    public Game(GamePlayer player1, GamePlayer player2, String instance) {
        this.player1 = player1;
        this.player2 = player2;
        this.instance = instance;
        Piece[][] player1Pieces = new Piece[8][6];
        Piece[][] player2Pieces = new Piece[8][6];
        setUpPieces(player1, player1Pieces);
        setUpPieces(player2, player2Pieces);
        Game.pieces.put(player1, player1Pieces);
        Game.pieces.put(player2, player2Pieces);
        //updatePieces();
    }

    public static TreeMap<String, Game> getGames() {
        return games;
    }

    public static boolean isInGame(Player player) {
        for (Game game : games.values()) {
            if (Objects.equals(Bukkit.getPlayer(game.getPlayer1().getUniqueId()), player) || Objects.equals(Bukkit.getPlayer(game.getPlayer2().getUniqueId()), player)) {
                return true;
            }
        }
        return false;
    }

    public static Game inGame(GamePlayer player) {
        for (Game game : games.values()) {
            if (Objects.equals(game.getPlayer1(), player) || Objects.equals(game.getPlayer2(), player)) {
                return game;
            }
        }
        return null;
    }

    public static GamePlayer getGamePlayer(Player player) {
        for (Game game : games.values()) {
            if (Objects.equals(Bukkit.getPlayer(game.getPlayer1().getUniqueId()), player)) return game.getPlayer1();
            if (Objects.equals(Bukkit.getPlayer(game.getPlayer2().getUniqueId()), player)) return game.getPlayer2();
        }
        return null;
    }

    public void createGame() {
        games.put(instance, this);
        new GameGUI().setGame(this);
    }

    public static void endGame(Game game) {
        Inventory inv1 = Objects.requireNonNull(Bukkit.getPlayer(game.getPlayer1().getUniqueId())).getInventory();
        Inventory inv2 = Objects.requireNonNull(Bukkit.getPlayer(game.getPlayer2().getUniqueId())).getInventory();
        for (int i = 9; i < 27; i++) {
            inv1.setItem(i, new ItemStack(Material.AIR));
            inv2.setItem(i, new ItemStack(Material.AIR));
        }
        for (String string : games.keySet()) {
            if (games.get(string).equals(game)) {
                games.remove(string);
            }
        }
        GameGUI.closeInventory(Objects.requireNonNull(Bukkit.getPlayer(game.getPlayer1().getUniqueId())));
        GameGUI.closeInventory(Objects.requireNonNull(Bukkit.getPlayer(game.getPlayer2().getUniqueId())));
    }

    public static Piece[][] getPieces(GamePlayer player) {
        return pieces.get(player);
    }

    public void setUpPieces(GamePlayer player, Piece[][] pieces) {
        pieces[0] = setUpPawns(player);
        pieces[1] = setUpRooks(player);
        pieces[2] = setUpKnights(player);
        pieces[3] = setUpBishops(player);
        pieces[4] = setUpQueen(player);
        pieces[5] = setUpKing(player);
    }

    public Piece[] setUpPawns(GamePlayer player) {
        Piece[] initialSetup = new Piece[8];
        String color = player.getColor();
        int y1 = color.equals("WHITE") ? 2 : 7;
        for (int i = 1; i <= 8; i++) {
            initialSetup[i - 1] = new Piece(i, y1, new PieceType("PAWN"), color);
        }
        return initialSetup;
    }

    public Piece[] setUpRooks(GamePlayer player) {
        Piece[] initialSetup = new Piece[8];
        String color = player.getColor();
        int y2 = color.equals("WHITE") ? 1 : 8;
        initialSetup[0] = new Piece(1, y2, new PieceType("ROOK"), color);
        initialSetup[1] = new Piece(8, y2, new PieceType("ROOK"), color);
        return initialSetup;
    }

    public Piece[] setUpKnights(GamePlayer player) {
        Piece[] initialSetup = new Piece[8];
        String color = player.getColor();
        int y2 = color.equals("WHITE") ? 1 : 8;
        initialSetup[0] = new Piece(2, y2, new PieceType("KNIGHT"), color);
        initialSetup[1] = new Piece(7, y2, new PieceType("KNIGHT"), color);
        return initialSetup;
    }

    public Piece[] setUpBishops(GamePlayer player) {
        Piece[] initialSetup = new Piece[8];
        String color = player.getColor();
        int y2 = color.equals("WHITE") ? 1 : 8;
        initialSetup[0] = new Piece(3, y2, new PieceType("BISHOP"), color);
        initialSetup[1] = new Piece(6, y2, new PieceType("BISHOP"), color);
        return initialSetup;
    }

    public Piece[] setUpQueen(GamePlayer player) {
        Piece[] initialSetup = new Piece[8];
        String color = player.getColor();
        int y2 = color.equals("WHITE") ? 1 : 8;
        initialSetup[0] = new Piece(4, y2, new PieceType("QUEEN"), color);
        return initialSetup;
    }

    public Piece[] setUpKing(GamePlayer player) {
        Piece[] initialSetup = new Piece[8];
        String color = player.getColor();
        int y2 = color.equals("WHITE") ? 1 : 8;
        initialSetup[0] = new Piece(5, y2, new PieceType("KING"), color);
        return initialSetup;
    }

    public GamePlayer getPlayer1() {
        return player1;
    }

    public GamePlayer getPlayer2() {
        return player2;
    }

    public GamePlayer getOpponent(GamePlayer player) {
        return player == player1 ? player2 : player1;
    }

    public HashMap<Piece, List<Move>> getPossibleMoves(GamePlayer player) {
        HashMap<Piece, List<Move>> output = new HashMap<>();
        for (Piece[] pieces : pieces.get(player)) {
            for (Piece piece : pieces) {
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
        //if
        return output;
    }

    public boolean occupiedBySelf(int x, int y, String color) {
        if (player1.isColor(color)) {
            for (Piece[] pieces : getPieces(player1)) {
                for (Piece piece : pieces) {
                    if (piece.getX() == x && piece.getY() == y) {
                        return true;
                    }
                }
            }
        }
        if (player2.isColor(color)) {
            for (Piece[] pieces : getPieces(player2)) {
                for (Piece piece : pieces) {
                    if (piece.getX() == x && piece.getY() == y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean occupiedByEnemy(int x, int y, String color) {
        if (player1.isColor(color)) {
            for (Piece[] pieces : getPieces(player2)) {
                for (Piece piece : pieces) {
                    if (piece.getX() == x && piece.getY() == y) {
                        return true;
                    }
                }
            }
        }
        if (player2.isColor(color)) {
            for (Piece[] pieces : getPieces(player1)) {
                for (Piece piece : pieces) {
                    if (piece.getX() == x && piece.getY() == y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean inCheck(GamePlayer player) {
        int x = 1;
        int y = 1;
        for (Piece[] pieceArray : getPieces(player)) {
            for (Piece piece : pieceArray) {
                if (piece.getType().equals("KING")) {
                    x = piece.getX();
                    y = piece.getY();
                    break;
                }
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

    public boolean stillInCheck(GamePlayer player, Move m) {
        //executeMove(m);
        int x = 1;
        int y = 1;
        for (Piece[] pieceArray : getPieces(player)) {
            for (Piece piece : pieceArray) {
                if (piece.getType().equals("KING")) {
                    x = piece.getX();
                    y = piece.getY();
                    break;
                }
            }
        }
        int i = 0;
        for (List<Move> moves : getPossibleMoves(getOpponent(player)).values()) {
            for (Move move : moves) {
                if (move.getAfterX() == x && move.getAfterY() == y) {
                    //checkingPieces.add(move.getPiece());
                }
                i++;
            }
        }
        return false;
    }

    public void executeMove(GamePlayer player, Move move) {
        Piece piece = move.getPiece();
        piece.setMoved();
        int x = move.getAfterX();
        int y = move.getAfterY();
        piece.setX(x);
        piece.setY(y);
    }

    public void emulateMove(GamePlayer player, Move move) {

    }

}
