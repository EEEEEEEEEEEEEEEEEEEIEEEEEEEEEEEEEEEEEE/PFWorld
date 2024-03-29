package cn.mgazul.pfworlds;

import cn.mgazul.pfworlds.Commands.cmdWorld;
import cn.mgazul.pfworlds.Listener.ClickEvent;
import cn.mgazul.pfworlds.utilities.Config;
import cn.mgazul.pfworlds.utilities.PFPapiHook;
import org.bukkit.Bukkit;
import org.bukkit.WorldType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;

public class Main extends JavaPlugin{

    public static Main instance;
    public static String prefix = "§6世界管理 §8》 §7";
    public static ArrayList<WorldType> WorldTypes = new ArrayList<WorldType>();

    public String type;
    
    public void addWorldType() {
        WorldTypes.add(WorldType.FLAT);
        WorldTypes.add(WorldType.NORMAL);
        WorldTypes.add(WorldType.AMPLIFIED);
        WorldTypes.add(WorldType.LARGE_BIOMES);
    }
    
    public String getCommandName() {
        return this.type;
    }
    
    public void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }

    @Override
    public void onEnable() {
        this.registerEvents();
        loadConfig();
        this.addWorldType();
        Main.instance = this;
        Config.createFile();
        Config.loadWorlds();
        Config.addWorld("world");
        Config.addWorld("world_the_end");
        Config.addWorld("world_nether");
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
        	PFPapiHook.hook();
        	Bukkit.getConsoleSender().sendMessage("§7[§6"+getDescription().getName()+"§7] §a变量系统已关联PlaceholderAPI.");
        } 
    }
    
    @Override
    public void onDisable() {
        if(Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            PFPapiHook.unhook();
        }
    }
    
    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ClickEvent(), this);
        this.getCommand("world").setExecutor(new cmdWorld());
    }
    
    public void setCommandName(String type) {
        this.type = type;
    }

    public static void deleteDir(File path) {
        if (path.exists()) {
            File[] allContents = path.listFiles();
            if (allContents != null) {
                File[] array;
                for (int length = (array = allContents).length, i = 0; i < length; ++i) {
                    File file = array[i];
                    deleteDir(file);
                }
            }
            path.delete();
        }
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        }
        catch (NumberFormatException e) {
            return false;
        }
    }
}
