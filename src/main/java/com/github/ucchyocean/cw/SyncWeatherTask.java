/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw;

import org.bukkit.scheduler.BukkitRunnable;

/**
 * 時間ごとにワールドに天気を同期するタスククラス
 * @author ucchy
 */
public class SyncWeatherTask extends BukkitRunnable {

    /**
     * 呼び出しごとに実行されるタスク処理
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        CurrentWeather.instance.getLogger().finest("- run SyncWeatherTask [" + hashCode() + "]");

        // 天気情報を取得しサーバーに反映
        CurrentWeather.instance.getWeatherInformation();
    }
}
