package net.mpoisv.weapon.main;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import net.mpoisv.weapon.Gun;
import net.mpoisv.weapon.Weapon;
import net.mpoisv.weapon.WeaponType;

public class AKM extends Gun {
	
	public AKM() {
		bulletParticles = Arrays.asList(Particle.CRIT_MAGIC);
		Weapon.registerWeapon(this);
	}

	@Override
	public boolean onDamaged(Player shooter, LivingEntity entity) {
		if(shooter == entity || entity.isDead()) return false;
		
		shooter.playSound(shooter.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, .04f, 1);
		return true;
	}

	@Override
	public void interaction(PlayerInteractEvent event) {
		event.setCancelled(true);
		
		setWeaponInfo(this, event.getPlayer());
		
		if(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if(shoot(this, event.getPlayer())) {
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_BLAZE_SHOOT, 0.15f, .4f);
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_IRON_DOOR_CLOSE, 0.1f, .5f);
				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ENTITY_IRONGOLEM_ATTACK, 0.3f, 1f);
			}
		}
	}

	@Override
	public String getName() {
		return "AKM";
	}

	@Override
	public List<String> getDescription() {
		return Arrays.asList("§f미하일 칼라시니코프가 발명한 돌격소총");
	}
	
	@Override
	public Material getItem() {
		return Material.WOOD_HOE;
	}
	
	@Override
	public double getBulletSpeed() {
		return 50;
	}
	
	@Override
	public double getMaxDistance() {
		return 50;
	}
	
	@Override
	public double getDamage() {
		return 1.8;
	}
	
	@Override
	public WeaponType getType() {
		return WeaponType.MAIN;
	}

	@Override
	public int getMaxBulletAmount() {
		// TODO Auto-generated method stub
		return 30;
	}

	@Override
	public double getReloadDuration() {
		return 1.5;
	}

	@Override
	public double getBulletFireDuration() {
		// TODO Auto-generated method stub
		return 0.3;
	}

	@Override
	public double getWeaponChangeDuration() {
		// TODO Auto-generated method stub
		return 0;
	}
}
