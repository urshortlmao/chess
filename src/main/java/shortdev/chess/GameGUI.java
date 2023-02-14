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
import shortdev.chess.constructors.PieceType;

import java.util.List;
import java.util.Objects;

import static org.bukkit.Material.*;

public class GameGUI implements Listener {

    private static Inventory inv;

    private static Inventory pInv;

    private final Chess plugin;

    private Game game;

    private GamePlayer player;

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

    public void initializeItems() {
        List<Piece> pieces = game.getPieces(player);
        List<Piece> opponentPieces = game.getPieces(game.getOpponent(player));
        int start = Math.max(0, game.getScrollPosition(player) * 9);

        for (int i = 0; i < 72; i++) {
            if (i % 9 < 8) {
                int x = i % 9;
                int y = 8 - i / 9;
                boolean isOccupied = false;
                for (Piece piece : pieces) {
                    if (piece.getX() == x && piece.getY() == y) {
                        isOccupied = true;
                        break;
                    }
                }
                for (Piece piece : opponentPieces) {
                    if (piece.getX() == x && piece.getY() == y) {
                        isOccupied = true;
                        break;
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
                        if (i < 53) {
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

        // add pieces to GUI
        int count = 0;
        for (Piece piece : pieces) {
            int x = piece.getX();
            int y = piece.getY();
            int index = (8 - y) * 9 + x;
            if (index >= start && count < 48) {
                if (index - start < 54) {
                    inv.setItem(index - start, PieceType.getItem(piece.getType(), piece.getColor()));
                } else {
                    pInv.setItem(index - start - 45, PieceType.getItem(piece.getType(), piece.getColor()));
                }
                count++;
            }
        }
        for (Piece piece : opponentPieces) {
            int x = piece.getX();
            int y = piece.getY();
            int index = (8 - y) * 9 + x;
            if (index >= start && count < 48) {
                if (index - start < 54) {
                    inv.setItem(index - start, PieceType.getItem(piece.getType(), piece.getColor()));
                } else {
                    pInv.setItem(index - start - 45, PieceType.getItem(piece.getType(), piece.getColor()));
                }
                count++;
            }
        }

        // add scrolling item to GUI
        if (start > 0) {
            inv.setItem(53, new ItemStack(ARROW, 1));
        } else {
            inv.setItem(53, new ItemStack(GRAY_STAINED_GLASS_PANE, 1));
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
        this.player = player;
        if (Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())).isOnline()) {
            pInv = Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())).getInventory();
            openInventory(Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())));
        } else {
            Bukkit.broadcastMessage("Error");
        }
    }

    public void setGame(Game game) {
        this.game = game;
    }

}
