/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.owm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * OpenWeatherMapへアクセスし、天気情報を取得するクラス
 * @author ucchy
 */
public class OpenWeatherMapAccesser {

    private static boolean DEBUG = false;

    // API ID for ucchy. これは ucchyが保持するAPIキーです。
    // ソースコードを流用する場合は、必ずご自分のAPIキーに変更してください。
    private static final String APIID = "be895364ad410c1772e5a056c05827a4";

    // 都市検索APIのURL（緯度経度）
    private static final String URL_API_FIND_BY_LATLON =
            "http://api.openweathermap.org/data/2.5/find?lat=%.6f&lon=%.6f&units=metric&mode=xml&appid=" + APIID;

    // 都市検索APIのURL（都市名）
    private static final String URL_API_FIND_BY_NAME =
            "http://api.openweathermap.org/data/2.5/find?q=%s&type=like&units=metric&mode=xml&appid=" + APIID;

    // 現在の天気取得APIのURL
    private static final String URL_API_CURRENT =
            "http://api.openweathermap.org/data/2.5/weather?id=%d&units=metric&mode=xml&appid=" + APIID;

    // 詳細天気情報へのURL
    private static final String URL_FORDETAIL = "http://openweathermap.org/city/%d";

    /**
     * 与えられた緯度と経度から、近隣の都市を検索して返します。
     * OpenWeatherMapの仕様で、固定で必ず10個の結果を返すようです。
     * @param lat 緯度
     * @param lon 経度
     * @return 近隣の都市
     * @throws OpenWeatherMapAccessException
     */
    public static ArrayList<OpenWeatherMapWeather> findCity(double lat, double lon)
            throws OpenWeatherMapAccessException {
        String url = String.format(URL_API_FIND_BY_LATLON, lat, lon);
        return getWeatherItemsFromURL(url, "item");
    }

    /**
     * 与えられた地名から、一致する都市を検索して返します。
     * @return 近隣の都市
     * @throws OpenWeatherMapAccessException
     */
    public static ArrayList<OpenWeatherMapWeather> findCity(String name)
            throws OpenWeatherMapAccessException {
        String url = String.format(URL_API_FIND_BY_NAME, name);
        return getWeatherItemsFromURL(url, "item");
    }

    /**
     * 指定された都市IDの、現在の天気状況を返します。
     * @param id OpenWeatherMapで定義されている都市ID
     * @return 指定された都市の天気状況
     * @throws OpenWeatherMapAccessException
     */
    public static OpenWeatherMapWeather getCurrentWeather(int id)
            throws OpenWeatherMapAccessException {
        String url = String.format(URL_API_CURRENT, id);
        ArrayList<OpenWeatherMapWeather> results =
                getWeatherItemsFromURL(url, "current");
        return results.get(0);
    }

    /**
     * 指定された都市IDの、天気詳細を参照するためのURLを返します。
     * @param id OpenWeatherMapで定義されている都市ID
     * @return 指定された都市の天気詳細情報へのURL
     */
    public static String getDetailURL(int id) {
        return String.format(URL_FORDETAIL, id);
    }

    /**
     * 指定されたURLへ接続し、レスポンスのXMLを解析して、指定されたタグを
     * OpenWeatherMapWeather形式で取得します。
     * @param url URL
     * @param tag 取得するタグ名
     * @return 解析された天気情報オブジェクト
     * @throws OpenWeatherMapAccessException
     */
    private static ArrayList<OpenWeatherMapWeather> getWeatherItemsFromURL(String url, String tag)
            throws OpenWeatherMapAccessException {

        ArrayList<OpenWeatherMapWeather> results = new ArrayList<OpenWeatherMapWeather>();
        HttpURLConnection urlconn = null;

        try {
            long start = System.currentTimeMillis();

            urlconn = (HttpURLConnection)((new URL(url)).openConnection());
            urlconn.setRequestMethod("GET");
            urlconn.setInstanceFollowRedirects(false);
            urlconn.connect();

            long point1 = System.currentTimeMillis();

            DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = docBuilder.parse(urlconn.getInputStream(), "UTF-8");

            long point2 = System.currentTimeMillis();

            if ( DEBUG ) {
                try {
                    TransformerFactory transFactory = TransformerFactory.newInstance();
                    Transformer transformer = transFactory.newTransformer();
                    DOMSource source = new DOMSource(document);
                    StreamResult result = new StreamResult(System.out);
                    transformer.transform(source, result);
                    System.out.println();
                } catch (TransformerConfigurationException e) {
                    // do nothing.
                } catch (TransformerFactoryConfigurationError e) {
                    // do nothing.
                } catch (TransformerException e) {
                    // do nothing.
                }
            }

            NodeList list = document.getElementsByTagName(tag);
            for ( int i=0; i<list.getLength(); i++ ) {
                results.add(OpenWeatherMapWeather.parse(list.item(i)));
            }

            long point3 = System.currentTimeMillis();

            if ( DEBUG ) {
                System.out.println("Performance report :");
                System.out.println(String.format("  Create HTTP connection : %d milli-sec", (point1 - start)));
                System.out.println(String.format("  Wait for HTTP responce : %d milli-sec", (point2 - point1)));
                System.out.println(String.format("  Parse XML to objects   : %d milli-sec", (point3 - point2)));
            }

        } catch (MalformedURLException e) {
            throw new OpenWeatherMapAccessException(
                    "Error: Could not parse responce.", e);
        } catch (ProtocolException e) {
            throw new OpenWeatherMapAccessException(
                    "Error: Could not parse responce.", e);
        } catch (IOException e) {
            throw new OpenWeatherMapAccessException(
                    "Error: Could not parse responce.", e);
        } catch (ParserConfigurationException e) {
            throw new OpenWeatherMapAccessException(
                    "Error: Could not parse responce.", e);
        } catch (SAXException e) {
            throw new OpenWeatherMapAccessException(
                    "Error: Could not parse responce.", e);
        } catch (OpenWeatherMapAccessException e) {
            throw e;
        } finally {
            if ( urlconn != null ) {
                try {
                    urlconn.disconnect();
                } catch (Exception e) {
                    // do nothing.
                }
            }
        }

        return results;
    }

    /**
     * デバッグ用エントリポイント
     * @param args
     */
    public static void main(String[] args) {

        DEBUG = true;
        int id = 1848354; // 横浜市
        try {
            System.out.println(getCurrentWeather(id));
            //System.out.println(findCity("Yokohama"));
        } catch (OpenWeatherMapAccessException e) {
            e.printStackTrace();
        }
    }
}
