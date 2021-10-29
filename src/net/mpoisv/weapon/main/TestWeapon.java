package net.mpoisv.weapon.main;

import net.mpoisv.weapon.Gun;
import net.mpoisv.weapon.Weapon;
import net.mpoisv.weapon.WeaponType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class TestWeapon extends Gun {

    public TestWeapon() {
        bulletParticles = Arrays.asList(Particle.CRIT);
        Weapon.registerWeapon(this);
    }

    @Override
    public double getMaxDistance() {
        return 40;
    }

    @Override
    public double getBulletSpeed() {
        return 1;
    }

    @Override
    public int getMaxBulletAmount() {
        return 5;
    }

    @Override
    public double getReloadDuration() {
        return 2;
    }

    @Override
    public double getBulletFireDuration() {
        return 3;
    }

    @Override
    public void interaction(PlayerInteractEvent event) {
        shoot(this, event.getPlayer());
    }

    @Override
    public String getName() {
        return "TEST";
    }

    @Override
    public List<String> getDescription() {
        return Arrays.asList("테스트용 무기");
    }

    @Override
    public Material getItem() {
        return Material.GLOWSTONE_DUST;
    }

    @Override
    public WeaponType getType() {
        return WeaponType.MAIN;
    }

    @Override
    public double getDamage() {
        return 5;
    }

    @Override
    public double getWeaponChangeDuration() {
        return 20;
    }
}
