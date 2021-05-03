/*    */ package de.warsteiner.ultimatejobs.utils.api.other;
/*    */ 
 
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
/*    */ import org.bukkit.entity.Player;

import net.minecraft.server.v1_16_R3.IChatBaseComponent;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutTitle;
 
         public class ActionBar
         {
        	 public static void sendActionbar(Player player, String actionbar) {
        		 CraftPlayer p = (CraftPlayer)player;
        		 PacketPlayOutTitle sendactionbar = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.ACTIONBAR, 
        				 (IChatBaseComponent)IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + actionbar + "\"}"));
        		 (p.getHandle()).playerConnection.sendPacket((Packet)sendactionbar);
        	 }
         }


