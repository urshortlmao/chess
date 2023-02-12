package shortdev.chess.constructors;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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

    public PieceType(String name) {
        this.name = name;
    }

    public boolean equals(String str) {
        return Objects.equals(str, name);
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem(String color) {
        if (color.equals("WHITE")) {
            switch (name) {
                case "PAWN":
                    createGuiItem(Material.SNOWBALL, ChatColor.translateAlternateColorCodes('&', "&fPawn"), null);
                case "ROOK":
                    createGuiItem(Material.DIORITE_WALL, ChatColor.translateAlternateColorCodes('&', "&fRook"), null);
                case "KNIGHT":
                    createGuiItem(Material.IRON_AXE, ChatColor.translateAlternateColorCodes('&', "&fKnight"), null);
                case "BISHOP":
                    createGuiItem(Material.ARROW, ChatColor.translateAlternateColorCodes('&', "&fBishop"), null);
                case "QUEEN":
                    createGuiItem(Material.WHITE_TULIP, ChatColor.translateAlternateColorCodes('&', "&fQueen"), null);
                case "KING":
                    createGuiItem(Material.IRON_SHOVEL, ChatColor.translateAlternateColorCodes('&', "&fKing"), null);
            }
        }
        if (color.equals("BLACK")) {
            switch (name) {
                case "PAWN":
                    createGuiItem(Material.FLINT, ChatColor.translateAlternateColorCodes('&', "&0Pawn"), null);
                case "ROOK":
                    createGuiItem(Material.POLISHED_DEEPSLATE_WALL, ChatColor.translateAlternateColorCodes('&', "&0Rook"), null);
                case "KNIGHT":
                    createGuiItem(Material.NETHERITE_AXE, ChatColor.translateAlternateColorCodes('&', "&0Knight"), null);
                case "BISHOP":
                    createGuiItem(Material.TIPPED_ARROW, ChatColor.translateAlternateColorCodes('&', "&0Bishop"), null);
                case "QUEEN":
                    createGuiItem(Material.WHITE_TULIP, ChatColor.translateAlternateColorCodes('&', "&0Queen"), null);
                case "KING":
                    createGuiItem(Material.IRON_SHOVEL, ChatColor.translateAlternateColorCodes('&', "&0King"), null);
            }
        }
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
            item.setItemMeta(potionMeta);
        } else {
            meta.setDisplayName(name);
            if (lore != null) {
                meta.setLore(lore);
            }
            item.setItemMeta(meta);
        }
        return item;
    }

}
