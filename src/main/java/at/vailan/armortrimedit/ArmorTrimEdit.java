package at.vailan.armortrimedit;

import revxrsal.commands.bukkit.BukkitLamp;
import at.vailan.armortrimedit.listener.EventListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ArmorTrimEdit extends JavaPlugin {

    private static ArmorTrimEdit plugin;
    private final Map<String, Component> messageCache = new HashMap<>();
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private String plainGuiTitle = "";

    public static ArmorTrimEdit getInstance() { return plugin; }

    public void reloadPlugin() {
        this.reloadConfig();
        messageCache.clear();
        plainGuiTitle = PlainTextComponentSerializer.plainText().serialize(
                miniMessage.deserialize(getConfig().getString("gui.title", "<dark_gray>ArmorTrimEdit</dark_gray>"))
        );
    }

    @Override
    public void onEnable() {
        plugin = this;
        this.saveDefaultConfig();
        reloadPlugin();
        Bukkit.getPluginManager().registerEvents(new EventListener(), this);

        new bStats(this);

        BukkitLamp.builder(this).build().register(new at.vailan.armortrimedit.commands.ArmorTrimCommands());

        getLogger().info("Plugin enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin disabled.");
    }

    public Component getPrefix() {
        return messageCache.computeIfAbsent("prefix", k -> miniMessage.deserialize(getConfig().getString("prefix", "<dark_gray>[<aqua>ArmorTrimEdit</aqua>]</dark_gray> ")));
    }

    public Component getMessage(String key) {
        return messageCache.computeIfAbsent("msg_" + key, k -> {
            String msg = getConfig().getString("messages." + key);
            return getPrefix().append(miniMessage.deserialize(Objects.requireNonNullElseGet(msg, () -> "<red>Missing message: " + key)));
        });
    }

    public Component getGUITitle() {
        return messageCache.computeIfAbsent("gui_title", k -> miniMessage.deserialize(getConfig().getString("gui.title", "<dark_gray>ArmorTrimEdit</dark_gray>")));
    }

    public String getGUITitlePlain() {
        return plainGuiTitle;
    }

}