package net.wealth_mc.checkitem;

import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckCommand implements CommandExecutor {
	
	private final CheckItem plg;
	public CheckCommand(CheckItem plg) {
		this.plg = plg;
	}
	private String no = CheckItem.NO_NOT;
	private String artefact;
	private String hkey;
	


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("add")) {
				return execCmdAdd(sender, args);
			}else if (args[0].equalsIgnoreCase("check")) {
				return execCmdCheck(sender);
			}else if (args[0].equalsIgnoreCase("reload")) {
				return reloadConfig(sender);
			}else if (args[0].equalsIgnoreCase("help")) {
				return execCmdHelp(sender);
			}
		}
		return false;
	}
	private boolean reloadConfig(CommandSender sender) {
		if (sender instanceof Player) {
			if (sender.hasPermission("checkitem.admin")) {
				CheckItem.instance.reloadConfig();
				CheckItem.instance.loadConfs();
				CheckItem.instance.getItemsConfig();
				sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "файл конфигурации перезагружен.");
			}
			else {
				sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "У Вас нет прав на перезагрузку конфига!");
				plg.getLogger().info("[У игрока: " + sender.getName() + " нет прав на перезагрузку конфига");
			}
		}
		else {
			CheckItem.instance.reloadConfig();
			CheckItem.instance.loadConfs();
			CheckItem.instance.getItemsConfig();
			sender.sendMessage("[CheckItem]: " + "файл конфигурации перезагружен.");
		}
		return true;
	}
	private boolean execCmdHelp(CommandSender sender) {
		sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "/checkitem help - подсказка");
		sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "/checkitem reload - перезагрузка конфигурации");
		sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "/checkitem check - проверка предмета в руке, по спискам");
		sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "/checkitem add [arg] - добавить предмет, что в руке в базу [arg], [arg] = items, anvil, armor, drop, place");
		return true;
	}
	
	private boolean execCmdCheck(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player) sender;

			if (sender instanceof Player) {
				String m = null;
				String n = null;
				String l = null;

				if (player.getInventory().getItemInHand().getType() != Material.AIR) {
					m = player.getInventory().getItemInHand().getType().toString();
					if (player.getInventory().getItemInHand().getItemMeta().hasDisplayName()) {
						n = player.getInventory().getItemInHand().getItemMeta().getDisplayName();
						}
					if (player.getInventory().getItemInHand().getItemMeta().hasLore()) {
						l = player.getInventory().getItemInHand().getItemMeta().getLore().toString();
						}
					
					
					Artefact art = new Artefact(m, n, l);
					if (checkArtefact(plg.itemsm, art, player, "$items"));
					if (checkArtefact(plg.anvilm, art, player, "$anvil"));
					if (checkArtefact(plg.armorm, art, player, "$armor"));
					if (checkArtefact(plg.dropsm, art, player, "$drops"));
					if (checkArtefact(plg.ddeadm, art, player, "$ddead"));
					if (checkArtefact(plg.placem, art, player, "$place"));
					return true;
					}else {
						player.sendMessage(CheckItem.TAG + ChatColor.GOLD + "У вас в руке ничего нет");
						return true;
					}
			}else {
				sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "- эта команда доступна только игрокам.");
				return true;
			}
		}
		return false;
	}
	private boolean execCmdAdd(CommandSender sender, String[] args) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if (args.length == 1) {
				sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "Не верный аргумент [args1]: /checkitem add [args1], тип не может быть пустым");
				return true;
				}
			if (player.hasPermission("checkitem.admin")) {
				String m = no;
				String n = no;
				String l = no;
				String b = null;
				int h = 0;
				if (player.getInventory().getItemInHand().getType() != Material.AIR) {
					m = player.getInventory().getItemInHand().getType().toString();
					if (player.getInventory().getItemInHand().getItemMeta().hasDisplayName()) {
						n = player.getInventory().getItemInHand().getItemMeta().getDisplayName();
						}
					if (player.getInventory().getItemInHand().getItemMeta().hasLore()) {
						l = player.getInventory().getItemInHand().getItemMeta().getLore().toString();
						}

					artefact = m + n + l;
					h = artefact.hashCode();
					hkey = "$" + h;

					if (args[1].equalsIgnoreCase("items")) {
						b = "$items";
						addArtefact(b, hkey, m, n, l, player);
						return true;
					}else if (args[1].equalsIgnoreCase("anvil")) {
						b = "$anvil";
						addArtefact(b, hkey, m, n, l, player);
						return true;
					}else if (args[1].equalsIgnoreCase("armor")) {
						b = "$armor";
						addArtefact(b, hkey, m, n, l, player);
						return true;
					}else if (args[1].equalsIgnoreCase("drop")) {
						b = "$drops";
						addArtefact(b, hkey, m, n, l, player);
						return true;
					}else if (args[1].equalsIgnoreCase("dead")) {
						b = "$ddead";
						addArtefact(b, hkey, m, n, l, player);
						return true;
					}else if (args[1].equalsIgnoreCase("place")) {
						b = "$place";
						addArtefact(b, hkey, m, n, l, player);
						return true;
					}else {
						sender.sendMessage(CheckItem.TAG + ChatColor.GOLD + "Не верный аргумент [args1]: /checkitem [args] [args1], допустимо: items, anvil, armor, drop, dead");
						return true;
					}
				}else {
					player.sendMessage(CheckItem.TAG + ChatColor.GOLD + "У вас в руке ничего нет");
					return true;
				}
			}
			player.sendMessage(CheckItem.TAG + ChatColor.GOLD + "У Вас нет прав на добавление предметов в конфиг!");
			return true;
		}
		return false;
	}
	private boolean checkArtefact(Set<Artefact> s, Artefact a, Player p, String b) {
		if (s.contains(a)) {
			p.sendMessage(CheckItem.TAG	+ a.toString() + ChatColor.GOLD + " присутствует в списках: " + b);
			return true;
		}
		p.sendMessage(CheckItem.TAG	+ a.toString() + ChatColor.GOLD + " нет в списках: " + b);
	return false;
	}
	
	private void addArtefact(String b, String k, String m, String n, String l, Player p) {
		CheckItem.instance.getConfig().set(b + "." + k + ".m", m);
		CheckItem.instance.getConfig().set(b + "." + k + ".n", n);
		CheckItem.instance.getConfig().set(b + "." + k + ".l", l);
		CheckItem.instance.saveConfig();
		CheckItem.instance.reloadConfig();
		CheckItem.instance.getItemsConfig();
		if (n.equals(no)) n = null;
		if (l.equals(no)) l = null;
		Artefact art = new Artefact(m, n, l);
		p.sendMessage(CheckItem.TAG 
		+ ChatColor.WHITE + art.toString() + ChatColor.GOLD + " в блок: " + b);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((artefact == null) ? 0 : artefact.hashCode());
		result = prime * result + ((hkey == null) ? 0 : hkey.hashCode());
		return result;
	}
}
