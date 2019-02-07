package net.wealth_mc.checkitem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

public class CheckEnchant {

	public static boolean checkArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		boolean result = false;
		Player player = event.getPlayer();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.enchant")) return result;
		ItemStack hand = event.getPlayerItem();
		ItemStack stand = event.getArmorStandItem();
		String logs = null;
		
		if (hand.getType() != Material.AIR) {
			if (checkEnchantLevel(hand) || checkEnchantAmount(hand)) {
				result = true;
				ItemStack itemair = new ItemStack(Material.AIR);
				event.setCancelled(result);
				player.getInventory().setItemInHand(itemair);
				player.sendMessage(CheckItem.enchantmess);
				logs = player.getName() + ": " + event.getEventName()
						+ ": " + hand.toString();
				CheckItem.instance.getLogger().info(logs);
				try {
					saveEnchantLevelLogs(logs);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				return result;
			}
		}
		if (stand.getType() != Material.AIR) {
			if (checkEnchantLevel(stand) || checkEnchantAmount(stand)) {
				result = true;
				event.setCancelled(result);
				player.sendMessage(CheckItem.enchantmess);
				logs = player.getName() + ": " + event.getEventName()
						+ ": " + stand.toString();
				CheckItem.instance.getLogger().info(logs);
				try {
					saveEnchantLevelLogs(logs);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		return result;
	}
/**
 * ChIListener.onDamageByEntity
 * @param event
 * @return
 */
	public static boolean checkDamageByEntity(EntityDamageByEntityEvent event) {
		boolean result = false;
		EntityDamageByEntityEvent damager = (EntityDamageByEntityEvent) event;
		if (!(damager.getDamager() instanceof Player)) return result;
		Player player = (Player) damager.getDamager();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.enchant")) return result;
		ItemStack hand = player.getInventory().getItemInHand();
		String logs = null;
		if (hand == null) return result;
		if (hand.getType() == Material.AIR) return result;
		if (checkEnchantLevel(hand) || checkEnchantAmount(hand)) {
			result = true;
			ItemStack itemair = new ItemStack(Material.AIR);
			event.setCancelled(result);
			player.getInventory().setItemInHand(itemair);
			player.sendMessage(CheckItem.enchantmess);
			logs = player.getName() + ": " + event.getEventName()
					+ ": " + hand.toString();
			CheckItem.instance.getLogger().info(logs);
			try {
				saveEnchantLevelLogs(logs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * ChIListener.onPlayerItemHeld
	 * @param event
	 * @return
	 */
	public static boolean checkPlayerItemHeld(PlayerItemHeldEvent event) {
		boolean result = false;
		int slot = 0;
		if (!CheckItem.enchantcheck) return result;
		Player player = event.getPlayer();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.enchant")) return result;
		slot = event.getNewSlot();
		ItemStack item = event.getPlayer().getInventory().getItem(slot);
		String logs = null;
		if (item == null) return result;
		if (item.getType() == Material.AIR) return result;
		if (checkEnchantLevel(item) || checkEnchantAmount(item)) {
			result = true;
			ItemStack itemair = new ItemStack(Material.AIR);
			player.getInventory().setItem(slot, itemair);
			CheckItemsList.updateInventoryPl(player);
			player.sendMessage(CheckItem.enchantmess);
			logs = event.getPlayer().getName() + ": " + event.getEventName()
					+ ": " + item;
			CheckItem.instance.getLogger().info(logs);
			try {
				saveEnchantLevelLogs(logs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * ChIListener.onInteractEvent
	 * @param event
	 * @return
	 */
	public static boolean checkItemClick(PlayerInteractEvent event) {
		boolean result = false;
		if (!CheckItem.enchantcheck) return result;
		Player player = event.getPlayer();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.enchant")) return result;
		ItemStack item = event.getItem();
		String logs = null;
		if (item == null) return result;
		if (checkEnchantLevel(item) || checkEnchantAmount(item)) {
			result = true;
			item = new ItemStack(Material.AIR);
			player.getInventory().setItemInHand(item);
			player.sendMessage(CheckItem.enchantmess);
			event.setCancelled(true);
			logs = event.getPlayer().getName() + ": " + event.getEventName()
					+ ": " + event.getItem();
			CheckItem.instance.getLogger().info(logs);
			try {
				saveEnchantLevelLogs(logs);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * ChIListener.onItemPickup
	 * @param event
	 * @return
	 */
	public static boolean checkItemPickup(PlayerPickupItemEvent event) {
		boolean result = false;
		if (!CheckItem.enchantcheck) return result;
		if (CheckItem.permcheck) if (event.getPlayer().hasPermission("checkitem.bypass.enchant")) return result;
		ItemStack item = event.getItem().getItemStack();
		if (item == null) return result;
		if (checkEnchantLevel(item) || checkEnchantAmount(item)) {
			result = true;
			event.setCancelled(true);
		}
		return result;
	}

	/**
	 * Проверяет количество зачарований
	 * @param item
	 * @return
	 */
	public static boolean checkEnchantAmount(ItemStack item) {
		int k = 0;
		for (Integer i : item.getEnchantments().values()) {
			k++;
			if (i != 0 && k > CheckItem.enchantamount) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Проверяет уровень зачарований
	 * @param item
	 * @return
	 */
	public static boolean checkEnchantLevel(ItemStack item) {
		for (Integer i : item.getEnchantments().values()) {
			if (i.intValue() > CheckItem.enchantlevel) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Запись в лог-файл
	 * @param log
	 * @throws FileNotFoundException
	 */
	protected synchronized static void saveEnchantLevelLogs(String log) throws FileNotFoundException {
		String data = java.util.Calendar.getInstance().getTime().toString();
		String logs = data + " - " + log;
		File dir = new File (CheckItem.instance.getDataFolder()+File.separator+"Log"+File.separator);
		if (!dir.exists()) dir.mkdirs();
        File f = new File (CheckItem.instance.getDataFolder()+File.separator+"Log"+File.separator+"levels.log");
        if(!f.exists()){
            try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        PrintStream printStream = new PrintStream(new FileOutputStream(f, true), true);
		printStream.println(logs);
		printStream.close();
        
	}
}
