package shortdev.chess;

import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.EnchantmentWrapper;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import shortdev.chess.constructors.Game;
import shortdev.chess.constructors.GamePlayer;
import shortdev.chess.constructors.Move;
import shortdev.chess.constructors.Piece;

import java.util.Objects;

public class HighlightedGUI {

    private final int index;

    private final HumanEntity ent;

    public HighlightedGUI(int i, HumanEntity ent) {
        index = i;
        this.ent = ent;
        highlightPossibleMoves();
    }

    public void highlightPossibleMoves() {
        Inventory inv = GameGUI.guiOf(ent);
        Inventory pInv = ent.getInventory();
        GamePlayer g = Game.getGamePlayer((Player) ent);
        Game game = Game.inGame(g);
        Piece[][] pieces = Game.getPieces(g);
        for (Piece[] pieceArray : pieces) {
            for (Piece piece : pieceArray) {
                if (index % 8 == piece.getX() && 8 - index/9 == piece.getY()) {
                    ItemStack item;
                    if (index < 54) {
                        item = inv.getItem(index);
                    } else if (index < 72) {
                        item = pInv.getItem(index - 54);
                    } else {
                        return;
                    }
                    assert item != null;
                    ItemMeta meta = item.getItemMeta();
                    assert meta != null;
                    meta.addEnchant(Objects.requireNonNull(EnchantmentWrapper.getByKey(NamespacedKey.minecraft("sharpness"))), 1, false);
                    meta.addItemFlags(ItemFlag.HIDE_ENCHANTS); //, ItemFlag.HIDE_ATTRIBUTES);
                    item.setItemMeta(meta);
                    inv.setItem(index, item);
                    if (index < 54) {
                        inv.setItem(index, item);
                    } else {
                        pInv.setItem(index - 54, item);
                    }
                    assert game != null;
                    for (Move move : game.findPossibleMoves(piece)) {
                        int x = move.getAfterX();
                        int y = move.getAfterY();
                        int i = x + 8*y - 9;
                        ItemStack itemStack;
                        if (i < 54) {
                            itemStack = inv.getItem(i);
                        } else if (i < 72) {
                            itemStack = pInv.getItem(i - 54);
                        } else {
                            return;
                        }
                        assert itemStack != null;
                        ItemMeta itemMeta = itemStack.getItemMeta();
                        assert itemMeta != null;
                        itemMeta.addEnchant(Objects.requireNonNull(EnchantmentWrapper.getByKey(NamespacedKey.minecraft("sharpness"))), 1, false);
                        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS); //, ItemFlag.HIDE_ATTRIBUTES);
                        itemStack.setItemMeta(itemMeta);
                        System.out.println(itemMeta.getEnchants().keySet());
                        System.out.println(itemStack.getEnchantments().keySet());

                        if (i < 54) {
                            inv.setItem(i, itemStack);
                        } else {
                            pInv.setItem(i - 54, itemStack);
                        }
                    }
                    return;
                }
            }
        }
    }

}
