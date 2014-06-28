/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * 時間ごとにワールドに時刻を同期するタスククラス
 * @author ucchy
 */
public class SyncTimeTask extends BukkitRunnable {

    /**
     * 呼び出しごとに実行されるタスク処理
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        CurrentWeather.instance.getLogger().finest("- run SyncTimeTask [" + hashCode() + "]");

        // 時刻情報を計算して、サーバーに反映
        Bukkit.getScheduler().runTask(CurrentWeather.instance, new BukkitRunnable() {
            @Override
            public void run() {
                CurrentWeather.instance.syncTime();
            }
        });
    }
}
