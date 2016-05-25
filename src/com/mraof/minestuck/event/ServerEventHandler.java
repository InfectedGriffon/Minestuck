package com.mraof.minestuck.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.entity.underling.EntityUnderling;
import com.mraof.minestuck.inventory.captchalouge.HashmapModus;
import com.mraof.minestuck.inventory.captchalouge.Modus;
import com.mraof.minestuck.network.skaianet.SburbHandler;
import com.mraof.minestuck.network.skaianet.SkaianetHandler;
import com.mraof.minestuck.util.Echeladder;
import com.mraof.minestuck.util.MinestuckPlayerData;
import com.mraof.minestuck.util.PostEntryTask;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ServerEventHandler
{
	
	public static final ServerEventHandler instance = new ServerEventHandler();
	
	public static long lastDay;
	
	public static List<PostEntryTask> tickTasks = new ArrayList<PostEntryTask>();
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if(event.phase == TickEvent.Phase.END)
		{
			
			if(!MinestuckConfig.hardMode && event.world.provider.getDimension() == 0)
			{
				long time = event.world.getWorldTime() / 24000L;
				if(time != lastDay)
				{
					lastDay = time;
					SkaianetHandler.resetGivenItems();
				}
			}
			
			Iterator<PostEntryTask> iter = tickTasks.iterator();
			while(iter.hasNext())
				if(iter.next().onTick())
					iter.remove();
		}
	}
	
	@SubscribeEvent(priority=EventPriority.LOWEST, receiveCanceled=false)
	public void onEntityDeath(LivingDeathEvent event)
	{
		if(event.getEntity() instanceof IMob && event.getSource().getEntity() instanceof EntityPlayerMP)
		{
			EntityPlayerMP player = (EntityPlayerMP) event.getSource().getEntity();
			int exp = 0;
			if(event.getEntity() instanceof EntityZombie || event.getEntity() instanceof EntitySkeleton)
				exp = 6;
			else if(event.getEntity() instanceof EntityCreeper || event.getEntity() instanceof EntitySpider || event.getEntity() instanceof EntitySilverfish)
				exp = 5;
			else if(event.getEntity() instanceof EntityEnderman || event.getEntity() instanceof EntityBlaze || event.getEntity() instanceof EntityWitch || event.getEntity() instanceof EntityGuardian)
				exp = 12;
			else if(event.getEntity() instanceof EntitySlime)
				exp = ((EntitySlime) event.getEntity()).getSlimeSize() - 1;
			
			if(exp > 0)
				Echeladder.increaseProgress(player, exp);
		}
		if(event.getEntity() instanceof EntityPlayerMP)
			SburbHandler.stopEntry((EntityPlayerMP) event.getEntity());
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=false)
	public void onEntityAttack(LivingHurtEvent event)
	{
		if(event.getSource().getEntity() != null)
			if(event.getEntityLiving() instanceof EntityUnderling && event.getSource().getEntity() instanceof EntityPlayerMP)
			{	//Increase damage to underling
				EntityPlayerMP player = (EntityPlayerMP) event.getSource().getEntity();
				double modifier = MinestuckPlayerData.getData(player).echeladder.getUnderlingDamageModifier();
				event.setAmount((float) (event.getAmount() * modifier));
				
			} else if(event.getEntityLiving() instanceof EntityPlayerMP && event.getSource().getEntity() instanceof EntityUnderling)
			{	//Decrease damamge to player
				EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();
				double modifier = MinestuckPlayerData.getData(player).echeladder.getUnderlingProtectionModifier();
				event.setAmount((float) (event.getAmount() * modifier));
			}
	}
	
	@SubscribeEvent
	public void playerChangedDimension(PlayerChangedDimensionEvent event)
	{
		if(!event.player.worldObj.isRemote)
			SburbHandler.stopEntry(event.player);
	}
	
	@SubscribeEvent(priority=EventPriority.LOW, receiveCanceled=false)
	public void onServerChat(ServerChatEvent event)
	{
		Modus modus = MinestuckPlayerData.getData(event.getPlayer()).modus;
		if(modus instanceof HashmapModus)
			((HashmapModus) modus).onChatMessage(event.getMessage());
	}
}