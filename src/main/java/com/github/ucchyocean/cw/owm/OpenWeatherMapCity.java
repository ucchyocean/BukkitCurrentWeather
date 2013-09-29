/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.owm;

import java.util.Date;

import org.w3c.dom.Node;

/**
 * OpenWeatherMap都市情報
 * @author ucchy
 */
public class OpenWeatherMapCity {
    
    private int id;
    private String name;
    private double lat;
    private double lon;
    private String country;
    private Date sunrise;
    private Date sunset;
    
    private OpenWeatherMapCity() {
    }
    
    /**
     * 指定されたXMLノードから、都市情報を読み取り、インスタンスを生成する。
     * @param node XMLノード
     * @return インスタンス
     * @throws OpenWeatherMapAccessException 
     */
    public static OpenWeatherMapCity parse(Node node) 
            throws OpenWeatherMapAccessException {
        
        OpenWeatherMapCity city = new OpenWeatherMapCity();
        city.id = XMLUtility.getIntFromPathAttr(node, "", "id");
        city.name = XMLUtility.getStringFromPathAttr(node, "", "name");
        city.lat = XMLUtility.getDoubleFromPathAttr(node, "coord", "lat");
        city.lon = XMLUtility.getDoubleFromPathAttr(node, "coord", "lon");
        city.country = XMLUtility.getNodeValueFromPath(node, "country");
        city.sunrise = XMLUtility.getDateFromPathAttr(node, "sun", "rise");
        city.sunset = XMLUtility.getDateFromPathAttr(node, "sun", "set");
        return city;
    }

    /**
     * @return 都市ID
     */
    public int getId() {
        return id;
    }

    /**
     * @return 都市名
     */
    public String getName() {
        return name;
    }

    /**
     * @return 緯度
     */
    public double getLat() {
        return lat;
    }

    /**
     * @return 経度
     */
    public double getLon() {
        return lon;
    }

    /**
     * @return 国コード
     */
    public String getCountry() {
        return country;
    }

    /**
     * @return 次の日の出時刻
     */
    public Date getSunrise() {
        return sunrise;
    }

    /**
     * @return 次の日の入り時刻
     */
    public Date getSunset() {
        return sunset;
    }
    
    /**
     * オブジェクトの文字列表現を返す。デバッグ用。
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "id=%d, name=%s, lat=%f, lon=%f, country=%s, sunrise=%s, sunset=%s",
                id, name, lat, lon, country, sunrise, sunset);
    }
}
