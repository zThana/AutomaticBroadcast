package ca.pandaaa.automaticbroadcast;

import java.util.LinkedHashMap;
import java.util.List;

public class Settings {

    private LinkedHashMap<String, Broadcast> broadcasts;
    private List<String> disabledWorlds, exemptedPlayers;
    private int timeBetweenMessages;
    private boolean randomOrder, exemptPermission, usePAPI;
    // Could perhaps turn the messages into its own Enum //
    private String unknownCommandMessage, noPermissionMessage, pluginReloadMessage;

    public Settings(LinkedHashMap<String, Broadcast> broadcasts, List<String> disabledWorlds, List<String> exemptedPlayers, int timeBetweenMessages, boolean randomOrder, boolean exemptPermission, boolean usePAPI, String unknownCommandMessage, String noPermissionMessage, String pluginReloadMessage) {
        this.broadcasts = broadcasts;
        this.disabledWorlds = disabledWorlds;
        this.exemptedPlayers = exemptedPlayers;
        this.timeBetweenMessages = timeBetweenMessages;
        this.randomOrder = randomOrder;
        this.exemptPermission = exemptPermission;
        this.usePAPI = usePAPI;
        this.unknownCommandMessage = unknownCommandMessage;
        this.noPermissionMessage = noPermissionMessage;
        this.pluginReloadMessage = pluginReloadMessage;
    }

    public void setBroadcasts(LinkedHashMap<String, Broadcast> broadcasts) {
        this.broadcasts = broadcasts;
    }

    public LinkedHashMap<String, Broadcast> getBroadcasts() {
        return broadcasts;
    }

    // Returns a list of the disabled world(s) //
    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }

    public void setDisabledWorlds(List<String> disabledWorlds) {
        this.disabledWorlds = disabledWorlds;
    }

    // Returns a list of the exempted player(s) //
    public List<String> getExemptedPlayers() {
        return exemptedPlayers;
    }

    public void setExemptedPlayers(List<String> exemptedPlayers) {
        this.exemptedPlayers = exemptedPlayers;
    }

    // Returns the time between every broadcast in seconds //
    public int getTimeBetweenMessages() {
        return timeBetweenMessages;
    }

    public void setTimeBetweenMessages(int timeBetweenMessages) {
        this.timeBetweenMessages = timeBetweenMessages;
    }

    // Returns true if the broadcasts should be sent randomly //
    public boolean isRandomOrder() {
        return randomOrder;
    }

    public void setRandomOrder(boolean randomOrder) {
        this.randomOrder = randomOrder;
    }

    // Returns true if the exempt permission is enabled //
    public boolean isExemptPermission() {
        return exemptPermission;
    }

    public void setExemptPermission(boolean exemptPermission) {
        this.exemptPermission = exemptPermission;
    }

    // Returns the unknown command message //
    public String getUnknownCommandMessage() {
        return unknownCommandMessage;
    }

    public void setUnknownCommandMessage(String unknownCommandMessage) {
        this.unknownCommandMessage = unknownCommandMessage;
    }

    // Returns the no permission message //
    public String getNoPermissionMessage() {
        return noPermissionMessage;
    }

    public void setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = noPermissionMessage;
    }

    // Returns the plugin reload command message //
    public String getPluginReloadMessage() {
        return pluginReloadMessage;
    }

    public void setPluginReloadMessage(String pluginReloadMessage) {
        this.pluginReloadMessage = pluginReloadMessage;
    }

    public void setUsingPAPI(boolean usePAPI) {
        this.usePAPI = usePAPI;
    }

    public boolean usingPAPI() {
        return this.usePAPI;
    }
}
