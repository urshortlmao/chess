package shortdev.chess.constructors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import shortdev.chess.Chess;

import java.util.List;
import java.util.Objects;

public class PieceType {

    private Chess plugin;

    private String name;

    private boolean highlighted;

    public PieceType(String name) {
        this.name = name;
    }

    public boolean equals(String str) {
        return Objects.equals(str, name);
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem(String color, boolean highlighted) {
        this.highlighted = highlighted;
        if (color.equals("WHITE")) {
            switch (name) {
                case "PAWN":
                    return createGuiItem(Material.SNOWBALL, ChatColor.translateAlternateColorCodes('&', "&fPawn"), null);
                case "ROOK":
                    return createGuiItem(Material.DIORITE_WALL, ChatColor.translateAlternateColorCodes('&', "&fRook"), null);
                case "KNIGHT":
                    return createGuiItem(Material.IRON_AXE, ChatColor.translateAlternateColorCodes('&', "&fKnight"), null);
                case "BISHOP":
                    return createGuiItem(Material.ARROW, ChatColor.translateAlternateColorCodes('&', "&fBishop"), null);
                case "QUEEN":
                    return createGuiItem(Material.WHITE_TULIP, ChatColor.translateAlternateColorCodes('&', "&fQueen"), null);
                case "KING":
                    return createGuiItem(Material.IRON_SHOVEL, ChatColor.translateAlternateColorCodes('&', "&fKing"), null);
            }
        }
        if (color.equals("BLACK")) {
            switch (name) {
                case "PAWN":
                    return createGuiItem(Material.FLINT, ChatColor.translateAlternateColorCodes('&', "&8Pawn"), null);
                case "ROOK":
                    return createGuiItem(Material.POLISHED_DEEPSLATE_WALL, ChatColor.translateAlternateColorCodes('&', "&8Rook"), null);
                case "KNIGHT":
                    return createGuiItem(Material.NETHERITE_AXE, ChatColor.translateAlternateColorCodes('&', "&8Knight"), null);
                case "BISHOP":
                    return createGuiItem(Material.TIPPED_ARROW, ChatColor.translateAlternateColorCodes('&', "&8Bishop"), null);
                case "QUEEN":
                    return createGuiItem(Material.WITHER_ROSE, ChatColor.translateAlternateColorCodes('&', "&8Queen"), null);
                case "KING":
                    return createGuiItem(Material.NETHERITE_SHOVEL, ChatColor.translateAlternateColorCodes('&', "&8King"), null);
            }
        }
        return null;
    }

    protected ItemStack createGuiItem(final Material material, final String name, final List<String> lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (material.equals(Material.TIPPED_ARROW)) {
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setBasePotionData(new PotionData(PotionType.WEAKNESS));
            potionMeta.setDisplayName(name);
            if (lore != null) {
                potionMeta.setLore(lore);
            }
            if (highlighted) {
                item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                potionMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            potionMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(potionMeta);
        } else {
            meta.setDisplayName(name);
            if (lore != null) {
                meta.setLore(lore);
            }
            if (highlighted) {
                item.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 1);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
            item.setItemMeta(meta);
        }
        return item;
    }

}
