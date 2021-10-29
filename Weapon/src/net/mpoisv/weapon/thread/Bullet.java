package net.mpoisv.weapon.thread;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.Packet;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.mpoisv.weapon.Gun;

public class Bullet implements Runnable {
	private static final int TickInSpeed = 20;
	private Gun weapon;
	private Player player;
	
	private Timer timer;
	private Vector vector;
	private Location beforeLoc, nowLoc;
	private final Location startLoc;
	private final double tickSpeed;
	
	public Bullet(Gun weapon, Player player) {
		this.weapon = weapon;
		this.player = player;
		startLoc = player.getEyeLocation();
		nowLoc = startLoc.clone();
		
		tickSpeed = weapon.getBulletSpeed() / TickInSpeed;
		vector = startLoc.clone().multiply(0.2).getDirection().normalize().multiply(tickSpeed);
	}

	@Override
	public void run() {
		timer = new Timer();
		timer.schedule(new BulletWorker(), 50, 50);
	}
	
	private void showParticles() {
		ArrayList<Packet<?>> packets = new ArrayList<>();
		for(Particle p : weapon.bulletParticles) {
			packets.add(
					new PacketPlayOutWorldParticles(EnumParticle.valueOf(p.toString()), true,
							(float) nowLoc.getX(), (float) nowLoc.getY(), (float) nowLoc.getZ()
							, 0, 0, 0, 0, 1)
					);
		}
		
		for(Player player : startLoc.getWorld().getPlayers()) {
			if(player == this.player && nowLoc.distance(startLoc) < 1.5) continue;
			
			for(Packet<?> packet : packets)
				((CraftPlayer) player).getHandle().playerConnection.networkManager.sendPacket(packet);
		}
	}
	
	private boolean checkEntityHit() {
		double x = (nowLoc.getX() - beforeLoc.getX()) / tickSpeed;
		double y = (nowLoc.getY() - beforeLoc.getY()) / tickSpeed;
		double z = (nowLoc.getZ() - beforeLoc.getZ()) / tickSpeed;
		
		Location checkLoc = beforeLoc.clone();
		for(double i = 0; i < tickSpeed; i += 0.8) {
			checkLoc.add(x, y, z);
			if(nearEntityCheck(checkLoc, 0.4))
				return true;
		}
		
		return false;
	}
	
	private boolean nearEntityCheck(Location loc, double radius) {
		for(Entity entity : loc.getWorld().getNearbyEntities(loc, radius, .05, radius)) {
			if(entity instanceof LivingEntity) {
				LivingEntity target = (LivingEntity) entity;
				if(target.isDead() || target.getHealth() <= 0) continue;

				if(weapon.onDamaged(player, (LivingEntity) entity)) {
					try {
						target.damage(weapon.getDamage(), player);
					}catch(Exception e) {}
					
					return true;
				}
			}
		}
		
		return false;
	}
	
	private class BulletWorker extends TimerTask {

		@Override
		public void run() {
			beforeLoc = nowLoc.clone();
			nowLoc.add(vector);
			if(nowLoc.distance(startLoc) > weapon.getMaxDistance() || !nowLoc.getBlock().getType().isTransparent()) {
				timer.cancel();
				return;
			}
			
			showParticles();
			if(checkEntityHit())
				timer.cancel();
		}
		
	}
}
