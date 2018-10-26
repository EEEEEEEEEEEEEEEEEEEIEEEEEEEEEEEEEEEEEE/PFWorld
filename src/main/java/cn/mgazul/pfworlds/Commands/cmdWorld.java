package cn.mgazul.pfworlds.Commands;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import cn.mgazul.pfworlds.Main;
import cn.mgazul.pfworlds.Listener.ClickEvent;
import cn.mgazul.pfworlds.utilities.Config;
import cn.mgazul.pfworlds.utilities.WorldTypes;
import cn.mgazul.pfworlds.utilities.itemAPI;

public class cmdWorld implements CommandExecutor{
	
    public static ArrayList<Player> worldInfo = new ArrayList<Player>();
    public static File f = new File("plugins/PFWorlds/worlds.yml");
    
    
    public static void openWorldGui(final Player p, final String name) {
        int groesse = 9;
        int pos = 0;
        while (Bukkit.getWorlds().size() > groesse) {
            groesse += 9;
        }
        final Inventory inv = Bukkit.createInventory(null, groesse, name);
        for (final World w : Bukkit.getWorlds()) {
            final ArrayList<String> infoLore = new ArrayList<String>();
            try {
                final FileConfiguration cfg = YamlConfiguration.loadConfiguration(cmdWorld.f);
                cfg.load(cmdWorld.f);
                if (cfg.getConfigurationSection("worlds.") != null && cmdWorld.f.exists()) {
                    String worldtype = "N/A";
                    String infos;
                    String name1;
                    if (cfg.get("worlds." + w.getName() + ".info") != null) {
                        infos = cfg.getString("worlds." + w.getName() + ".info");
                        worldtype = cfg.getString("worlds." + w.getName() + ".worldtype");
                        name1 = cfg.getString("worlds." + w.getName() + ".name");
                    } else {
                        infos = "§7-/-";
                        name1 = w.getName().toString();
                    }
                    infoLore.add("§b世界别名 §8》 §7" + name1);
                    infoLore.add("§b信息 §8》 §7" + infos);
                    infoLore.add("§b世界类型 §8》 §7" + worldtype);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            inv.setItem(pos, itemAPI.doItem(Material.MAP, 1, "§7》 §6" + w.getName().toString(), infoLore));
            ++pos;
            infoLore.clear();
        }
        inv.setItem(pos, itemAPI.doItem(Material.REDSTONE_BLOCK, 1, "§4§l关闭菜单", null));
        p.openInventory(inv);
    }
    
    public static void worldNotExists(final Player p) {
        p.sendMessage(Main.getInstance().getPrefix() + "这个世界不存在。 可以通过§6/world list§7看到现有的世界!");
    }
    
    public boolean onCommand(final CommandSender cs, final Command cmd, final String label, final String[] args) {
        if (cs instanceof Player) {
            final Player p = (Player)cs;
            if (p.isOp()) {
                if (args.length == 0) {
                    this.sendHelp(p);
                }
                else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("list")) {
                        openWorldGui(p, "§8》 §6世界");
                    }
                    else {
                        this.sendHelp(p);
                    }
                }
                else if (args.length == 2) {
                    if (args[0].equalsIgnoreCase("create")) {
                        if (Bukkit.getWorld(args[1]) == null) {
                            Main.getInstance().setCommandName(args[1]);
                            int i = -1;
                            final Inventory inv = Bukkit.createInventory(null, 27, "§7世界名§8: §c" + args[1]);
                            for (WorldType worldType : WorldTypes.WorldTypes) {
                                ++i;
                                inv.setItem(i, itemAPI.doItem(Material.MAP, 1, worldType.getName(), null));
                            }
                            p.openInventory(inv);
                        }
                        else {
                            this.worldAlreadyExists(p);
                        }
                    }
                    else if (args[0].equalsIgnoreCase("tp")) {
                        if (Bukkit.getWorld(args[1]) != null) {
                            p.teleport(Bukkit.getWorld(args[1]).getSpawnLocation());
                            p.sendMessage(Main.getInstance().getPrefix() + "已传送到世界§6 " + args[1] + " §7的出生点.");
                        } else {
                            worldNotExists(p);
                        }
                    }
                    else if (args[0].equalsIgnoreCase("delete")) {
                        if (!args[1].equalsIgnoreCase("world")) {
                            final World w = Bukkit.getWorld(args[1]);
                            if (w != null) {
                                for (final Player all : Bukkit.getOnlinePlayers()) {
                                    if (all.getWorld() == w) {
                                        all.teleport(Bukkit.getWorld("world").getSpawnLocation());
                                    }
                                }
                            }
                            try {
                                Bukkit.unloadWorld(w, true);
                                final File deleteWorld = w.getWorldFolder();
                                deleteWorld.delete();
                                p.sendMessage(Main.getInstance().getPrefix() + "世界被成功删除.");
                                Config.removeWorld(args[1]);
                            }
                            catch (Exception e2) {
                                p.sendMessage(Main.getInstance().getPrefix() + "§c世界无法删除.");
                            }
                        } else {
                            p.sendMessage(Main.getInstance().getPrefix() + "§c你无法删除这个世界!");
                        }
                    }
                    else if (args[0].equalsIgnoreCase("import")) {
                        try {
                            final World w = Bukkit.getWorld(args[1]);
                            p.teleport(w.getSpawnLocation());
                            p.sendMessage(Main.getInstance().getPrefix() + "这个世界已经存在。 已将你传送.");
                        }
                        catch (Exception e3) {
                            final File loadWorld = new File(args[1]);
                            if (loadWorld.exists()) {
                                p.sendMessage(Main.getInstance().getPrefix() + "加载世界中.");
                                final World w2 = Bukkit.createWorld(new WorldCreator(args[1]).type(WorldType.FLAT));
                                p.teleport(w2.getSpawnLocation());
                                Config.addWorld(w2.getName());
                                p.sendMessage(Main.getInstance().getPrefix() + "这个世界已被加载。已将你传送.");
                            }
                            else {
                                p.sendMessage(Main.getInstance().getPrefix() + "找不到你要加载的世界文件.");
                            }
                        }
                    }
                    else if (args[0].equalsIgnoreCase("unload")) {
                        if (Bukkit.getWorld(args[1]) != null) {
                            for (final Player all2 : Bukkit.getOnlinePlayers()) {
                                if (all2.getWorld() == Bukkit.getWorld(args[1])) {
                                    all2.teleport(Bukkit.getWorld("world").getSpawnLocation());
                                }
                            }
                            Bukkit.unloadWorld(Bukkit.getWorld(args[1]), true);
                            Config.removeWorld(args[1]);
                        }
                    }
                    else if (args[0].equalsIgnoreCase("addinfo")) {
                        final World w = Bukkit.getWorld(args[1]);
                        if (w != null) {
                            p.sendMessage(Main.getInstance().getPrefix() + "您现在正在编辑世界的信息 §b§l" + w.getName() + "§7.");
                            p.sendMessage(Main.getInstance().getPrefix() + "现在在聊天中写下地图信息.");
                            ClickEvent.changeInfo = args[1];
                            cmdWorld.worldInfo.add(p);
                        }
                        else {
                            worldNotExists(p);
                        }
                    }
                }
                else if (args.length == 3 && args[0].equalsIgnoreCase("create")) {
                    final String worldname = args[1];
                    if (Bukkit.getWorld(args[1]) == null) {
                        final WorldType worldType2 = WorldType.getByName(args[2]);
                        p.sendMessage(Main.getInstance().getPrefix() + "\u00dcberpr\u00fcfe WorldType§6 " + args[2]);
                        if (worldType2 != null) {
                            if (worldType2 != WorldType.CUSTOMIZED) {
                                p.sendMessage(Main.getInstance().getPrefix() + "§aWorldType正确.");
                                p.sendMessage(Main.getInstance().getPrefix() + "世界§c " + worldname + " §7已创建. §8(§7" + args[2] + "§8)");
                                final World welt = Bukkit.createWorld(new WorldCreator(worldname).type(WorldType.getByName(args[2])));
                                try {
                                    Config.addWorld(welt.getName());
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                    p.sendMessage("无法创建世界.");
                                }
                                p.teleport(Bukkit.getWorld(worldname).getSpawnLocation());
                            } else {
                                p.sendMessage(Main.getInstance().getPrefix() + "世界类型§6 " + worldType2.toString() + " §7目前不支持\u00fctzt.");
                            }
                        }
                        else {
                            p.sendMessage(Main.getInstance().getPrefix() + "§c无法找到此 WorldType.");
                        }
                    }
                    else {
                        this.worldAlreadyExists(p);
                    }
                }
            }
        }
        return false;
    }
    
    private void sendHelp(final Player p) {
        p.sendMessage(Main.getInstance().getPrefix() + "/world create <Name> [WorldType] 创建世界");
        p.sendMessage(Main.getInstance().getPrefix() + "/world delete <Name> 删除世界");
        p.sendMessage(Main.getInstance().getPrefix() + "/world tp <Name> 传送世界");
        p.sendMessage(Main.getInstance().getPrefix() + "/world import <Name> 加载世界");
        p.sendMessage(Main.getInstance().getPrefix() + "/world unload <Name> 卸载世界");
        p.sendMessage(Main.getInstance().getPrefix() + "/world addinfo <Name> 添加世界介绍");
        p.sendMessage(Main.getInstance().getPrefix() + "/world list  世界列表GUI");
        p.sendMessage(" ");
    }
    
    private void worldAlreadyExists(final Player p) {
        p.sendMessage(Main.getInstance().getPrefix() + "这个世界不存在。 可以通过§6/world list§7看到现有的世界!");
    }
}
