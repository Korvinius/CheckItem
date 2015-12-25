package net.wealth_mc.checkitem;

import org.bukkit.ChatColor;

public class Artefact {


	private String type;
	private String name;
	private String lore;
	
	public Artefact(String type, String name, String lore) {
		this.type = type;
		this.name = name;
		this.lore = lore;
	}

	public Artefact(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public Artefact(String type) {
		this.type = type;
	}
	
	public Artefact() {
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLore() {
		return lore;
	}

	public void setLore(String lore) {
		this.lore = lore;
	}
	

	@Override 
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lore == null) ? 0 : lore.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artefact other = (Artefact) obj;
		if (lore == null) {
			if (other.lore != null)
				return false;
		} else if (!lore.equals(other.lore))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		if (type != null && name == null && lore == null)
		return type;
		if (type != null && name != null && lore == null)
			return type + ", " + name + ChatColor.RESET;
		
		return type + ", " + name  + ChatColor.RESET + ", " + ChatColor.DARK_PURPLE + lore + ChatColor.RESET;
	}
}
