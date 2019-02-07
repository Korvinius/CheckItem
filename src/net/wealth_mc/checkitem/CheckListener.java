package net.wealth_mc.checkitem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

public class CheckListener implements Listener {
	
	public CheckListener(CheckItem checkItem) {
	}


	@EventHandler
	public static void onArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		if (CheckItem.enchantcheck) if(CheckEnchant.checkArmorStandManipulate(event)) return;
		CheckItemsList.checkArmorStandManipulate(event);
	}
	
	@EventHandler
	public static void onDamageByEntity(EntityDamageByEntityEvent event) {
		if (CheckItem.enchantcheck) if (CheckEnchant.checkDamageByEntity(event)) ;
	}

	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent event) {
		if (CheckItem.enchantcheck) if (CheckEnchant.checkItemPickup(event)) ;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public static void onPlayerItemHeld(PlayerItemHeldEvent event) {
		if (CheckItem.enchantcheck) if (CheckEnchant.checkPlayerItemHeld(event)) ;
		
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerBlockPlace(BlockPlaceEvent event){	
		CheckItemsList.checkPlayerBlockPlace(event);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerDropItem(PlayerDropItemEvent event){
		CheckItemsList.checkPlayerDropItem(event);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onInteractEvent(PlayerInteractEvent event){
		CheckItemsList.checkArmorClick(event);
		if (CheckItem.enchantcheck) if (CheckEnchant.checkItemClick(event)) ;
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onFrameClick (PlayerInteractEntityEvent event) {
		CheckItemsList.checkFrameInteractEntity(event);
	}

	@EventHandler(priority = EventPriority.HIGH)
	public void onCheckInventoryClickEvent(InventoryClickEvent event) {
/*		CheckItem.instance.getLogger().info("1 " + event.getEventName());
		CheckItem.instance.getLogger().info("2 " + event.getAction());
		CheckItem.instance.getLogger().info("1 " + event.isRightClick());
		CheckItem.instance.getLogger().info("1 " + event.getClick());*/
		if (event.getInventory().getType() == InventoryType.ANVIL
				&& event.getAction() == InventoryAction.HOTBAR_SWAP) {
			event.setCancelled(true);
		}
		CheckItemsList.checkAnvilPickup(event);
		CheckItemsList.checkAnvilPlace(event);
		CheckItemsList.checkCloneStack(event);
		CheckItemsList.checkCloneStackR(event);
		CheckItemsList.checkInventoryClick(event);
		CheckItemsList.checkCreativeDrop(event);
	}
	
		
}