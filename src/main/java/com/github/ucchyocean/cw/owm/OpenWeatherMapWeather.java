/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.owm;

import java.util.Date;

import org.w3c.dom.Node;

/**
 * OpenWeatherMap天気情報
 * @author ucchy
 */
public class OpenWeatherMapWeather {

    private OpenWeatherMapCity city;
    private double temparatureMax;
    private double temparatureMin;
    private String temparatureUnit;
    private double temparature;
    private String humidityUnit;
    private int humidity;
    private String pressureUnit;
    private double pressure;
    private OpenWeatherMapWind wind;
    private String cloudsName;
    private int clouds;
    private double precipitation;
    private String precipitationMode;
    private String precipitationUnit;
    private String weatherIcon;
    private int weatherNumber;
    private String weather;
    private Date lastupdate;
    
    private OpenWeatherMapWeather() {
    }
    
    /**
     * 指定されたXMLノードから、天気情報を読み取り、インスタンスを生成する。
     * @param node XMLノード
     * @return インスタンス
     * @throws OpenWeatherMapAccessException 
     */
    public static OpenWeatherMapWeather parse(Node node) 
            throws OpenWeatherMapAccessException {
        
        OpenWeatherMapWeather weather = new OpenWeatherMapWeather();
        weather.city = OpenWeatherMapCity.parse(
                XMLUtility.getNodeFromNodeList(node.getChildNodes(), "city"));
        weather.temparatureMax = XMLUtility.getDoubleFromPathAttr(node, "temperature", "max");
        weather.temparatureMin = XMLUtility.getDoubleFromPathAttr(node, "temperature", "min");
        weather.temparatureUnit = XMLUtility.getStringFromPathAttr(node, "temperature", "unit");
        weather.temparature = XMLUtility.getDoubleFromPathAttr(node, "temperature", "value");
        weather.humidityUnit = XMLUtility.getStringFromPathAttr(node, "humidity", "unit");
        weather.humidity = XMLUtility.getIntFromPathAttr(node, "humidity", "value");
        weather.pressureUnit = XMLUtility.getStringFromPathAttr(node, "pressure", "unit");
        weather.pressure = XMLUtility.getDoubleFromPathAttr(node, "pressure", "value");
        weather.wind = OpenWeatherMapWind.parse(
                XMLUtility.getNodeFromNodeList(node.getChildNodes(), "wind"));
        weather.cloudsName = XMLUtility.getStringFromPathAttr(node, "clouds", "name");
        weather.clouds = XMLUtility.getIntFromPathAttr(node, "clouds", "value");
        weather.precipitationMode = XMLUtility.getStringFromPathAttr(node, "precipitation", "mode");
        if ( !weather.precipitationMode.equalsIgnoreCase("no") ) {
            weather.precipitation = XMLUtility.getDoubleFromPathAttr(node, "precipitation", "value");
            weather.precipitationUnit = XMLUtility.getStringFromPathAttr(node, "precipitation", "unit");
        } else { // 「precipitation mode="no"」の場合は、value と unit の属性値が存在しない
            weather.precipitation = 0;
            weather.precipitationUnit = "";
        }
        weather.weatherIcon = XMLUtility.getStringFromPathAttr(node, "weather", "icon");
        weather.weatherNumber = XMLUtility.getIntFromPathAttr(node, "weather", "number");
        weather.weather = XMLUtility.getStringFromPathAttr(node, "weather", "value");
        weather.lastupdate = XMLUtility.getDateFromPathAttr(node, "lastupdate", "value");
        
        return weather;
    }

    /**
     * @return 都市情報
     */
    public OpenWeatherMapCity getCity() {
        return city;
    }

    /**
     * @return 最高気温
     */
    public double getTemparatureMax() {
        return temparatureMax;
    }

    /**
     * @return 最低気温
     */
    public double getTemparatureMin() {
        return temparatureMin;
    }

    /**
     * @return 気温の単位、URLを変更しない限りは必ず"celsius"になるはず
     */
    public String getTemparatureUnit() {
        return temparatureUnit;
    }

    /**
     * @return 現在の気温
     */
    public double getTemparature() {
        return temparature;
    }

    /**
     * @return 湿度の単位、URLを変更しない限りは必ず"%"になるはず
     */
    public String getHumidityUnit() {
        return humidityUnit;
    }

    /**
     * @return 湿度
     */
    public int getHumidity() {
        return humidity;
    }

    /**
     * @return 気圧の単位、URLを変更しない限りは必ず"hPa"になるはず
     */
    public String getPressureUnit() {
        return pressureUnit;
    }

    /**
     * @return 気圧
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * @return 風情報
     */
    public OpenWeatherMapWind getWind() {
        return wind;
    }

    /**
     * @return 雲の状況（文字列表現）
     */
    public String getCloudsName() {
        return cloudsName;
    }

    /**
     * @return 雲（快晴で0、曇天で100）
     */
    public int getClouds() {
        return clouds;
    }

    /**
     * @return 降雨降雪量
     */
    public double getPrecipitation() {
        return precipitation;
    }

    /**
     * @return 降雨降雪の状況（無しで"no"、雨で"rain" など）
     */
    public String getPrecipitationMode() {
        return precipitationMode;
    }

    /**
     * @return 降雨降雪量の単位（"1h"、"3h"、など）
     */
    public String getPrecipitationUnit() {
        return precipitationUnit;
    }

    /**
     * @return 天気アイコンのID
     */
    public String getWeatherIcon() {
        return weatherIcon;
    }

    /**
     * @return 天気コード
     */
    public int getWeatherNumber() {
        return weatherNumber;
    }

    /**
     * @return 天気（文字列表現）
     */
    public String getWeather() {
        return weather;
    }

    /**
     * @return 天気データの最終更新時間
     */
    public Date getLastupdate() {
        return lastupdate;
    }
    
    /**
     * オブジェクトの文字列表現を返す。デバッグ用。
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return String.format(
                "city={%s}, temparatureMax=%f, temparatureMin=%f, temparatureUnit=%s, " +
                "temparature=%f, humidityUnit=%s, humidity=%d, pressureUnit=%s, " +
                "pressure=%f, wind={%s}, cloudsName=%s, clouds=%d, precipitation=%f, " +
                "precipitationMode=%s, precipitationUnit=%s, " +
                "weatherIcon=%s, weatherNumber=%d, weather=%s, lastupdate=%s",
                city, temparatureMax, temparatureMin, temparatureUnit, 
                temparature, humidityUnit, humidity, pressureUnit, 
                pressure, wind, cloudsName, clouds, precipitation, 
                precipitationMode, precipitationUnit,
                weatherIcon, weatherNumber, weather, lastupdate);
    }
} 
