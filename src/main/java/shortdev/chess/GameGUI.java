package shortdev.chess;

import net.wesjd.anvilgui.AnvilGUI;
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

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.bukkit.Material.*;

public class GameGUI implements Listener {

    private static Inventory inv;

    private final Chess plugin;

    private Game game;

    private GamePlayer player;

    public GameGUI(Chess plugin) {
        this.plugin = plugin;
        inv = Bukkit.createInventory(null, 54, "Chess");
        openInventory(Objects.requireNonNull(Bukkit.getPlayer(player.getUniqueId())));
    }

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;
        e.setCancelled(true);
        final ItemStack clickedItem = e.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().equals(AIR)) return;
        final Player p = (Player) e.getWhoClicked();
        if (e.getRawSlot() == 11) {
            p.closeInventory();
            openAnvilGUI(p,"Type here");
        }
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
        if (player.isColor("WHITE")) {
            for (Piece piece : pieces) {
                int x = piece.getX();
                int y = piece.getY();
                int i = 8*y + x - 8;
                if (i < 52) {
                    inv.setItem(i, piece.getType().getItem("WHITE"));
                }
            }
            for (Piece piece : opponentPieces) {
                int x = piece.getX();
                int y = piece.getY();
                int i = 8*y + x - 8;
                if (i < 52) {
                    inv.setItem(i, piece.getType().getItem("BLACK"));
                }
            }
        }
        if (player.isColor("BLACK")) {
            for (Piece piece : pieces) {
                int x = piece.getX();
                int y = piece.getY();
                int i = 73 - x - 8*y;
                if (i < 52) {
                    inv.setItem(i, piece.getType().getItem("BLACK"));
                }
            }
            for (Piece piece : opponentPieces) {
                int x = piece.getX();
                int y = piece.getY();
                int i = 73 - x - 8*y;
                if (i < 52) {
                    inv.setItem(i, piece.getType().getItem("WHITE"));
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
        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void openAnvilGUI(Player player, String string) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Chess with a player")
                .itemLeft(new ItemStack(Material.PAPER))
                .text(string)
                .onComplete(completion -> Arrays.asList(AnvilGUI.ResponseAction.run(() -> {

                }), AnvilGUI.ResponseAction.close()))
                .open(player);
    }

}
