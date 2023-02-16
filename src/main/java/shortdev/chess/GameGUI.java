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

import java.util.List;
import java.util.Objects;

import static org.bukkit.Material.*;

public class GameGUI implements Listener {

    private Inventory inv1, inv2, pInv1, pInv2;

    private static Game game;

    private GamePlayer player1, player2;

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

    public void initializeItems(Player player) {
        boolean isPlayer1 = Objects.equals(Bukkit.getPlayer(player1.getUniqueId()), player);
        GamePlayer p = isPlayer1 ? player1 : player2;
        Inventory inv = isPlayer1 ? inv1 : inv2;
        Inventory pInv = isPlayer1 ? pInv1 : pInv2;
        Piece[][] pieces = game.getPieces(p);
        Piece[][] opponentPieces = game.getPieces(game.getOpponent(p));

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
                                    inv.setItem(i, piece.getType().getItem(piece.getColor()));
                                } else {
                                    pInv.setItem(i - 45, piece.getType().getItem(piece.getColor()));
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
                                    inv.setItem(i, piece.getType().getItem(piece.getColor()));
                                } else {
                                    pInv.setItem(i - 45, piece.getType().getItem(piece.getColor()));
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
                            inv.setItem(i, new ItemStack(WHITE_STAINED_GLASS_PANE));
                        } else {
                            pInv.setItem(i - 45, new ItemStack(WHITE_STAINED_GLASS_PANE));
                        }
                    } else {
                        if (i < 54) {
                            inv.setItem(i, new ItemStack(BLACK_STAINED_GLASS_PANE));
                        } else {
                            pInv.setItem(i - 45, new ItemStack(BLACK_STAINED_GLASS_PANE));
                        }
                    }
                }
            } else {
                if (i < 54) {
                    inv.setItem(i, new ItemStack(GRAY_STAINED_GLASS_PANE));
                } else {
                    pInv.setItem(i - 45, new ItemStack(GRAY_STAINED_GLASS_PANE));
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
        initializeItems((Player) ent);
    }

    public void setGame(Game game) {
        GameGUI.game = game;
        player1 = game.getPlayer1();
        player2 = game.getPlayer2();
        pInv1 = Objects.requireNonNull(Bukkit.getPlayer(player1.getUniqueId())).getInventory();
        pInv2 = Objects.requireNonNull(Bukkit.getPlayer(player2.getUniqueId())).getInventory();
        openInventory(Objects.requireNonNull(Bukkit.getPlayer(player1.getUniqueId())), inv1);
        openInventory(Objects.requireNonNull(Bukkit.getPlayer(player2.getUniqueId())), inv2);
    }

}
