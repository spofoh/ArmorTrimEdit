package at.vailan.armortrimedit.manager;

import at.vailan.armortrimedit.data.MaterialItem;
import at.vailan.armortrimedit.data.PatternItem;
import at.vailan.armortrimedit.data.ArmorTrimData;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static at.vailan.armortrimedit.ArmorTrimEdit.getInstance;

public class ArmorTrimController {

    private final SurvivalModeManager survivalManager;
    private final Map<UUID, ArmorTrimData> trims = new HashMap<>();

    private static final ArmorTrimController INSTANCE =
            new ArmorTrimController(new SurvivalModeManager(getInstance()));

    private ArmorTrimController(SurvivalModeManager survivalManager) {
        this.survivalManager = survivalManager;
    }

    public static ArmorTrimController get() {
        return INSTANCE;
    }

    public ArmorTrimData get(Player player) {
        return trims.computeIfAbsent(player.getUniqueId(), u -> new ArmorTrimData());
    }

    public void reset(Player player) {
        remove(player);
    }

    public void remove(Player player) {
        trims.remove(player.getUniqueId());
    }

    public void applyTrim(Player player) {
        ArmorTrimData d = get(player);

        ItemStack item = player.getInventory().getItemInMainHand();

        if (!(item.getItemMeta() instanceof ArmorMeta meta)) {
            player.sendMessage(getInstance().getMessage("not-holding-armor"));

            return;
        }

        ArmorTrim existingTrim = meta.hasTrim() ? meta.getTrim() : null;

        TrimPattern p = d.getPattern() != null ? d.getPattern() : (existingTrim != null ? existingTrim.getPattern() : null);
        TrimMaterial m = d.getMaterial() != null ? d.getMaterial() : (existingTrim != null ? existingTrim.getMaterial() : null);

        if (m == null || p == null) { return; }

        PatternItem patternItem = PatternItem.fromTrimPattern(p);
        MaterialItem materialItem = MaterialItem.fromTrimMaterial(m);

        if (!survivalManager.canApply(player, patternItem, materialItem)) {
            player.sendMessage(getInstance().getMessage("missing-items"));
            return;
        }

        meta.setTrim(new ArmorTrim(m, p));
        item.setItemMeta(meta);

        survivalManager.consumeItems(player, patternItem, materialItem);

        reset(player);
    }

    public void removeTrim(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!(item.getItemMeta() instanceof ArmorMeta meta)) {
            player.sendMessage(getInstance().getMessage("not-holding-armor"));

            return;
        }

        ArmorTrim trim = meta.getTrim();

        if (trim == null) {
            player.sendMessage(getInstance().getMessage("no-armor-trim"));
            return;
        }

        MaterialItem materialItem = MaterialItem.fromTrimMaterial(trim.getMaterial());
        PatternItem patternItem = PatternItem.fromTrimPattern(trim.getPattern());

        if (materialItem != null && patternItem != null) {
            survivalManager.returnItems(player, patternItem, materialItem);
        }


        meta.setTrim(null);
        item.setItemMeta(meta);
    }

    public void select(Player player, String name) {
        ArmorTrimData d = get(player);

        String key = name.toLowerCase();

        PatternItem pattern = PatternItem.fromDisplay(key);
        if (pattern != null) {
            d.setPattern(pattern.trimPattern);

            return;
        }

        MaterialItem material = MaterialItem.fromDisplay(key);
        if (material != null) {
            d.setMaterial(material.trimMaterial);
        }

    }

    public void onClose(Player player) {
        remove(player);
    }

}