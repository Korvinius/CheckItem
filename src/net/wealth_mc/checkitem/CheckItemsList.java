package net.wealth_mc.checkitem;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class CheckItemsList {

	public static void checkArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
		if (!CheckItem.instance.items) return;
		Player player = event.getPlayer();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.items")) return;
		ItemStack hand = event.getPlayerItem();
		ItemStack stand = event.getArmorStandItem();
		GameMode gm = player.getGameMode();
		if (gm != GameMode.CREATIVE) return;
		
		if (hand.getType() != Material.AIR) {
			Artefact artefact = getArtefact(hand);
			if (CheckItem.instance.itemsm.contains(artefact)) {
				
				event.setCancelled(true);
				player.sendMessage(CheckItem.TAG + artefact.toString() 
						+ ChatColor.DARK_RED +  " нельзя одеть на стенд, с включенным творческим режимом!");
			}
			return;
		}
		if (stand.getType() != Material.AIR) {
			Artefact artefact = getArtefact(stand);
			if (CheckItem.instance.itemsm.contains(artefact)) {
				
				event.setCancelled(true);
				player.sendMessage(CheckItem.TAG + artefact.toString() 
						+ ChatColor.DARK_RED +  " нельзя снимать из стенда, с включенным творческим режимом!");
			}
		}
		
	}
	/**
	 * Удаление запрещенного дропа из инвентаря в ГМ 1
	 * @param event
	 */
	public static void checkCreativeDrop(InventoryClickEvent event) {
		if (!CheckItem.instance.items) return;
		Player player = (Player) event.getWhoClicked();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.items")) return;
		InventoryAction click = event.getAction();
	
		if (event.getCursor().getType() != Material.AIR && (event.getCurrentItem() == null
				|| click == InventoryAction.SWAP_WITH_CURSOR)) {
				GameMode gm = event.getWhoClicked().getGameMode();
			if (gm != GameMode.CREATIVE) return;
			Artefact artefact = getArtefact(event.getCursor());
			
			if (CheckItem.instance.itemsm.contains(artefact)) {
				event.setCancelled(true);
				event.setResult(Result.DENY);
				closeInventoryPl(player);
				player.sendMessage(CheckItem.TAG + artefact.toString() 
						+ ChatColor.DARK_RED +  " уничтожается при попытке выбросить с включенным творческим режимом!");
			}
		}
	}

	/**
	 * Проверка перемещения запрещенных вещей в инвентаре
	 * @param event
	 */
	public static void checkInventoryClick(InventoryClickEvent event) {

		Player player = (Player) event.getWhoClicked();
		ItemStack item1 = event.getCurrentItem();
		ItemStack item2 = event.getCursor();
		ItemStack item3 = new ItemStack(Material.AIR);
		if (item1 == null) return;
		if (item1 == item2) return;
//******* Проверка чар 1
		if (item1.getType() != Material.AIR) {
			if (CheckItem.enchantcheck &&
					(CheckEnchant.checkEnchantLevel(item1) || CheckEnchant.checkEnchantAmount(item1))) {
				if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.enchant")) return;
				event.setCancelled(true);
				event.setResult(Result.DENY);
				event.setCurrentItem(item3);
				closeInventoryPl(player);
				player.sendMessage(CheckItem.enchantmess);
				CheckItem.instance.getLogger().info(player.getName() + ": " + event.getEventName()
						+ ": " + item1);
				return;
			}
		}
//******* конец проверки чар 1

		if (item2.getType() == Material.AIR) return; //** общая проверка

//******* Проверка чар 2
		if (CheckItem.enchantcheck &&
				(CheckEnchant.checkEnchantLevel(item2) || CheckEnchant.checkEnchantAmount(item2))) {
			if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.enchant")) return;
			event.setCancelled(true);
			event.setResult(Result.DENY);
			
			closeInventoryPl(player);
			player.sendMessage(CheckItem.enchantmess);
			CheckItem.instance.getLogger().info(player.getName() + ": " + event.getEventName()
					+ ": " + item1);
			return;
		}
//******* конец проверки чар 2
		if (!CheckItem.instance.items) return;
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.items")) return;
//		if (event.getClick() != ClickType.CREATIVE) return;
		if (!event.getEventName().equals("InventoryCreativeEvent")) return;

		Artefact artefact = getArtefact(event.getCurrentItem());
		if (artefact != null) {
//			artefact = getArtefact(event.getCurrentItem());
			if (CheckItem.instance.itemsm.contains(artefact)) {
				player.sendMessage(CheckItem.TAG +  artefact.toString() + ChatColor.DARK_RED
						+ " нельзя перемещать в инвентаре с включенным творческим режимом!");
				event.setCancelled(true);
				closeInventoryPl(player);
				return;
			}
		}
		
		if (event.getCursor() != null && event.getCursor().getType() != Material.AIR) {
			artefact = getArtefact(event.getCursor());
			if (artefact == null) return;
			if (CheckItem.instance.itemsm.contains(artefact)) {
				player.sendMessage(CheckItem.TAG +  artefact.toString() + ChatColor.DARK_RED
						+ " уничтожается при попытке перемещать в инвентаре с включенным творческим режимом!");
				event.setCancelled(true);
				event.setResult(Result.DENY);
				closeInventoryPl(player);
			}
		}
		
/*		Artefact artefact = getArtefact(event.getCursor());
		if (CheckItem.instance.itemsm.contains(artefact)) {
			player.sendMessage(CheckItem.TAG +  artefact.toString() + ChatColor.DARK_RED
					+ " уничтожается при попытке перемещать в инвентаре с включенным творческим режимом!");
			event.setCancelled(true);
			event.setResult(Result.DENY);
			closeInventoryPl(player);
		}*/
	}

	/**
	 * Запрет клонирования запрещенных вещей в разных инвентарях при ГМ 1
	 * @param event
	 */
	public static void checkCloneStack(InventoryClickEvent event) {
		if (!CheckItem.instance.items) return;
		Player player = (Player) event.getWhoClicked();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.items")) return;
		InventoryAction action = event.getAction();
		if(action != InventoryAction.CLONE_STACK) return;
		
		Artefact artefact = getArtefact(event.getCurrentItem());
		if (CheckItem.instance.itemsm.contains(artefact)) {
			
			event.setCancelled(true);
			event.setResult(Result.DENY);
			closeInventoryPl(player);
			player.sendMessage(CheckItem.TAG + artefact.toString() 
					+ ChatColor.DARK_RED + " невозможно клонировать!");
		}
	}

	public static void checkCloneStackR(InventoryClickEvent event) {
		if (!CheckItem.instance.items) return;
		Player player = (Player) event.getWhoClicked();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.items")) return;
//		InventoryAction action = event.getAction();
		if (player.getGameMode() == GameMode.CREATIVE
				&& (event.isRightClick() || event.isLeftClick())) {
			
			Artefact artefact = getArtefact(event.getCurrentItem());
			Artefact artefact2 = getArtefact(event.getCursor());
			if (CheckItem.instance.itemsm.contains(artefact)
					||CheckItem.instance.itemsm.contains(artefact2)) {
				event.setCancelled(true);
				event.setResult(Result.DENY);
				closeInventoryPl(player);
				player.sendMessage(CheckItem.TAG + ChatColor.DARK_RED + " - запрещенное действие в креативе!");
			}
		}
	}
	
	/**
	 * Клики в наковальне place~
	 * @param event
	 */
	public static void checkAnvilPlace(InventoryClickEvent event) {
		if (!CheckItem.instance.anvil) return;
		Player player = (Player) event.getWhoClicked();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.anvil")) return;
		InventoryType inventory = event.getInventory().getType();
		if(inventory != InventoryType.ANVIL) return; 
		InventoryAction invact = event.getAction();

		if (	   invact == InventoryAction.PLACE_ALL
				|| invact == InventoryAction.PLACE_ONE
				|| invact == InventoryAction.PLACE_SOME
				|| invact == InventoryAction.SWAP_WITH_CURSOR) {

			Artefact artefact = getArtefact(event.getCursor());
			if (CheckItem.instance.anvilm.contains(artefact)) {
				event.setCancelled(true);
				closeInventoryPl(player);
				player.sendMessage(CheckItem.TAG + artefact.toString() 
						+ ChatColor.DARK_RED + " нельзя перемещать в инвентаре наковальни!");
			}
		}
	}

	/**
	 * Клики в наковальне pickup~
	 * @param event
	 */
	public static void checkAnvilPickup(InventoryClickEvent event) {
		if (!CheckItem.instance.anvil) return;
		Player player = (Player) event.getWhoClicked();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.anvil")) return;
		InventoryType inventory = event.getInventory().getType();
		if(inventory != InventoryType.ANVIL) return;
		InventoryAction invact = event.getAction();
		
		if (invact == InventoryAction.MOVE_TO_OTHER_INVENTORY
				|| invact == InventoryAction.PICKUP_ALL
				|| invact == InventoryAction.PICKUP_HALF
				|| invact == InventoryAction.PICKUP_ONE
				|| invact == InventoryAction.PICKUP_SOME
				|| invact == InventoryAction.SWAP_WITH_CURSOR) {
			
			Artefact artefact = getArtefact(event.getCurrentItem());
			if (CheckItem.instance.anvilm.contains(artefact)) {
				event.setCancelled(true);
				closeInventoryPl(player);
				player.sendMessage(CheckItem.TAG + artefact.toString()
						+ ChatColor.DARK_RED + " нельзя перемещать в инвентаре наковальни!");
			}
		}
	}

	/**
	 * Проверяет попытки поместить запрещенные вещи в рамку или на арм.стенд
	 * @param event
	 */
	public static void checkFrameInteractEntity (PlayerInteractEntityEvent event) {
		
		Player  player = event.getPlayer();
		EntityType ent = event.getRightClicked().getType();
		ItemStack itemhand = player.getInventory().getItemInHand(); 
		if (itemhand.getType() == Material.AIR) return;
		if (ent != EntityType.ITEM_FRAME) return;

		if (CheckItem.enchantcheck &&
				(CheckEnchant.checkEnchantLevel(itemhand) || CheckEnchant.checkEnchantAmount(itemhand))) {
			if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.enchant")) return;
			ItemStack itemair = new ItemStack(Material.AIR);
			event.setCancelled(true);
			event.getPlayer().getInventory().setItemInHand(itemair);
			closeInventoryPl(player);
			player.sendMessage(CheckItem.enchantmess);
			CheckItem.instance.getLogger().info(player.getName() + ": " + event.getEventName()
					+ ": " + itemhand.toString());
			return;
		}
		if(!CheckItem.instance.items) return;
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.items")) return;
		GameMode gm = event.getPlayer().getGameMode();
		if (gm != GameMode.CREATIVE) return;
		Artefact artefact = getArtefact(itemhand);
		if (CheckItem.instance.itemsm.contains(artefact)) {
			event.setCancelled(true);
			player.sendMessage(CheckItem.TAG + artefact.toString()
					+ ChatColor.DARK_RED + " невозможно клонировать!");
		}
	}

	/**
	 * Проверка вещей одеваемых с помощью ПКМ
	 * @param event
	 */
	public static void checkArmorClick(PlayerInteractEvent event){
		
		Player player = event.getPlayer();
		GameMode gm = event.getPlayer().getGameMode();
		Action click = event.getAction();
		ItemStack hand = player.getInventory().getItemInHand();
		if (hand == null) return;
		if (hand.getType() == Material.AIR) return;

		if (click == Action.RIGHT_CLICK_AIR || click == Action.RIGHT_CLICK_BLOCK) {
			//***********
			if (CheckItem.enchantcheck &&
					(CheckEnchant.checkEnchantLevel(hand) || CheckEnchant.checkEnchantAmount(hand))) {
				if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.enchant")) return;
				event.setUseItemInHand(Result.DENY);
				updateInventoryPl(player);
			}
			//***********
			if(!CheckItem.instance.armor) return;
			if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.armor")) return;
			if (gm != GameMode.CREATIVE) return;
			Artefact artefact = getArtefact(hand);
			if (CheckItem.instance.armorm.contains(artefact)) {
				event.setUseItemInHand(Result.DENY);
				updateInventoryPl(player);	
				player.sendMessage(CheckItem.TAG + artefact.toString() 
						+ ChatColor.DARK_RED + " невозможно одеть с включенным творческим режимом!");
			}
		}
	}

	/**
	 * Проверка блоков при строительстве в ГМ 1
	 * @param event
	 */
	public static  void checkPlayerBlockPlace(BlockPlaceEvent event){
		if (!CheckItem.instance.place) return;
		Player player = event.getPlayer();
		GameMode gm = event.getPlayer().getGameMode();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.blockplace")) return;
		if (gm != GameMode.CREATIVE) return;

		Artefact artefact = getArtefact(event.getItemInHand());
		if (CheckItem.instance.placem.contains(artefact)) {
			event.setCancelled(true);
			player.sendMessage(CheckItem.TAG + artefact.toString()
					+ ChatColor.DARK_RED + " нельзя ставить в творческом режиме!");
		}
	}

	/**
	 * Проверка дропа
	 * @param event
	 */
	public static void checkPlayerDropItem(PlayerDropItemEvent event){
		
		if (!CheckItem.instance.drops) return;
		Player player = event.getPlayer();
		if (CheckItem.permcheck) if (player.hasPermission("checkitem.bypass.drops")) return;

		Artefact artefact = getArtefact(event.getItemDrop().getItemStack());
		if (CheckItem.instance.dropsm.contains(artefact)) {
			event.setCancelled(true);
			updateInventoryPl(player);	
			player.sendMessage(CheckItem.TAG + artefact.toString()
					+ ChatColor.DARK_RED +  " невозможно выбросить!");
		}
	}

	/**
	 * Получить артефакт из Итема
	 * @param item
	 * @return
	 */
	public static Artefact getArtefact(ItemStack item) {
		String mat = null;
		String nam = null;
		String lor = null;
		if (item == null || item.getType() == Material.AIR) return null;
		mat = item.getType().toString();
		if (item.getItemMeta().hasDisplayName()) {
			nam = item.getItemMeta().getDisplayName().toString();
		}
		if (item.getItemMeta().hasLore()) {
			lor = item.getItemMeta().getLore().toString();
		}
		Artefact artefact = new Artefact(mat, nam, lor);
		return artefact;
	}

	/**
	 * Закрыть инвентарь через 1 тик 
	 * @param p
	 */
	public static void closeInventoryPl(final Player p) {
		new BukkitRunnable() {
            @Override
            public void run() {

                p.closeInventory();
            }
        }.runTaskLater(CheckItem.instance, 2);
	}
	/**
	 * Обновить инвентарь через 1 тик
	 * @param p
	 */
	public static void updateInventoryPl(final Player p) {
		new BukkitRunnable() {
            @Override
            public void run() {

                p.updateInventory();
            }
        }.runTaskLater(CheckItem.instance, 2);
	}

}
