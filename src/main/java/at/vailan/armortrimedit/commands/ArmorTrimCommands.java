package at.vailan.armortrimedit.commands;

import at.vailan.armortrimedit.Permissions;
import at.vailan.armortrimedit.data.MaterialItem;
import at.vailan.armortrimedit.data.PatternItem;
import at.vailan.armortrimedit.manager.ArmorTrimController;
import at.vailan.armortrimedit.gui.ArmorTrimGUIOpener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import static at.vailan.armortrimedit.ArmorTrimEdit.getInstance;

public class ArmorTrimCommands {

    @Command({"armortrim", "ae", "armortrimedit"})
    @CommandPermission(Permissions.APPLY)
    public void onDefault(Player p) {
        ItemStack item = p.getInventory().getItemInMainHand();

        if (getInstance().getConfig().getBoolean("gui-enabled")) {
            if (isArmor(item)) {
                p.sendMessage(getInstance().getMessage("not-holding-armor"));
                return;
            }
            ArmorTrimGUIOpener.open(p);
        }
    }

    @Command({"armortrim set", "ae set", "armortrimedit set"})
    @CommandPermission(Permissions.APPLY)
    public void onSet(Player player, String patternName, String materialName) {
        if (!getInstance().getConfig().getBoolean("command-enabled")) return;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isArmor(item)) {
            player.sendMessage(getInstance().getMessage("not-holding-armor"));
            return;
        }

        if (!PatternItem.isPattern(patternName) || !MaterialItem.isMaterial(materialName)) {
            player.sendMessage(getInstance().getMessage("invalid-pattern-or-material"));
            return;
        }

        ArmorTrimController controller = ArmorTrimController.get();

        controller.select(player, patternName);
        controller.select(player, materialName);
        controller.applyTrim(player);
    }

    @Command({"armortrim reload", "ae reload", "armortrimedit reload"})
    @CommandPermission(Permissions.RELOAD)
    public void onReload(Player player) {
        getInstance().reloadPlugin();
        player.sendMessage(getInstance().getMessage("reloaded"));
    }

    @Command({"armortrim remove", "ae remove", "armortrimedit remove"})
    @CommandPermission(Permissions.REMOVE)
    public void onRemove(Player player) {
        if (!getInstance().getConfig().getBoolean("command-enabled")) return;
        ItemStack item = player.getInventory().getItemInMainHand();

        if (isArmor(item)) {
            player.sendMessage(getInstance().getMessage("not-holding-armor"));
            return;
        }

        ArmorTrimController controller = ArmorTrimController.get();
        controller.removeTrim(player);
    }

    public static boolean isArmor(ItemStack item) {
        if (item == null || item.getType() == Material.AIR) return true;

        Material m = item.getType();

        if (m == Material.TURTLE_HELMET) return true;

        if (m.name().endsWith("_HELMET")) return false;
        if (m.name().endsWith("_CHESTPLATE")) return false;
        if (m.name().endsWith("_LEGGINGS")) return false;
        return !m.name().endsWith("_BOOTS");
    }
}
