package cn.mgazul.pfworlds.utilities;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Config
{
    static File f;
    
    static {
        Config.f = new File("plugins/PFWorlds/worlds.yml");
    }
    
    public static void addInfo(final String w, final String info) {
        final World world = Bukkit.getWorld(w);
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
        if (Config.f.exists()) {
            try {
                cfg.load(Config.f);
                if (cfg.getString("worlds." + world.getName()) != null) {
                    cfg.set("worlds." + world.getName() + ".info", info);
                }
                cfg.save(Config.f);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void addname(final String w, final String info) {
        final World world = Bukkit.getWorld(w);
        final FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
        if (Config.f.exists()) {
            try {
                cfg.load(Config.f);
                if (cfg.getString("worlds." + world.getName()) != null) {
                    cfg.set("worlds." + world.getName() + ".name", info);
                }
                cfg.save(Config.f);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public static void addWorld(final String w) {
        if (Bukkit.getWorld(w) != null) {
            final World world = Bukkit.getWorld(w);
            final FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
            if (Config.f.exists()) {
                try {
                    cfg.load(Config.f);
                    if (cfg.getString("worlds." + world.getName()) == null) {
                        cfg.set("worlds." + world.getName() + ".seed", world.getSeed());
                        cfg.set("worlds." + world.getName() + ".worldtype", world.getWorldType().name());
                        cfg.set("worlds." + world.getName() + ".environment", world.getEnvironment().name());
                        cfg.set("worlds." + world.getName() + ".name",world.getName().toString());
                        cfg.set("worlds." + world.getName() + ".info","-/-");
                    }
                    cfg.save(Config.f);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public static void createFile() {
        try {
            if (!Config.f.exists()) {
                final File file = new File("plugins/PFWorlds", "worlds.yml");
                final FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
                try {
                    cfg.save(file);
                }
                catch (IOException var4) {
                    var4.printStackTrace();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void loadWorlds() {
        try {
            final FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
            cfg.load(Config.f);
            if (cfg.getConfigurationSection("worlds.") != null) {
                for (final String w : cfg.getConfigurationSection("worlds.").getKeys(false)) {
                    if (Bukkit.getWorld(w) == null && Config.f.exists()) {
                        Long seed = -1L;
                        String environment = "-1";
                        String worldtype = "-1";
                        if (cfg.get("worlds." + w + ".seed") != null) {
                            seed = cfg.getLong("worlds." + w + ".seed");
                        }
                        else {
                            seed = -1L;
                        }
                        if (cfg.get("worlds." + w + ".environment") != null) {
                            environment = cfg.getString("worlds." + w + ".environment");
                        }
                        else {
                            environment = "-1";
                        }
                        if (cfg.get("worlds." + w + ".worldtype") != null) {
                            worldtype = cfg.getString("worlds." + w + ".worldtype");
                        }
                        else {
                            worldtype = "-1";
                        }
                        new WorldCreator(w).seed(seed).type(WorldType.valueOf(worldtype)).environment(World.Environment.valueOf(environment)).createWorld();
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void removeWorld(final String w) {
        if (Bukkit.getWorld(w) != null) {
            final World world = Bukkit.getWorld(w);
            final FileConfiguration cfg = YamlConfiguration.loadConfiguration(Config.f);
            if (Config.f.exists()) {
                try {
                    cfg.load(Config.f);
                    if (cfg.getString("worlds." + world.getName()) != null) {
                        cfg.set("worlds." + world.getName() + ".seed", null);
                        cfg.set("worlds." + world.getName() + ".worldtype", null);
                        cfg.set("worlds." + world.getName() + ".environment", null);
                        cfg.set("worlds." + world.getName(), null);
                    }
                    cfg.save(Config.f);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
