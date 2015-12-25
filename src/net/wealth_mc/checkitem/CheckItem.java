package net.wealth_mc.checkitem;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class CheckItem extends JavaPlugin {
	
	public static CheckItem instance;	
	private CheckCommand checkitem;

	public FileConfiguration config;
	
	public static final String NO_NOT = "not";
	public static final String  TAG = ChatColor.DARK_RED + "[" + ChatColor.GOLD + "CheckItem" 
	+ ChatColor.DARK_RED + "] " + ChatColor.RESET;
	
	public boolean debug = true;
	public boolean items = true;
	public boolean anvil = true;
	public boolean ddead = true;
	public boolean drops = true;
	public boolean armor = true;
	public boolean place = true;
	
	public Set<Artefact> itemsm = new HashSet<Artefact>();
	public Set<Artefact> anvilm = new HashSet<Artefact>();
	public Set<Artefact> ddeadm = new HashSet<Artefact>();
	public Set<Artefact> dropsm = new HashSet<Artefact>();
	public Set<Artefact> armorm = new HashSet<Artefact>();
	public Set<Artefact> placem = new HashSet<Artefact>();
	
	protected static int enchantlevel;
	protected static int enchantamount;
	protected static String enchantmess;
	protected static boolean enchantcheck;
	protected static boolean permcheck;

	@Override
	public void onEnable() {
		instance = this;
		
		this.getConfig().options().copyDefaults(true).copyHeader(true);
		this.saveDefaultConfig();
		config = this.getConfig();
		debug = config.getBoolean("debug");
		items = config.getBoolean("items");
		anvil = config.getBoolean("anvil");
		ddead = config.getBoolean("ddead");
		drops = config.getBoolean("drops");
		armor = config.getBoolean("armor");
		place = config.getBoolean("place");
		enchantlevel = config.getInt("enchant.level");
		enchantamount = config.getInt("enchant.amount");
		enchantmess = TAG + ChatColor.DARK_RED + config.getString("enchant.message");
		enchantcheck = config.getBoolean("enchant.check");
		permcheck = config.getBoolean("permission");
		getItemsConfig();
		
		getServer().getPluginManager().registerEvents(new CheckListener(this), this);
		checkitem = new CheckCommand(this);
		getCommand("checkitem").setExecutor(checkitem);
	} 

	public void loadConfs() {
		this.reloadConfig();
		config = this.getConfig();
		debug = config.getBoolean("debug");
		items = config.getBoolean("items");
		anvil = config.getBoolean("anvil");
		ddead = config.getBoolean("ddead");
		drops = config.getBoolean("drops");
		armor = config.getBoolean("armor");
		place = config.getBoolean("place");
		enchantlevel = config.getInt("enchant.level");
		enchantamount = config.getInt("enchant.amount");
		enchantmess = TAG + ChatColor.DARK_RED + config.getString("enchant.message");
		enchantcheck = config.getBoolean("enchant.check");
		permcheck = config.getBoolean("permission");
	}

	public void getItemsConfig() {
	
		itemsm.clear();
		if (this.getConfig().getConfigurationSection("$items") != null) {
			for (String key : this.getConfig().getConfigurationSection("$items").getKeys(false)) {
				String m = this.getConfig().getString("$items." + key + ".m");
				String n = this.getConfig().getString("$items." + key + ".n");
				String l = this.getConfig().getString("$items." + key + ".l");
				if (n.equals(NO_NOT)) n = null;
				if (l.equals(NO_NOT)) l = null;
				Artefact artefact = new Artefact(m, n, l);
				itemsm.add(artefact);
			}
		}
		anvilm.clear();
		if (this.getConfig().getConfigurationSection("$anvil") != null) {
			for (String key : this.getConfig().getConfigurationSection("$anvil").getKeys(false)) {
				String m = this.getConfig().getString("$anvil." + key + ".m");
				String n = this.getConfig().getString("$anvil." + key + ".n");
				String l = this.getConfig().getString("$anvil." + key + ".l");
				if (n.equals(NO_NOT)) n = null;
				if (l.equals(NO_NOT)) l = null;
				Artefact artefact = new Artefact(m, n, l);
				anvilm.add(artefact);
			}
		}
		armorm.clear();
		if (this.getConfig().getConfigurationSection("$armor") != null) {
			for (String key : this.getConfig().getConfigurationSection("$armor").getKeys(false)) {
				String m = this.getConfig().getString("$armor." + key + ".m");
				String n = this.getConfig().getString("$armor." + key + ".n");
				String l = this.getConfig().getString("$armor." + key + ".l");
				if (n.equals(NO_NOT)) n = null;
				if (l.equals(NO_NOT)) l = null;
				Artefact artefact = new Artefact(m, n, l);
				armorm.add(artefact);
			}
		}
		dropsm.clear();
		if (this.getConfig().getConfigurationSection("$drops") != null) {
			for (String key : this.getConfig().getConfigurationSection("$drops").getKeys(false)) {
				String m = this.getConfig().getString("$drops." + key + ".m");
				String n = this.getConfig().getString("$drops." + key + ".n");
				String l = this.getConfig().getString("$drops." + key + ".l");
				if (n.equals(NO_NOT)) n = null;
				if (l.equals(NO_NOT)) l = null;
				Artefact artefact = new Artefact(m, n, l);
				dropsm.add(artefact);
			}
		}
		ddeadm.clear();
		if (this.getConfig().getConfigurationSection("$ddead") != null) {
			for (String key : this.getConfig().getConfigurationSection("$ddead").getKeys(false)) {
				String m = this.getConfig().getString("$ddead." + key + ".m");
				String n = this.getConfig().getString("$ddead." + key + ".n");
				String l = this.getConfig().getString("$ddead." + key + ".l");
				if (n.equals(NO_NOT)) n = null;
				if (l.equals(NO_NOT)) l = null;
				Artefact artefact = new Artefact(m, n, l);
				ddeadm.add(artefact);
			}
		}
		placem.clear();
		if (this.getConfig().getConfigurationSection("$place") != null) {
			for (String key : this.getConfig().getConfigurationSection("$place").getKeys(false)) {
				String m = this.getConfig().getString("$place." + key + ".m");
				String n = this.getConfig().getString("$place." + key + ".n");
				String l = this.getConfig().getString("$place." + key + ".l");
				if (n.equals(NO_NOT)) n = null;
				if (l.equals(NO_NOT)) l = null;
				Artefact artefact = new Artefact(m, n, l);
				placem.add(artefact);
			}
		}
	}
}
