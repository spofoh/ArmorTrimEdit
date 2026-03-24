package at.vailan.armortrimedit.data;

import org.bukkit.Material;
import org.bukkit.inventory.meta.trim.TrimMaterial;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum MaterialItem {
    IRON("Iron", Material.IRON_INGOT, TrimMaterial.IRON),
    GOLD("Gold", Material.GOLD_INGOT, TrimMaterial.GOLD),
    COPPER("Copper", Material.COPPER_INGOT, TrimMaterial.COPPER),
    NETHERITE("Netherite", Material.NETHERITE_INGOT, TrimMaterial.NETHERITE),
    DIAMOND("Diamond", Material.DIAMOND, TrimMaterial.DIAMOND),
    EMERALD("Emerald", Material.EMERALD, TrimMaterial.EMERALD),
    REDSTONE("Redstone", Material.REDSTONE, TrimMaterial.REDSTONE),
    LAPIS("Lapis", Material.LAPIS_LAZULI, TrimMaterial.LAPIS),
    AMETHYST("Amethyst", Material.AMETHYST_SHARD, TrimMaterial.AMETHYST),
    QUARTZ("Quartz", Material.QUARTZ, TrimMaterial.QUARTZ),
    RESIN("Resin", Material.RESIN_CLUMP, TrimMaterial.RESIN);

    public final String display;
    public final Material material;
    public final TrimMaterial trimMaterial;

    MaterialItem(String display, Material material, TrimMaterial trimMaterial) {
        this.display = display;
        this.material = material;
        this.trimMaterial = trimMaterial;
    }

    private static final Map<String, MaterialItem> DISPLAY_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            pi -> pi.display.toLowerCase(),
                            pi -> pi
                    ));

    public static MaterialItem fromDisplay(String display) {
        return DISPLAY_MAP.get(display.toLowerCase());
    }

    public static MaterialItem fromTrimMaterial(TrimMaterial trim) {
        for (MaterialItem item : values()) {
            if (item.trimMaterial.equals(trim)) return item;
        }
        return null;
    }

    public static boolean isMaterial(String s) {
        return DISPLAY_MAP.containsKey(s);
    }

}