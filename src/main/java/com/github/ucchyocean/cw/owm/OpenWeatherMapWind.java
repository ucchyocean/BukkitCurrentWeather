/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.owm;

import org.w3c.dom.Node;

/**
 * OpenWeatherMap風情報
 * @author ucchy
 */
public class OpenWeatherMapWind {

    private String speedName;
    private double speed;
    private String directionCode;
    private String directionName;
    private double direction;

    private OpenWeatherMapWind() {
    }
    
    /**
     * 指定されたXMLノードから、風情報を読み取り、インスタンスを生成する。
     * @param node XMLノード
     * @return インスタンス
     * @throws OpenWeatherMapAccessException 
     */
    public static OpenWeatherMapWind parse(Node node) 
            throws OpenWeatherMapAccessException {
        
        OpenWeatherMapWind wind = new OpenWeatherMapWind();
        wind.speedName = XMLUtility.getStringFromPathAttr(node, "speed", "name");
        wind.speed = XMLUtility.getDoubleFromPathAttr(node, "speed", "value");
        wind.directionCode = XMLUtility.getStringFromPathAttr(node, "direction", "code");
        wind.directionName = XMLUtility.getStringFromPathAttr(node, "direction", "name");
        wind.direction = XMLUtility.getDoubleFromPathAttr(node, "direction", "value");
        return wind;
    }

    /**
     * @return 風の強さ（文字列表現）
     */
    public String getSpeedName() {
        return speedName;
    }

    /**
     * @return 風の強さ
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * @return 風向きコード（例：NNE、SW）
     */
    public String getDirectionCode() {
        return directionCode;
    }

    /**
     * @return 風向き（文字列表現）
     */
    public String getDirectionName() {
        return directionName;
    }

    /**
     * @return 風向き
     */
    public double getDirection() {
        return direction;
    }
    
    /**
     * オブジェクトの文字列表現を返す。デバッグ用。
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "speedName=%s, speed=%f, directionCode=%s, directionName=%s, direction=%f",
                speedName, speed, directionCode, directionName, direction);
    }
}
