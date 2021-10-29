package net.mpoisv.weapon;

import net.mpoisv.weapon.thread.Reload;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventManager implements Listener {
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Weapon.registerPlayer(event.getPlayer());
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		Weapon.removePlayer(event.getPlayer());
		Reload.reloadingPlayers.remove(event.getPlayer());
	}
	
	@EventHandler
	public void onInteraction(PlayerInteractEvent event) {
		if(!event.hasItem()) return;
		Material item = event.getItem().getType();
		
		for(Weapon weapon : Weapon.getWeaponList()) {
			if(weapon.getItem() == item) weapon.interaction(event);
		}
	}
}
