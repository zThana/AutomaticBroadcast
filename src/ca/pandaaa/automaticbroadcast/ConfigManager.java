package ca.pandaaa.automaticbroadcast;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigManager {
    // Attributes //
    private final FileConfiguration config;
    private final FileConfiguration messages;

    // Constructor //
    public ConfigManager(FileConfiguration config, FileConfiguration messages) {
        this.config = config;
        this.messages = messages;
    }

    ////////////////
    // CONFIG.YML //
    ////////////////

    // Returns the time between every broadcast (not in second for the moment) //
    public int getTimeBetweenMessages() {
        return config.getInt("time-between-messages");
    }

    // Returns true if the broadcasts should be sent randomly //
    public boolean getRandom() {
        return config.getBoolean("random");
    }

    // Returns true if the exempt permission is enabled //
    private boolean getExemptPermission() {
        return config.getBoolean("exempt-permission");
    }

    // Returns a list of the disabled world(s) //
    private List<String> getDisabledWorlds() {
        return config.getStringList("disabled-worlds");
    }

    // Returns a list of the exempted player(s) //
    private List<String> getExemptedPlayers() {
        return config.getStringList("exempted-players");
    }

    // Returns the unknown command message //
    public String getUnknownCommandMessage() {
        return applyFormat(config.getString("unknown-command"));
    }

    // Returns the no permission message //
    public String getNoPermissionMessage() {
        return applyFormat(config.getString("no-permission"));
    }

    // Returns the plugin reload command message //
    public String getPluginReloadMessage() {
        return applyFormat(config.getString("plugin-reload"));
    }

    //////////////////
    // MESSAGES.YML //
    //////////////////

    // Returns a list of all the existing broadcasts (Staff, Discord, etc.) //
    public String[] getBroadcastTitles() {
        // Creates a Set with all the titles of the messages directly from the "messages.yml" file //
        Set<String> broadcastTitlesSet = messages.getConfigurationSection("broadcasts").getKeys(false);
        // Creates a list of the size of the "broadcastTitlesSet" Set //
        String[] broadcastTitlesList = new String[broadcastTitlesSet.size()];
        // Changes the created board to set the collected titles (Set) inside it //
        return broadcastTitlesSet.toArray(broadcastTitlesList);
    }

    // Returns a list of all the messages in a broadcast //
    public List<String> getBroadcastMessagesList(String broadcastTitle) {
        return messages.getStringList("broadcasts." + broadcastTitle + ".messages");
    }

    // Returns the broadcast "click" message/command/suggestion string if applicable //
    public String getBroadcastClick(String broadcastTitle) {
        return messages.getString("broadcasts." + broadcastTitle + ".click");
    }

    // Returns the broadcast "hover" message(s) list if applicable //
    public List<String> getBroadcastHoverList(String broadcastTitle) {
        return messages.getStringList("broadcasts." + broadcastTitle + ".hover");
    }

    // Tries to get the broadcast sound value (if it exist...) //
    public Sound getBroadcastSound(String broadcastTitle) {
        try {
            return Sound.valueOf(messages.getString("broadcasts." + broadcastTitle + ".sound"));
        } catch(Exception exception) {
            return null;
        }
    }

    ////////////
    // OTHERS //
    ////////////

    // Applies the format used threw the whole plugin (returns a formatted string) //
    public String applyFormat(String message) {
        // Replaces the double arrows automatically //
        message = message.replace(">>", "»").replace("<<", "«");

        // Changes the hex color code to set the colors //
        Pattern hexPattern = Pattern.compile("&#([A-Fa-f0-9]){6}");
        Matcher matcher = hexPattern.matcher(message);
        while (matcher.find()) {
            ChatColor hexColor = ChatColor.of(matcher.group().substring(1));
            String before = message.substring(0, matcher.start());
            String after = message.substring(matcher.end());
            message = before + hexColor + after;
            matcher = hexPattern.matcher(message);
        }

        // Returns the message with the correct colors and formats //
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    // Returns the permission for a player to receive a broadcast.
    public boolean playerCanReceiveBroadcast(Player player) {
        // True if list of disabled world(s) contains the player's world.
        boolean disabledWorldsContainsPlayerWorld = getDisabledWorlds().contains(player.getWorld().getName());
        // True if player has the permission to be exempted.
        boolean playerHasPermission = player.hasPermission("automaticbroadcast.exempt");
        // True if permission is disabled in config.
        boolean permissionEnabled = getExemptPermission();
        // True if list of exempted player(s) contains the player.
        boolean exemptedPlayersContainsPlayer = getExemptedPlayers().contains(player.getName());

        // Returns true if the player...:
        // - Is not in a disabled world.
        // - Is not in the exempted players list.
        // - Does not have the exempt permission (if the permission option is enabled)
        // or the permission option is disabled.
        return !disabledWorldsContainsPlayerWorld && !(permissionEnabled && playerHasPermission) && !exemptedPlayersContainsPlayer;
    }
}
