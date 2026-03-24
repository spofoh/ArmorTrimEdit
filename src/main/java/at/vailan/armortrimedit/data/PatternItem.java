package at.vailan.armortrimedit.data;

import org.bukkit.Material;
import org.bukkit.inventory.meta.trim.TrimPattern;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum PatternItem {
    SENTRY("Sentry", Material.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.SENTRY),
    DUNE("Dune", Material.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.DUNE),
    WILD("Wild", Material.WILD_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.WILD),
    COAST("Coast", Material.COAST_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.COAST),
    VEX("Vex", Material.VEX_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.VEX),
    WARD("Ward", Material.WARD_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.WARD),
    SILENCE("Silence", Material.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.SILENCE),
    TIDE("Tide", Material.TIDE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.TIDE),
    SNOUT("Snout", Material.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.SNOUT),
    RIB("Rib", Material.RIB_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.RIB),
    EYE("Eye", Material.EYE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.EYE),
    SPIRE("Spire", Material.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.SPIRE),
    HOST("Host", Material.HOST_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.HOST),
    SHAPER("Shaper", Material.SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.SHAPER),
    RAISER("Raiser", Material.RAISER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.RAISER),
    WAYFINDER("Wayfinder", Material.WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.WAYFINDER),
    BOLT("Bolt", Material.BOLT_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.BOLT),
    FLOW("Flow", Material.FLOW_ARMOR_TRIM_SMITHING_TEMPLATE, TrimPattern.FLOW);

    public final String display;
    public final Material material;
    public final TrimPattern trimPattern;

    PatternItem(String display, Material material, TrimPattern trimPattern) {
        this.display = display;
        this.material = material;
        this.trimPattern = trimPattern;
    }

    private static final Map<String, PatternItem> DISPLAY_MAP =
            Arrays.stream(values())
                    .collect(Collectors.toMap(
                            pi -> pi.display.toLowerCase(),
                            pi -> pi
                    ));

    public static PatternItem fromDisplay(String display) { return DISPLAY_MAP.get(display.toLowerCase()); }

    public static PatternItem fromTrimPattern(TrimPattern trim) {
        for (PatternItem item : values()) {
            if (item.trimPattern.equals(trim)) return item;
        }
        return null;
    }

    public static boolean isPattern(String s) {
        return DISPLAY_MAP.containsKey(s);
    }


}