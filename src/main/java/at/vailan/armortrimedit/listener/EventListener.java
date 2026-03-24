package at.vailan.armortrimedit.listener;

import at.vailan.armortrimedit.Permissions;
import at.vailan.armortrimedit.gui.ArmorTrimGUI;
import at.vailan.armortrimedit.gui.ArmorTrimGUIOpener;
import at.vailan.armortrimedit.manager.ArmorTrimController;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import static at.vailan.armortrimedit.ArmorTrimEdit.getInstance;

public class EventListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) {
            return;
        }
        String viewTitle = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (!getInstance().getGUITitlePlain().equals(viewTitle)) {
            return;
        }

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType().isAir() || !clicked.hasItemMeta()) return;

        Component comp = clicked.getItemMeta().displayName();
        String name = comp != null
                ? PlainTextComponentSerializer.plainText().serialize(comp)
                : "";

        ArmorTrimController controller = ArmorTrimController.get();

        switch (event.getSlot()) {

            case ArmorTrimGUI.APPLY_SLOT -> {
                if (!player.hasPermission(Permissions.APPLY)) {
                    player.sendMessage(getInstance().getMessage("no-permission"));
                    return;
                }
                controller.applyTrim(player);
                refreshGUI(player);
            }

            case ArmorTrimGUI.REMOVE_SLOT -> {
                if (!player.hasPermission(Permissions.REMOVE)) {
                    player.sendMessage(getInstance().getMessage("no-permission"));
                    return;
                }
                controller.removeTrim(player);
                refreshGUI(player);
            }

            case ArmorTrimGUI.CLOSE_SLOT -> player.closeInventory();

            default -> handlePatternOrMaterialClick(player, name);
        }
    }


    private void handlePatternOrMaterialClick(Player player, String name) {
        ArmorTrimController controller = ArmorTrimController.get();
        controller.select(player, name);
        refreshGUI(player);
    }

    private void refreshGUI(Player player) {
        at.vailan.armortrimedit.manager.SchedulerUtil.runDelayed(at.vailan.armortrimedit.ArmorTrimEdit.getInstance(), player, () -> ArmorTrimGUIOpener.open(player), 1L);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;

        String viewTitle = PlainTextComponentSerializer.plainText().serialize(event.getView().title());
        if (!getInstance().getGUITitlePlain().equals(viewTitle)) return;

        if (event.getReason() != InventoryCloseEvent.Reason.OPEN_NEW) {
            ArmorTrimController.get().onClose(player);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        ArmorTrimController.get().onClose(event.getPlayer());
    }

}