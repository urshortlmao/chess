package shortdev.chess;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
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

    private static Inventory inv;

    private static Inventory pInv;

    private final Chess plugin;

    private static Game game;

    private static GamePlayer player;

    public GameGUI(Chess plugin) {
        this.plugin = plugin;
        inv = Bukkit.createInventory(null, 54, "Chess");
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().equals(AIR)) return;
        final Player p = (Player) e.getWhoClicked();
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(final InventoryCloseEvent e) {
        Game.endGame(game);
    }

    public void initializeItems() {
        Piece[][] pieces = Game.getPieces(game.getPlayer1());
        Piece[][] opponentPieces = Game.getPieces(game.getPlayer2());

        for (int i = 0; i < 72; i++) {
            if (i % 9 < 8) {
                int x = i % 9;
                int y = 8 - i / 9;
                boolean isOccupied = false;
                for (Piece[] pieceArray : pieces) {
                    for (Piece piece : pieceArray) {
                        if (piece != null) {
                            if (piece.getX() == x && piece.getY() == y) {
                                System.out.println(piece.getType().getName());
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
                                System.out.println(piece.getType().getName());
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

    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
        initializeItems();
    }

    public void setPlayer(GamePlayer player) {
        GameGUI.player = player;
        if (Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())).isOnline()) {
            pInv = Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())).getInventory();
            openInventory(Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())));
        } else {
            Bukkit.broadcastMessage("Error");
        }
    }

    public void setGame(Game game) {
        GameGUI.game = game;
    }

}
