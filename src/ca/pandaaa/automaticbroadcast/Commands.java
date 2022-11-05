package ca.pandaaa.automaticbroadcast;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {
    // ConfigManager instance //
    ConfigManager config = AutomaticBroadcast.getPlugin().getConfigManager();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String message, String[] args) {
        // If the sender is not a player or the console, return false (prevents from continuing in the function) //
        if(!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) return false;

        // If the command name is "automaticbroadcast", "autobroadcast" or "ab" //
        if (command.getName().equalsIgnoreCase("automaticbroadcast")) {
            // If there are no arguments, sends the error and return false //
            if (args.length == 0) {
                sendUnknownCommandMessage(sender);
                return false;
            }

            // Checks the first arguments //
            switch (args[0].toLowerCase()) {
                // "reload" will reload the configurations //
                // "list" will display all the broadcasts rapidly //
                // Anything else will send the error //
                case "reload":
                    reloadPlugin(sender);
                    break;
                case "list":
                    sendList(sender);
                    break;
                default:
                    sendUnknownCommandMessage(sender);
                    break;
            }
        }
        // Returns false
        return false;
    }

    // Reloads the plugin //
    public void reloadPlugin(CommandSender sender) {
        // If the sender does not have the permission //
        if (!sender.hasPermission("automaticbroadcast.config")) {
            // Sends the noPermission message and return //
            sendNoPermissionMessage(sender);
            return;
        }

        // Reloads the configurations and sends the confirmation message //
        AutomaticBroadcast.getPlugin().reloadConfig(sender);
    }

    // Sends all the broadcast rapidly to the sender of the command //
    public void sendList(CommandSender sender) {
        // If the sender does not have the permission //
        if (!sender.hasPermission("automaticbroadcast.config")) {
            // Sends the noPermission message and return //
            sendNoPermissionMessage(sender);
            return;
        }

        // For all the broadcasts //
        for (String broadcastTitles : config.getBroadcastTitles()) {
            // For all the messages of each broadcast //
            for (String broadcastMessages : config.getBroadcastMessagesList(broadcastTitles)) {
                // Creates a message component which is the message formatted //
                if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && sender instanceof Player)
                    broadcastMessages = PlaceholderAPI.setPlaceholders((Player) sender, broadcastMessages);
                TextComponent message = new TextComponent(TextComponent.fromLegacyText(config.applyFormat(broadcastMessages)));
                BroadcastManager manager = new BroadcastManager();
                // Applies the hover and click event to the component //
                if (sender instanceof Player)
                    manager.setHoverBroadcastEvent(message, config.getBroadcastHoverList(broadcastTitles), (Player) sender);
                else
                    manager.setHoverBroadcastEvent(message, config.getBroadcastHoverList(broadcastTitles), null);
                manager.setClickBroadcastEvent(message, config.getBroadcastClick(broadcastTitles));

                // Sends the message with the correct format and all it's events to the sender //
                sender.spigot().sendMessage(message);
            }
        }
    }

    // Sends the unknownCommand error message //
    private void sendUnknownCommandMessage(CommandSender sender) {
        sender.sendMessage(config.getUnknownCommandMessage());
    }

    // Sends the noPermission error message //
    private void sendNoPermissionMessage(CommandSender sender) {
        sender.sendMessage(config.getNoPermissionMessage());
    }
}
