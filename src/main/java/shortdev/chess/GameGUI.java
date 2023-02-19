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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shortdev.chess.constructors.Game;
import shortdev.chess.constructors.GamePlayer;
import shortdev.chess.constructors.Move;
import shortdev.chess.constructors.Piece;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Material.*;

public class GameGUI implements Listener {

    private Inventory inv1, inv2, pInv1, pInv2;

    private Game game;

    private GamePlayer player1, player2;

    private List<Integer> highlightedIndices = new ArrayList<>();

    public GameGUI() {
        inv1 = Bukkit.createInventory(null, 54, "Chess");
        inv2 = Bukkit.createInventory(null, 54, "Chess");
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv1) && !e.getInventory().equals(inv2)) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().equals(AIR)) return;
        final Player p = (Player) e.getWhoClicked();
        int i = e.getRawSlot();
        int x = i % 9 + 1;
        int y = 8 - i / 9;
        GamePlayer g = Objects.equals(Bukkit.getPlayer(player1.getUniqueId()), p) ? player1 : player2;
        Piece[][] pieces = game.getPieces(g);
        for (Piece[] pieceArray : pieces) {
            for (Piece piece : pieceArray) {
                if (piece.getX() == x && piece.getY() == y) {
                    List<Move> moves = game.findPossibleMoves(piece);
                    highlightedIndices.clear();
                    highlightedIndices.add(i);
                    for (Move move : moves) {
                        int x2 = move.getAfterX();
                        int y2 = move.getAfterY();
                        highlightedIndices.add(x2 + 8*y2 - 1);
                    }
                }
            }
        }
        initializeItems();
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv1) || e.getInventory().equals(inv2)) {
            e.setCancelled(true);
        }
    }

    public static void closeInventory(Player player) {
        player.closeInventory();
    }

    public void initializeItems() {
        pInv1.clear();
        pInv2.clear();
        Piece[][] pieces = game.getPieces(player1);
        Piece[][] opponentPieces = game.getPieces(player2);

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
                                    inv1.setItem(i, piece.getType().getItem(piece.getColor(), highlightedIndices.contains(i)));
                                } else {
                                    pInv1.setItem(i - 45, piece.getType().getItem(piece.getColor(), highlightedIndices.contains(i)));
                                }
                                if (i < 18) {
                                    pInv2.setItem(25 - i, piece.getType().getItem(piece.getColor(), highlightedIndices.contains(i)));
                                } else {
                                    inv2.setItem(70 - i, piece.getType().getItem(piece.getColor(), highlightedIndices.contains(i)));
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
                                    inv1.setItem(i, piece.getType().getItem(piece.getColor(), highlightedIndices.contains(i)));
                                } else {
                                    pInv1.setItem(i - 45, piece.getType().getItem(piece.getColor(), highlightedIndices.contains(i)));
                                }
                                if (i < 18) {
                                    pInv2.setItem(25 - i, piece.getType().getItem(piece.getColor(), highlightedIndices.contains(i)));
                                } else {
                                    inv2.setItem(70 - i, piece.getType().getItem(piece.getColor(), highlightedIndices.contains(i)));
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
                            inv1.setItem(i, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null, highlightedIndices.contains(i)));
                            inv2.setItem(i, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null, highlightedIndices.contains(i)));
                        } else {
                            pInv1.setItem(i - 45, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null, highlightedIndices.contains(i)));
                            pInv2.setItem(i - 45, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null, highlightedIndices.contains(i)));
                        }
                    } else {
                        if (i < 54) {
                            inv1.setItem(i, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null, highlightedIndices.contains(i)));
                            inv2.setItem(i, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null, highlightedIndices.contains(i)));
                        } else {
                            pInv1.setItem(i - 45, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null, highlightedIndices.contains(i)));
                            pInv2.setItem(i - 45, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null, highlightedIndices.contains(i)));
                        }
                    }
                }
            } else {
                if (i < 54) {
                    inv1.setItem(i, new ItemStack(GRAY_STAINED_GLASS_PANE));
                    inv2.setItem(i, new ItemStack(GRAY_STAINED_GLASS_PANE));
                } else {
                    pInv1.setItem(i - 45, new ItemStack(GRAY_STAINED_GLASS_PANE));
                    pInv2.setItem(i - 45, new ItemStack(GRAY_STAINED_GLASS_PANE));
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
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
    }

    public void setGame(Game game) {
        this.game = game;
        player1 = game.getPlayer1();
        player2 = game.getPlayer2();
        pInv1 = Objects.requireNonNull(Bukkit.getPlayer(player1.getUniqueId())).getInventory();
        pInv2 = Objects.requireNonNull(Bukkit.getPlayer(player2.getUniqueId())).getInventory();
        openInventory(Objects.requireNonNull(Bukkit.getPlayer(player1.getUniqueId())), inv1);
        openInventory(Objects.requireNonNull(Bukkit.getPlayer(player2.getUniqueId())), inv2);
        initializeItems();
    }

}
