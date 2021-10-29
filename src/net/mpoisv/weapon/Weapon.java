package net.mpoisv.weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.mpoisv.weapon.player.PlayerWeaponInfo;

public abstract class Weapon {
	private static ArrayList<Weapon> weaponList = new ArrayList<>();
	private static HashMap<Player, PlayerWeaponInfo> playerInfos = new HashMap<>();
	
	public abstract void interaction(PlayerInteractEvent event);
	public abstract String getName();
	public abstract List<String> getDescription();
	
	public abstract Material getItem();
	public abstract WeaponType getType();
	
	public abstract double getDamage();
	public abstract double getWeaponChangeDuration();
	
	public static void registerWeapon(Weapon weapon) {
		weaponList.add(weapon);
	}
	
	public static List<Weapon> getWeaponList() {
		return weaponList;
	}
	
	public static void registerPlayer(Player player) {
		if(playerInfos.containsKey(player)) return;
		playerInfos.put(player, new PlayerWeaponInfo());
	}
	
	public static void removePlayer(Player player) {
		playerInfos.remove(player);
	}
	
	public static PlayerWeaponInfo getPlayer(Player player) {
		return playerInfos.get(player);
	}
	
	public static void setWeaponInfo(Weapon weapon, Player player) {
		for(ItemStack item : player.getInventory().all(weapon.getItem()).values()) {
			ItemMeta meta = item.getItemMeta();
			if(meta.getDisplayName() != null && meta.getDisplayName().startsWith("§f" + weapon.getName())) continue;

			meta.setDisplayName("§f" + weapon.getName());
			meta.setLore(weapon.getDescription());
			item.setItemMeta(meta);
		}
	}
}
