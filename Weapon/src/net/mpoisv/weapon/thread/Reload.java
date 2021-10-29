package net.mpoisv.weapon.thread;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.mpoisv.weapon.Gun;
import net.mpoisv.weapon.player.WeaponInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Reload implements Runnable {
    public static final ArrayList<Player> reloadingPlayers = new ArrayList<>();
    private final WeaponInfo info;
    private final Gun weapon;
    private final Player player;

    private final long reloadStartTime;
    private final int currentBulletAmount;

    private Timer timer;

    public Reload(WeaponInfo info, Player player) {
        reloadingPlayers.add(player);

        this.info = info;
        this.weapon = (Gun) info.weapon;
        this.player = player;
        reloadStartTime = System.currentTimeMillis();
        currentBulletAmount = info.bulletAmount;
    }
    @Override
    public void run() {
        timer = new Timer();
        timer.schedule(new ReloadWorker(), 50, 50);
    }

    private void stop(boolean isFinished) {
        timer.cancel();

        if(player != null) {
            reloadingPlayers.remove(player);
            if(!isFinished)
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§7Reload Canceled§c..."));
            else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§7Reload Completed§c!"));
                weapon.onReloaded(player);
                info.bulletAmount = weapon.getMaxBulletAmount();
                weapon.setWeaponInfo(info, player);
            }
        }
    }

    private class ReloadWorker extends TimerTask {

        @Override
        public void run() {
            if(player == null || player.getInventory().getItemInMainHand().getType() != weapon.getItem()
                    || currentBulletAmount != info.bulletAmount) {
                stop(false);
                return;
            }

            long diff = System.currentTimeMillis() - reloadStartTime;
            float percent;
            if(weapon.getReloadDuration() <= 0)
                percent = 1;
            else
                percent = (float) (diff / (weapon.getReloadDuration() * 1000));

            int block = Math.round(percent * 20);

            StringBuffer buf = new StringBuffer();
            buf.append("§c");
            for(int i = 0; i < 20; i++) {
                if(block == i)
                    buf.append("§7");

                buf.append("|");
            }

            buf.append(" §f").append(String.format("%.2fs", weapon.getReloadDuration() - (diff / 1000d)));
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(buf.toString()));

            if(percent >= 1) {
                stop(true);
            }
        }
    }
}
