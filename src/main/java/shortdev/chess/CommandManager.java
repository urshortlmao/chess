package shortdev.chess;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import shortdev.chess.constructors.Game;
import shortdev.chess.constructors.GamePlayer;

import java.util.Objects;
import java.util.Random;

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
                return true;
            }
            if (args.length == 1) {
                Random rand = new Random();
                if (rand.nextFloat() >= 0.5) {
                    GamePlayer player1 = new GamePlayer(p.getUniqueId(), "WHITE");
                    GamePlayer player2 = new GamePlayer(Objects.requireNonNull(getPlayer(args[0])).getUniqueId(), "BLACK");
                    Game game = new Game(player1, player2, String.valueOf(p.getUniqueId()));
                    Game.createGame(game);
                    return true;
                }
                GamePlayer player1 = new GamePlayer(p.getUniqueId(), "BLACK");
                GamePlayer player2 = new GamePlayer(Objects.requireNonNull(getPlayer(args[0])).getUniqueId(), "WHITE");
                Game game = new Game(player1, player2, String.valueOf(p.getUniqueId()));
                Game.createGame(game);
                return true;
            }
        }
        return false;
    }
}
