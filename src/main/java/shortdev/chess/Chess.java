package shortdev.chess;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class Chess extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getServer().getPluginManager().registerEvents(new ChessGUI(this), this);
        getServer().getPluginManager().registerEvents(new BrowseGUI(this), this);
        getServer().getPluginManager().registerEvents(new SettingsGUI(this), this);
        getServer().getPluginManager().registerEvents(new GameGUI(this), this);
        Objects.requireNonNull(this.getCommand("chess")).setExecutor(new CommandManager(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
