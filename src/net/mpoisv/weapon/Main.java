package net.mpoisv.weapon;

import net.mpoisv.weapon.main.TestWeapon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import net.mpoisv.weapon.main.AKM;
import net.mpoisv.weapon.thread.ThreadManager;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(new EventManager(), this);
		initWeapons();

		for(Player player : Bukkit.getOnlinePlayers()) {
			Weapon.registerPlayer(player);
		}
		
		Bukkit.getConsoleSender().sendMessage("§c" + getDescription().getName() + "§f 로드 완료.");
	}
	
	@Override
	public void onDisable() {
		ThreadManager.stop();
		Bukkit.getConsoleSender().sendMessage("§c"+ getDescription().getName() + "§f 종료 성공");
	}
	
	private void initWeapons() {
		new AKM();
		new TestWeapon();
	}
}
