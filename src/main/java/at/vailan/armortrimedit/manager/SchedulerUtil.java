package at.vailan.armortrimedit.manager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class SchedulerUtil {

    private static Method getSchedulerMethod;
    private static Method runDelayedMethod;
    private static boolean initialized = false;
    private static boolean isFoliaSupported = false;

    private static void init() {
        if (initialized) return;
        try {
            getSchedulerMethod = Entity.class.getMethod("getScheduler");

            Class<?> schedulerClass = Class.forName("io.papermc.paper.threadedregions.scheduler.EntityScheduler");
            runDelayedMethod = schedulerClass.getMethod("runDelayed", Plugin.class, Consumer.class, Runnable.class, long.class);
            isFoliaSupported = true;
        } catch (Exception e) {
            isFoliaSupported = false;
        }
        initialized = true;
    }

    public static void runDelayed(Plugin plugin, Player player, Runnable runnable, long delayTicks) {
        init();
        if (isFoliaSupported) {
            try {
                Object scheduler = getSchedulerMethod.invoke(player);
                Consumer<Object> taskConsumer = task -> runnable.run();
                runDelayedMethod.invoke(scheduler, plugin, taskConsumer, null, delayTicks);
                return;
            } catch (Exception ignored) {
                // Fallback below if reflection invocation randomly drops
            }
        }

        Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
    }
}
