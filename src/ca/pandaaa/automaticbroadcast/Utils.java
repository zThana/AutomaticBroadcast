package ca.pandaaa.automaticbroadcast;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {    // Applies the hover event on the broadcast //
    public static void setHoverEvent(TextComponent component, List<String> hoverMessagesList, Player receiver) {
        // Checks for empty hover arguments.
        if (hoverMessagesList.size() == 0)
            return;

        // Creates a component builder //
        ComponentBuilder hoverMessageBuilder = new ComponentBuilder();
        int hoverLine = 0;
        // For all the hover messages of the broadcast (config: broadcastTitle.hover) //
        for (String hoverMessages : hoverMessagesList) {
            if (receiver != null && Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null)
                hoverMessages = PlaceholderAPI.setPlaceholders(receiver, hoverMessages);

            // Adds the message to the component builder //
            TextComponent textComponent = new TextComponent(TextComponent.fromLegacyText(applyFormat(hoverMessages)));
            hoverMessageBuilder.append(textComponent);
            // Changes the line if not at the last message //
            if (hoverLine != (hoverMessagesList.size() - 1))
                hoverMessageBuilder.append("\n");

            hoverLine++;
        }

        // Applies the hover message (the component builder) to the message desired //
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverMessageBuilder.create()));
    }

    // Applies the click event on the broadcast //
    public static void setClickEvent(TextComponent component, String click) {
        // If the click string is null, return //
        if (click == null || click.length() == 0)
            return;

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

    // Applies the format used threw the whole plugin (returns a formatted string) //
    public static String applyFormat(String message) {
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

    // Tries to get the broadcast sound value (if it exist...), otherwise returns null //
    public static Sound validateSound(String soundName) {
        try {
            return Sound.valueOf(soundName);
        } catch (Exception exception) {
            return null;
        }
    }

    public static Broadcast getBroadcastByIndex(LinkedHashMap<String, Broadcast> map, int index) {
        return map.get((map.keySet().toArray())[index]);
    }
}