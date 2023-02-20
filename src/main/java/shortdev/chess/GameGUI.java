package shortdev.chess;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shortdev.chess.constructors.Game;
import shortdev.chess.constructors.GamePlayer;
import shortdev.chess.constructors.Move;
import shortdev.chess.constructors.Piece;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Material.*;

public class GameGUI implements Listener {

    private Inventory inv1, inv2, pInv1, pInv2;

    private Game game;

    private GamePlayer player1, player2;

    private Player p1, p2;

    private boolean isGameActive = true;

    private static HashMap<GamePlayer, List<Integer>> highlightedIndices = new HashMap<>();

    public GameGUI() {
        inv1 = Bukkit.createInventory(null, 54, "Chess");
        inv2 = Bukkit.createInventory(null, 54, "Chess");
    }

    public void setGame(Game game) {
        this.game = game;
        player1 = game.getPlayer1();
        player2 = game.getPlayer2();
        p1 = Bukkit.getPlayer(player1.getUniqueId());
        p2 = Bukkit.getPlayer(player2.getUniqueId());
        pInv1 = Objects.requireNonNull(p1.getInventory());
        pInv2 = Objects.requireNonNull(p2.getInventory());
        openInventory(p1, inv1);
        openInventory(p2, inv2);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        Bukkit.broadcastMessage("Click registered");
        final Player p = (Player) e.getWhoClicked();
        Bukkit.broadcastMessage(p.getName());
        Bukkit.broadcastMessage("Clicker is in game: " + Game.isInGame(p));
        if (!Game.isInGame(p)) return;
        Bukkit.broadcastMessage("Event cancelled");
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().equals(AIR)) return;
        Bukkit.broadcastMessage("Item is neither null nor air");
        int i = e.getRawSlot();
        /* if (e.getInventory().getType().equals(InventoryType.PLAYER)) {
            i += 54;
            Bukkit.broadcastMessage("Item is in player inventory");
        } */
        int x = i % 9 + 1;
        int y = 8 - i / 9;
        GamePlayer g = Game.getGamePlayer(p);
        highlightPossibleMoves(i, x, y, g);
        initializeItems();
    }

    public static void highlightPossibleMoves(int i, int x, int y, GamePlayer g) {
        Piece[][] pieces = Game.getPieces(g);
        for (Piece[] pieceArray : pieces) {
            for (Piece piece : pieceArray) {
                if (piece != null) {
                    if (piece.getX() == x && piece.getY() == y) {
                        List<Move> moves = Objects.requireNonNull(Game.inGame(g)).findPossibleMoves(piece);
                        List<Integer> indices = new ArrayList<>();
                        indices.add(i);
                        for (Move move : moves) {
                            int x2 = move.getAfterX();
                            int y2 = move.getAfterY();
                            indices.add(x2 + 8*y2 - 1);
                            Bukkit.broadcastMessage(x2 + ", " + y2 + " " + i + " piece: " + piece.getType().getName());
                        }
                        highlightedIndices.put(g, indices);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv1) || e.getInventory().equals(inv2) || e.getInventory().equals(pInv1) || e.getInventory().equals(pInv2)) {
            e.setCancelled(true);
        }
    }

    public static void closeInventory(Player player) {
        player.closeInventory();
    }

    public boolean isHighlightedIndex(GamePlayer player, int index) {
        if (highlightedIndices.get(player) != null) {
            for (int i : highlightedIndices.get(player)) {
                if (i == index) {
                    return true;
                }
            }
        }
        return false;
    }

    public void initializeItems() {
        pInv1.clear();
        pInv2.clear();
        Piece[][] pieces = Game.getPieces(player1);
        Piece[][] opponentPieces = Game.getPieces(player2);

        for (int i = 0; i < 72; i++) {
            if (i % 9 < 8) {
                int x = i % 9 + 1;
                int y = 8 - i / 9;
                boolean isOccupied = false;
                for (Piece[] pieceArray : pieces) {
                    for (Piece piece : pieceArray) {
                        if (piece != null) {
                            if (piece.getX() == x && piece.getY() == y) {
                                if (i < 54) {
                                    inv1.setItem(i, piece.getType().getItem(piece.getColor(), isHighlightedIndex(player1, i)));
                                } else {
                                    pInv1.setItem(i - 45, piece.getType().getItem(piece.getColor(), isHighlightedIndex(player1, i)));
                                }
                                if (i < 18) {
                                    pInv2.setItem(25 - i, piece.getType().getItem(piece.getColor(), isHighlightedIndex(player2, i)));
                                } else {
                                    inv2.setItem(70 - i, piece.getType().getItem(piece.getColor(), isHighlightedIndex(player2, i)));
                                }
                                isOccupied = true;
                                break;
                            }
                        }
                    }
                }
                for (Piece[] pieceArray : opponentPieces) {
                    for (Piece piece : pieceArray) {
                        if (piece != null) {
                            if (piece.getX() == x && piece.getY() == y) {
                                if (i < 54) {
                                    inv1.setItem(i, piece.getType().getItem(piece.getColor(), isHighlightedIndex(player1, i)));
                                } else {
                                    pInv1.setItem(i - 45, piece.getType().getItem(piece.getColor(), isHighlightedIndex(player1, i)));
                                }
                                if (i < 18) {
                                    pInv2.setItem(25 - i, piece.getType().getItem(piece.getColor(), isHighlightedIndex(player2, i)));
                                } else {
                                    inv2.setItem(70 - i, piece.getType().getItem(piece.getColor(), isHighlightedIndex(player2, i)));
                                }
                                isOccupied = true;
                                break;
                            }
                        }
                    }
                }
                if (!isOccupied) {
                    if ((x + y) % 2 == 0) {
                        if (i < 54) {
                            inv1.setItem(i, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null, isHighlightedIndex(player1, i)));
                            inv2.setItem(i, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null, isHighlightedIndex(player2, i)));
                        } else {
                            pInv1.setItem(i - 45, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null, isHighlightedIndex(player1, i)));
                            pInv2.setItem(i - 45, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null, isHighlightedIndex(player2, i)));
                        }
                    } else {
                        if (i < 54) {
                            inv1.setItem(i, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null, isHighlightedIndex(player1, i)));
                            inv2.setItem(i, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null, isHighlightedIndex(player2, i)));
                        } else {
                            pInv1.setItem(i - 45, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null, isHighlightedIndex(player1, i)));
                            pInv2.setItem(i - 45, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null, isHighlightedIndex(player2, i)));
                        }
                    }
                }
            } else {
                if (i < 54) {
                    inv1.setItem(i, createGuiItem(GRAY_STAINED_GLASS_PANE, " ", null, false));
                    inv2.setItem(i, createGuiItem(GRAY_STAINED_GLASS_PANE, " ", null, false));
                } else {
                    pInv1.setItem(i - 45, createGuiItem(GRAY_STAINED_GLASS_PANE, " ", null, false));
                    pInv2.setItem(i - 45, createGuiItem(GRAY_STAINED_GLASS_PANE, " ", null, false));
                }
            }
        }
    }


    protected ItemStack createGuiItem(final Material material, final String name, final List<String> lore, boolean highlighted) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        if (lore != null) {
            meta.setLore(lore);
        }
        if (highlighted) {
            item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES); //, ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
        initializeItems();
    }

}
