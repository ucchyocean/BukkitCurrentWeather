/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.owm;

/**
 * OpenWeatherMapへのアクセス時に発生する例外クラス
 * @author ucchy
 */
public class OpenWeatherMapAccessException extends Exception {

    /**
     * コンストラクタ
     * @param message 例外メッセージ
     */
    public OpenWeatherMapAccessException(String message) {
        super(message);
    }

    /**
     * コンストラクタ
     * @param message 例外メッセージ
     * @param cause 基底例外クラス
     */
    public OpenWeatherMapAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
