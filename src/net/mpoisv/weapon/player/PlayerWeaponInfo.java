package net.mpoisv.weapon.player;

import net.mpoisv.weapon.Gun;
import net.mpoisv.weapon.Weapon;
import net.mpoisv.weapon.WeaponType;

import java.util.HashMap;

public class PlayerWeaponInfo {
	public WeaponInfo[] weaponInfos = new WeaponInfo[WeaponType.values().length];

	public HashMap<Weapon, WeaponInfo> usedWeapons = new HashMap<>();

	public WeaponInfo getOrCreate(Weapon weapon) {
		WeaponInfo info = usedWeapons.get(weapon);
		if(info == null) {
			info = new WeaponInfo();
			info.weapon = weapon;
			if(weapon instanceof Gun)
				info.bulletAmount = ((Gun) weapon).getMaxBulletAmount();
		}

		return info;
	}
}
