package shortdev.chess;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getPlayer;

public class CommandManager implements CommandExecutor {

    private final Chess plugin;

    public CommandManager(Chess plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("chess")) {
            if (args.length == 0) {
                new ChessGUI(plugin).openInventory(p);
            }
            if (args.length == 1) {

            }
        }
        return false;
    }
}
