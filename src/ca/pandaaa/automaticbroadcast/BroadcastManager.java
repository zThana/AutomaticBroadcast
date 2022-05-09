package ca.pandaaa.automaticbroadcast;

import java.util.List;
import java.util.Random;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class BroadcastManager {

    // Classes instances //
    AutomaticBroadcast plugin = AutomaticBroadcast.getPlugin();
    ConfigManager config = plugin.getConfigManager();

    // The index of the broadcast that should be displayed next //
    private int broadcastIndex;

    // Sends the automatic broadcasts (the principal function of the plugin) //
    public void automaticBroadcast() {
        // Creates a list of the broadcasts' titles (Discord, Staff, Plugin) //
        String[] broadcastList = config.getBroadcastTitles();

        // If the list is empty, return (return prevents from looking at the rest of the code) //
        if (broadcastList.length < 1) return;
        // If the config "random" option is set to true, call the random broadcast function //
        if (config.getRandom()) {
            sendAutomaticRandomBroadcast(broadcastList);
            return;
        }

        // Calls the broadcast function //
        sendAutomaticBroadcast(broadcastList);
        // Increments the broadcastIndex (which broadcast is being displayed next) //
        if (broadcastIndex == broadcastList.length - 1) broadcastIndex = 0;
        else broadcastIndex++;
    }

    // Sends the automatic random broadcast //
    private void sendAutomaticRandomBroadcast(String[] broadcastList) {
        // Getting a random integer (from 0 to the size of the broadcastList (list of all the broadcasts))
        int number = new Random().nextInt(broadcastList.length);

        // If the number chosen randomly is the same as the last displayed broadcast's index, we call the function again //
        // If there is only one broadcast, we continue with the same broadcast (cannot choose another one...) //
        if (number == broadcastIndex && broadcastList.length != 1) {
            sendAutomaticRandomBroadcast(broadcastList);
            return;
        }

        // When the random successfully worked, we assign the value of that random to the broadcastIndex (next broadcast being displayed) //
        broadcastIndex = number;

        sendAutomaticBroadcast(broadcastList);
    }

    // Sends the automatic broadcast //
    private void sendAutomaticBroadcast(String[] broadcastList) {
        // For all the online players //
        for (Player broadcastReceivers : Bukkit.getOnlinePlayers()) {
            // If the player can receive the broadcast (see ConfigManager.playerCanReceiveBroadcast) //
            if (config.playerCanReceiveBroadcast(broadcastReceivers)) {
                // If the broadcast has a determined sound, it is played at the location of the player.
                if (config.getBroadcastSound(broadcastList[broadcastIndex]) != null)
                    broadcastReceivers.playSound(broadcastReceivers.getLocation(),
                            config.getBroadcastSound(broadcastList[broadcastIndex]), 1, 1);

                // For all the messages in the broadcast (config: broadcastTitle.messages) //
                for (String broadcastMessages : config.getBroadcastMessagesList(broadcastList[broadcastIndex])) {

                    // Changes the placeholder(s) using PAPI (if applicable)
                    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                        broadcastMessages = PlaceholderAPI.setPlaceholders(broadcastReceivers, broadcastMessages);

                    // Creates a component message, which is the formatted message //
                    TextComponent message = new TextComponent(TextComponent.fromLegacyText(config.applyFormat(broadcastMessages)));
                    // Calls the functions to add the hover and click events to this component //
                    setHoverBroadcastEvent(message, config.getBroadcastHoverList(broadcastList[broadcastIndex]), broadcastReceivers);
                    setClickBroadcastEvent(message, config.getBroadcastClick(broadcastList[broadcastIndex]));

                    // Sends the message with the correct format and the click and hover events (if applicable) //
                    broadcastReceivers.spigot().sendMessage(message);
                }
            }
        }
    }

    // Applies the hover event on the broadcast //
    public void setHoverBroadcastEvent(TextComponent component, List<String> hoverMessagesList, Player broadcastReceivers) {
        // Checks for empty hover arguments.
        if(hoverMessagesList.size() == 0)
            return;

        // Creates a component builder //
        ComponentBuilder hoverMessageBuilder = new ComponentBuilder();
        int hoverLine = 0;
        // For all the hover messages of the broadcast (config: broadcastTitle.hover) //
        for (String hoverMessages : hoverMessagesList) {
            if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && broadcastReceivers != null)
                hoverMessages = PlaceholderAPI.setPlaceholders(broadcastReceivers, hoverMessages);
            // Adds the message to the component builder //
            TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(config.applyFormat(hoverMessages)));
            hoverMessageBuilder.append(textComponent);
            // Changes the line if not at the last message //
            if (hoverLine != (hoverMessagesList.size() - 1)) {
                hoverMessageBuilder.append("\n");
            }
            hoverLine++;
        }

        // Applies the hover message (the component builder) to the message desired //
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMessageBuilder.create()));
    }

    // Applies the click event on the broadcast //
    public void setClickBroadcastEvent(TextComponent component, String click) {
        // If the click string is null, return //
        if (click == null || click.length() == 0) return;

        // Checks the char at the start of the click string (config: broadcastTitle.click)
        switch (click.charAt(0)) {
            // '/' suggests a command (with the /) //
            // '*' suggests a message (without the *) //
            // Anything else will try to open a link (will not work if the link is not a real link) //
            case '/':
                component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, click));
                break;
            case '*':
                component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, click.substring(1)));
                break;
            default:
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, click));
                break;
        }
    }
}
