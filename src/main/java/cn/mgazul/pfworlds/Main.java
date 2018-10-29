package cn.mgazul.pfworlds;

import cn.mgazul.pfworlds.Commands.cmdWorld;
import cn.mgazul.pfworlds.Listener.ClickEvent;
import cn.mgazul.pfworlds.utilities.Config;
import cn.mgazul.pfworlds.utilities.PFPapiHook;
import cn.mgazul.pfworlds.utilities.WorldTypes;
import org.bukkit.Bukkit;
import org.bukkit.WorldType;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

    public static Main instance;
    public static String prefix = "§6世界管理 §8》 §7";

    public String type;
    
    public void addWorldType() {
        WorldTypes.WorldTypes.add(WorldType.FLAT);
        WorldTypes.WorldTypes.add(WorldType.NORMAL);
        WorldTypes.WorldTypes.add(WorldType.LARGE_BIOMES);
    }
    
    public String getCommandName() {
        return this.type;
    }
    
    public void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
    
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
    
    public void onDisable() {
  	  PFPapiHook.unhook();
    }
    
    public void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new ClickEvent(), this);
        this.getCommand("world").setExecutor(new cmdWorld());
    }
    
    public void setCommandName(String type) {
        this.type = type;
    }
}
