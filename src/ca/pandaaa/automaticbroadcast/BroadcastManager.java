package ca.pandaaa.automaticbroadcast;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Random;
import java.util.stream.Collectors;

public class BroadcastManager {

    // Classes instances //
    AutomaticBroadcast plugin = AutomaticBroadcast.getPlugin();
    ConfigManager config = plugin.getConfigManager();

    // The index of the broadcast that should be displayed next //
    private int broadcastIndex;

    // Sends the automatic broadcasts (the principal function of the plugin) //
    public void automaticBroadcast() {
        // If the list is empty, return (return prevents from looking at the rest of the code) //
        if (config.getSettings().getBroadcasts().keySet().size() < 1)
            return;

        // If the config "random" option is set to true, call the random broadcast function //
        if (config.getSettings().isRandomOrder())
            sendAutomaticRandomBroadcast();
        else {
            // Calls the broadcast function //
            sendAutomaticBroadcast();

            // Increments the broadcastIndex (which broadcast is being displayed next) //
            if (broadcastIndex == config.getSettings().getBroadcasts().size() - 1)
                broadcastIndex = 0;
            else
                broadcastIndex++;
        }
    }

    // Sends the automatic random broadcast //
    private void sendAutomaticRandomBroadcast() {
        // Getting a random integer (from 0 to the size of the broadcastList (list of all the broadcasts))
        int number = new Random().nextInt(config.getSettings().getBroadcasts().size());

        // If the number chosen randomly is the same as the last displayed broadcast's index, we call the function again //
        // If there is only one broadcast, we continue with the same broadcast (cannot choose another one...) //
        if (number == broadcastIndex && config.getSettings().getBroadcasts().size() != 1)
            sendAutomaticRandomBroadcast();
        else {
            // When the random successfully worked, we assign the value of that random to the broadcastIndex (next broadcast being displayed) //
            broadcastIndex = number;
            sendAutomaticBroadcast();
        }
    }

    // Sends the automatic broadcast //
    private void sendAutomaticBroadcast() {
        Collection<? extends Player> receivers = Bukkit.getOnlinePlayers();

        // Player may receive the message if...:
        // - Is not in a disabled world.
        // - Is not in the exempted players list.
        // - Does not have the exempt permission (if the permission option is enabled)
        // or the permission option is disabled.

        // removes any online player if their username is in the exempted players list, if it has any entries
        if (config.getSettings().getExemptedPlayers().size() > 0)
            receivers = receivers.stream().filter(player -> config.getSettings().getExemptedPlayers().contains(player.getName())).collect(Collectors.toList());

        if (config.getSettings().getDisabledWorlds().size() > 0)
            receivers = receivers.stream().filter(player -> config.getSettings().getDisabledWorlds().contains(player.getWorld().getName())).collect(Collectors.toList());

        if (config.getSettings().isExemptPermission()) {
            // continue filtering players based on their permissions. if they have "automaticbroadcast.exempt", don't send it to them
            receivers = receivers.stream().filter(player -> !player.hasPermission("automaticbroadcast.exempt")).collect(Collectors.toList());
        }

        final Broadcast broadcast = Utils.getBroadcastByIndex(config.getSettings().getBroadcasts(), broadcastIndex);

        // For all applicable receivers //
        for (Player broadcastReceiver : receivers) {
            // If the broadcast has a determined sound, it is played at the location of the player.
            if (broadcast.getSound() != null)
                broadcastReceiver.playSound(broadcastReceiver.getLocation(), broadcast.getSound(), 1, 1);

            // For all the messages in the broadcast (config: broadcastTitle.messages) //
            for (String broadcastMessages : broadcast.getTextLines()) {
                // Changes the placeholder(s) using PAPI (if applicable)
                if (config.getSettings().usingPAPI())
                    broadcastMessages = PlaceholderAPI.setPlaceholders(broadcastReceiver, broadcastMessages);

                // Creates a component message, which is the formatted message //
                TextComponent message = new TextComponent(TextComponent.fromLegacyText(Utils.applyFormat(broadcastMessages)));
                // Calls the functions to add the hover and click events to this component //
                Utils.setHoverEvent(message, broadcast.getHoverLines(), broadcastReceiver);
                Utils.setClickEvent(message, broadcast.getClickText());

                // Sends the message with the correct format and the click and hover events (if applicable) //
                broadcastReceiver.spigot().sendMessage(message);
            }
        }
    }
}