package at.vailan.armortrimedit.gui;

import at.vailan.armortrimedit.data.MaterialItem;
import at.vailan.armortrimedit.data.PatternItem;
import at.vailan.armortrimedit.manager.ArmorTrimController;
import at.vailan.armortrimedit.data.ArmorTrimData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.inventory.meta.trim.TrimMaterial;
import org.bukkit.inventory.meta.trim.TrimPattern;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ArmorMeta;
import org.bukkit.inventory.meta.ItemMeta;

import static at.vailan.armortrimedit.ArmorTrimEdit.getInstance;

public class ArmorTrimGUI {

    public static final int APPLY_SLOT = 4;
    public static final int DISPLAY_SLOT = 22;
    public static final int REMOVE_SLOT = 40;
    public static final int CLOSE_SLOT = 49;
    public static final int[] PATTERN_SLOTS = {0, 1, 2, 9, 10, 11, 18, 19, 20, 27, 28, 29, 36, 37, 38, 45, 46, 47};
    public static final int[] MATERIAL_SLOTS = {6, 7, 15, 16, 24, 25, 33, 34, 42, 43, 51};
    public static final int[] EMPTY_SLOTS = {3, 5, 8, 12, 13, 14, 17, 21, 23, 26, 30, 31, 32, 35, 39, 41, 44, 48, 50, 52, 53};

    private final Player player;
    private final Inventory inventory;

    public ArmorTrimGUI(Player p) {
        this.player = p;
        this.inventory = Bukkit.createInventory(player, 54, getInstance().getGUITitle());
        buildInventory(player);
    }

    private void buildInventory(Player player) {

        inventory.setItem(APPLY_SLOT, createApplyButton());
        inventory.setItem(DISPLAY_SLOT, createDisplayItem(player));
        inventory.setItem(REMOVE_SLOT, createRemoveButton());
        inventory.setItem(CLOSE_SLOT, createCloseButton());

        fillPatternSlots(inventory);
        fillMaterialSlots(inventory);
        fillEmptySlots(inventory);

    }

    public Inventory getInventory() { return inventory; }

    private ItemStack createApplyButton() {
        return newItem("<green>Apply Armor Trim</green>", Material.LIME_CONCRETE);
    }

    private ItemStack createRemoveButton() { return newItem("<red>Remove Armor Trim</red>", Material.BARRIER); }

    private ItemStack createCloseButton() { return newItem("<dark_red>Close Menu</dark_red>", Material.RED_CONCRETE); }

    private ItemStack createDisplayItem(Player player) {
        ItemStack item = player.getInventory().getItemInMainHand();

        if (!(item.getItemMeta() instanceof ArmorMeta meta)) {
            player.sendMessage(getInstance().getMessage("not-holding-armor"));

            return newItem("<red>Invalid Item</red>", Material.BARRIER);
        }

        ArmorTrimData data = ArmorTrimController.get().get(player);
        TrimPattern p = data.getPattern();
        TrimMaterial m = data.getMaterial();

        ItemStack displayItem = new ItemStack(item.getType(), item.getAmount());
        ArmorMeta armorMeta = meta.clone();
        ArmorTrim existingTrim = meta.hasTrim() ? meta.getTrim() : null;

        TrimPattern finalPattern = p != null ? p : (existingTrim != null ? existingTrim.getPattern() : null);
        TrimMaterial finalMaterial = m != null ? m : (existingTrim != null ? existingTrim.getMaterial() : null);

        if (finalPattern != null && finalMaterial != null) {
            armorMeta.setTrim(new ArmorTrim(finalMaterial, finalPattern));
        } else {
            armorMeta.setTrim(null);
        }
        
        displayItem.setItemMeta(armorMeta);

        return displayItem;
    }

    private void fillPatternSlots(Inventory inv) {
        PatternItem[] items = PatternItem.values();
        ArmorTrimData data = ArmorTrimController.get().get(player);
        for (int i = 0; i < ArmorTrimGUI.PATTERN_SLOTS.length; i++) {
            int slot = ArmorTrimGUI.PATTERN_SLOTS[i];
            PatternItem item = items[i];

            ItemStack stack = newItem(item.display, item.material);

            if (item.trimPattern.equals(data.getPattern())) {
                stack = applySelectedVisuals(stack);
            }

            inv.setItem(slot, stack);
        }
    }

    private void fillMaterialSlots(Inventory inv) {
        MaterialItem[] items = MaterialItem.values();
        ArmorTrimData data = ArmorTrimController.get().get(player);
        for (int i = 0; i < ArmorTrimGUI.MATERIAL_SLOTS.length; i++) {
            int slot = ArmorTrimGUI.MATERIAL_SLOTS[i];
            MaterialItem item = items[i];

            ItemStack stack = newItem(item.display, item.material);

            if (item.trimMaterial.equals(data.getMaterial())) {
                stack = applySelectedVisuals(stack);
            }

            inv.setItem(slot, stack);
        }
    }
    
    private void fillEmptySlots(Inventory inv) {
        for (int slot : ArmorTrimGUI.EMPTY_SLOTS) {
            inv.setItem(slot, newItem("", Material.GRAY_STAINED_GLASS_PANE));
        }
    }

    protected static ItemStack newItem(String name, Material material) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize("<!italic>" + name));
        item.setItemMeta(meta);

        return item;
    }

    protected static ItemStack applySelectedVisuals(ItemStack item) {
        ItemStack clone = item.clone();
        
        if (getInstance().getConfig().getBoolean("gui.selected-glint", true)) {
            ItemMeta meta = clone.getItemMeta();
            if (meta != null) {
                meta.addEnchant(Enchantment.UNBREAKING, 1, true);
                meta.addItemFlags(org.bukkit.inventory.ItemFlag.HIDE_ENCHANTS);
                clone.setItemMeta(meta);
            }
        }
        
        String loreConfig = getInstance().getConfig().getString("gui.selected-lore", "");
        if (!loreConfig.isEmpty()) {
            ItemMeta meta = clone.getItemMeta();
            if (meta != null) {
                java.util.List<Component> lore = meta.hasLore() ? meta.lore() : new java.util.ArrayList<>();
                assert lore != null;
                lore.add(net.kyori.adventure.text.minimessage.MiniMessage.miniMessage().deserialize(loreConfig).decoration(TextDecoration.ITALIC, false));
                meta.lore(lore);
                clone.setItemMeta(meta);
            }
        }
        
        return clone;
    }

}