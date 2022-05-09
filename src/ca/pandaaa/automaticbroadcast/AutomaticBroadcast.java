package ca.pandaaa.automaticbroadcast;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;

public class AutomaticBroadcast extends JavaPlugin {
    // Generates the messages.yml file //
    private File messagesFile;
    private FileConfiguration messagesConfig;

    // Attributes //
    private static AutomaticBroadcast plugin;
    private ConfigManager config;
    private BroadcastManager manager;
    private BukkitTask automaticBroadcastTask;

    // On the plugin enabling //
    @Override
    public void onEnable() {
        // BStats initialization (both lines are important) //
        int pluginId = 13749;
        Metrics metrics = new Metrics(this, pluginId);

        // Saves the default configs (creates them in the case that they don't already exist) //
        saveDefaultConfig();
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists())
            saveResource("messages.yml", false);
        messagesConfig = new YamlConfiguration();

        try {
            messagesConfig.load(messagesFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        // Sets the plugin instance... //
        plugin = this;

        // Creates the attributes and the command //
        setAttributesAndCommands();

        // Starts the broadcasting //
        startBroadcasting();

        // Console message when the plugin is fully enabled //
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "   &3_____  &b_____"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &3|  _  |&b| __  |  &3Auto&bmatic&8Broad&7cast"));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &3|     |&b| __ -|    &7Version " + getDescription().getVersion()));
        getServer().getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "  &3|__|__|&b|_____|      &7by &8Pa&7nd&5aaa"));
        getServer().getConsoleSender().sendMessage("");

    }

    // Creates or changes the attributes and the command //
    private void setAttributesAndCommands() {
        // Changes the config class by a new one with the new datas //
        config = new ConfigManager(getConfig(), messagesConfig);
        // Changes the manager by a brand new one with the new datas //
        manager = new BroadcastManager();
        // Changes the command executor and completer to work with the new datas //
        getCommand("AutomaticBroadcast").setExecutor(new Commands());
        getCommand("AutomaticBroadcast").setTabCompleter(new TabCompletion());
    }

    // Returns the plugin //
    public static AutomaticBroadcast getPlugin() {
        return plugin;
    }

    // Returns the config (should be the only config instance allowed in any classes) //
    public ConfigManager getConfigManager() {
        return config;
    }

    // Starts the broadcasting... //
    private void startBroadcasting() {
        // Stops the runnable if it was already instantiated //
        if(automaticBroadcastTask != null) automaticBroadcastTask.cancel();
        // Generates a runnable that executes the following code in loop //
        automaticBroadcastTask = new BukkitRunnable() {
            @Override
            public void run() {
                // Calls the principal function of the plugin //
                manager.automaticBroadcast();
            }
        // Time between every interval (in seconds) //
        }.runTaskTimer(plugin, 0, 20L * config.getTimeBetweenMessages());
    }

    // Reloads the configurations //
    public void reloadConfig(CommandSender sender) {
        // Deletes the config data (not in the file but in the program) //
        plugin.reloadConfig();
        // Replaces the messages data by the ones from the file //
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);

        // Changes the attributes and the command //
        setAttributesAndCommands();

        // Sends the confirmation message to the command executor //
        sender.sendMessage(config.getPluginReloadMessage());
        // Starts the broadcasting //
        startBroadcasting();
    }
}
