package ca.pandaaa.automaticbroadcast;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String arg, String[] args) {
        List<String> completionList = new ArrayList<>();
        if (sender.hasPermission("automaticbroadcast.config")) {
            completionList.add("list");
            completionList.add("reload");
        }
        return completionList;
    }
}
