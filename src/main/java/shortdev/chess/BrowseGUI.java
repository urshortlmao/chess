package shortdev.chess;

import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

import java.util.Arrays;
import java.util.List;

import static org.bukkit.Material.*;

public class BrowseGUI implements Listener {

    private static Inventory inv;

    private final Chess plugin;

    public BrowseGUI(Chess plugin) {
        this.plugin = plugin;
        inv = Bukkit.createInventory(null, 27, "Chess against a player");
        initializeItems();
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
        if (e.getRawSlot() == 15) {
            p.closeInventory();
            new BrowseMenu(plugin).openInventory(p);
        }
    }

    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

    public void initializeItems() {
        inv.setItem(11, createGuiItem(ARROW, ChatColor.translateAlternateColorCodes('&', "&bInvite a player directly"), null));
        inv.setItem(15, createGuiItem(SPYGLASS, ChatColor.translateAlternateColorCodes('&', "&3Join/host games"), null));
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
    }

    public void openAnvilGUI(Player player, String string) {
        new AnvilGUI.Builder()
                .plugin(plugin)
                .title("Invite player")
                .itemLeft(new ItemStack(Material.PAPER))
                .text(string)
                .onComplete(completion -> Arrays.asList(AnvilGUI.ResponseAction.run(() -> {

                }), AnvilGUI.ResponseAction.close()))
                .open(player);
    }

}
