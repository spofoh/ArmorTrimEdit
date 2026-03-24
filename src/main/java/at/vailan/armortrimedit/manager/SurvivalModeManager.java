package at.vailan.armortrimedit.manager;

import at.vailan.armortrimedit.ArmorTrimEdit;
import at.vailan.armortrimedit.data.MaterialItem;
import at.vailan.armortrimedit.data.PatternItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class SurvivalModeManager {

    private final boolean enabled;

    public SurvivalModeManager(ArmorTrimEdit plugin) {
        this.enabled = plugin.getConfig().getBoolean("survival-mode-enabled");
    }

    public boolean canApply(Player player, PatternItem patternItem, MaterialItem materialItem) {
        if (!enabled) return true;

        ItemStack pattern = ItemStack.of(patternItem.material, 1);
        ItemStack material = ItemStack.of(materialItem.material, 1);

        return player.getInventory().containsAtLeast(pattern, 1)
                && player.getInventory().containsAtLeast(material, 1);
    }

    public void consumeItems(Player player, PatternItem patternItem, MaterialItem materialItem) {
        if (!enabled) return;

        ItemStack pattern = ItemStack.of(patternItem.material, 1);
        ItemStack material = ItemStack.of(materialItem.material, 1);

        player.getInventory().removeItem(pattern);
        player.getInventory().removeItem(material);
    }

    public void returnItems(Player player, PatternItem patternItem, MaterialItem materialItem) {
        if (!enabled) return;

        ItemStack pattern = ItemStack.of(patternItem.material, 1);
        ItemStack material = ItemStack.of(materialItem.material, 1);

        Map<Integer, ItemStack> leftover = player.getInventory().addItem(pattern, material);

        leftover.values().forEach(item -> player.getWorld().dropItemNaturally(player.getLocation(), item));
    }

}
