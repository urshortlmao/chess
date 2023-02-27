package shortdev.chess;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shortdev.chess.constructors.Game;
import shortdev.chess.constructors.GamePlayer;
import shortdev.chess.constructors.Piece;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Material.*;

public class GameGUI implements Listener {

    private Inventory inv1, inv2, pInv1, pInv2;

    private Game game;

    private GamePlayer player1, player2;

    private Player p1, p2;

    private Piece[][] pieces1 = new Piece[8][6];

    private Piece[][] pieces2 = new Piece[8][6];

    private static HashMap<HumanEntity, Inventory> guiMap = new HashMap<>();

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
        pieces1 = Game.getPieces(player1);
        pieces2 = Game.getPieces(player2);
        openInventory(p1, inv1);
        openInventory(p2, inv2);
    }

    public static HashMap<HumanEntity, Inventory> getGuiMap() {
        return guiMap;
    }

    public static Inventory guiOf(HumanEntity ent) {
        return guiMap.get(ent);
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (e.getInventory().equals(inv1) || e.getInventory().equals(inv2)) {
            e.setCancelled(true);
            int i = e.getRawSlot();
            new HighlightedGUI(i, e.getWhoClicked());
        }
        if (e.getInventory().equals(pInv1) || e.getInventory().equals(pInv2)) {
            e.setCancelled(true);
            int i = e.getRawSlot() + 54;
            new HighlightedGUI(i, e.getWhoClicked());
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

    public void initializeItems() {
        pInv1.clear();
        pInv2.clear();

        for (int i = 0; i < 72; i++) {
            if (i % 9 < 8) {
                int x = i % 9 + 1;
                int y = 8 - i / 9;
                boolean isOccupied = false;
                for (Piece[] pieceArray : pieces1) {
                    for (Piece piece : pieceArray) {
                        if (piece != null) {
                            if (piece.getX() == x && piece.getY() == y) {
                                if (i < 54) {
                                    inv1.setItem(i, piece.getType().getItem(piece.getColor(), false));
                                } else {
                                    pInv1.setItem(i - 45, piece.getType().getItem(piece.getColor(), false));
                                }
                                if (i < 18) {
                                    pInv2.setItem(25 - i, piece.getType().getItem(piece.getColor(), false));
                                } else {
                                    inv2.setItem(70 - i, piece.getType().getItem(piece.getColor(), false));
                                }
                                isOccupied = true;
                                break;
                            }
                        }
                    }
                }
                for (Piece[] pieceArray : pieces2) {
                    for (Piece piece : pieceArray) {
                        if (piece != null) {
                            if (piece.getX() == x && piece.getY() == y) {
                                if (i < 54) {
                                    inv1.setItem(i, piece.getType().getItem(piece.getColor(), false));
                                } else {
                                    pInv1.setItem(i - 45, piece.getType().getItem(piece.getColor(), false));
                                }
                                if (i < 18) {
                                    pInv2.setItem(25 - i, piece.getType().getItem(piece.getColor(), false));
                                } else {
                                    inv2.setItem(70 - i, piece.getType().getItem(piece.getColor(), false));
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
                            inv1.setItem(i, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null));
                            inv2.setItem(i, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null));
                        } else {
                            pInv1.setItem(i - 45, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null));
                            pInv2.setItem(i - 45, createGuiItem(WHITE_STAINED_GLASS_PANE, " ", null));
                        }
                    } else {
                        if (i < 54) {
                            inv1.setItem(i, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null));
                            inv2.setItem(i, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null));
                        } else {
                            pInv1.setItem(i - 45, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null));
                            pInv2.setItem(i - 45, createGuiItem(BLACK_STAINED_GLASS_PANE, " ", null));
                        }
                    }
                }
            } else {
                if (i < 54) {
                    inv1.setItem(i, createGuiItem(GRAY_STAINED_GLASS_PANE, " ", null));
                    inv2.setItem(i, createGuiItem(GRAY_STAINED_GLASS_PANE, " ", null));
                } else {
                    pInv1.setItem(i - 45, createGuiItem(GRAY_STAINED_GLASS_PANE, " ", null));
                    pInv2.setItem(i - 45, createGuiItem(GRAY_STAINED_GLASS_PANE, " ", null));
                }
            }
        }
    }


    protected ItemStack createGuiItem(final Material material, final String name, final List<String> lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        meta.setDisplayName(name);
        if (lore != null) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    public void openInventory(final HumanEntity ent, Inventory inv) {
        ent.openInventory(inv);
        guiMap.put(ent, inv);
        initializeItems();
    }

}
