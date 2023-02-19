package ca.pandaaa.automaticbroadcast;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {

    // Attributes //
    private final FileConfiguration config;
    private final FileConfiguration messages;
    private final Settings settings;

    // Constructor //
    public ConfigManager(FileConfiguration config, FileConfiguration messages, Settings settings) {
        this.config = config;
        this.messages = messages;
        this.settings = settings;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public FileConfiguration getMessagesConfig() {
        return messages;
    }
}
