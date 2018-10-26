package cn.mgazul.pfworlds.Listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import cn.mgazul.pfworlds.Main;
import cn.mgazul.pfworlds.Commands.cmdWorld;
import cn.mgazul.pfworlds.utilities.Config;

public class ChatListener implements Listener{
	
    @EventHandler
    public void onPlayerChat(final AsyncPlayerChatEvent event) {
        Player p = event.getPlayer();
        if (cmdWorld.worldInfo.contains(p)) {
            event.setCancelled(true);
            Config.addInfo(ClickEvent.changeInfo, event.getMessage());
            p.sendMessage(Main.getInstance().getPrefix() + "已添加信息§b " + event.getMessage() + "§7 到配置文件.");
            cmdWorld.worldInfo.remove(p);
        }
    }
}
