package net.mpoisv.weapon;

import java.util.List;

import net.mpoisv.weapon.player.PlayerWeaponInfo;
import net.mpoisv.weapon.player.WeaponInfo;
import net.mpoisv.weapon.thread.Reload;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import net.mpoisv.weapon.thread.Bullet;
import net.mpoisv.weapon.thread.ThreadManager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class Gun extends Weapon {
	public List<Particle> bulletParticles;
	
	public boolean shoot(Gun weapon, Player shooter) {
		PlayerWeaponInfo playerInfo = Weapon.getPlayer(shooter);
		int id = weapon.getType().ordinal();
		WeaponInfo info = playerInfo.weaponInfos[id];

		long now = System.currentTimeMillis();

		if(info != null && info.weapon != weapon
				&& info.weapon.getType() == weapon.getType()
				&& now - info.lastShootTime < info.weapon.getWeaponChangeDuration() * 1000) {
			shooter.sendTitle(""
					, String.format("§a%.2fs §f초 후 무기를 교체할 수 있습니다.", info.weapon.getWeaponChangeDuration() - ((now - info.lastShootTime) / 1000d))
					, 3, 12, 5);
			return false;
		}

		if(info == null || info.weapon != weapon) {
			playerInfo.weaponInfos[id] = info = playerInfo.getOrCreate(weapon);
		}

		if(info.bulletAmount <= 0) {
			if(!Reload.reloadingPlayers.contains(shooter))
				ThreadManager.startThread(new Reload(info, shooter));

			return false;
		}

		if(now - info.lastShootTime < weapon.getBulletFireDuration() * 1000)
			return false;

		info.lastShootTime = now;
		info.bulletAmount -= 1;

		ThreadManager.startThread(new Bullet(weapon, shooter));
		setWeaponInfo(info, shooter);

		return true;
	}

	public void setWeaponInfo(WeaponInfo info, Player player) {
		for(ItemStack item : player.getInventory().all(info.weapon.getItem()).values()) {
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName("§f" + info.weapon.getName() + " ≪" + info.bulletAmount + "≫");
			meta.setLore(info.weapon.getDescription());
			item.setItemMeta(meta);
		}
	}
	
	public boolean onDamaged(Player shooter, LivingEntity entity) { return true; }
	public void onReloaded(Player player) {}

	public abstract double getMaxDistance();
	public abstract double getBulletSpeed();
	public abstract int getMaxBulletAmount();
	
	public abstract double getReloadDuration();
	public abstract double getBulletFireDuration();
}
