/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.japan;

/**
 * JapanPanelのクリック座標を調整するための、係数・切片を算出するためのクラス。
 * このクラスはデバッグにしか使用されない。
 * @author ucchy
 */
public class JapanPanelAdjuster {

    /**
     * @param args
     */
    public static void main(String[] args) {

        int sapporo_x = 285;
        int sapporo_y = 83;
        int sendai_x = 275;
        int sendai_y = 226;
        int tokyo_x = 250;
        int tokyo_y = 301;
        int hiroshima_x = 82;
        int hiroshima_y = 336;
        int kagoshima_x = 38;
        int kagoshima_y = 413;

        int okinawa_x = 104;
        int okinawa_y = 96;

        double sapporo_lat = 43.0344;
        double sapporo_lon = 141.2116;
        double sendai_lat = 38.268839;
        double sendai_lon = 140.872103;
        double tokyo_lat = 35.681382;
        double tokyo_lon = 139.766084;
        double hiroshima_lat = 34.2307;
        double hiroshima_lon = 132.271;
        double kagoshima_lat = 31.355;
        double kagoshima_lon = 130.3329;

        double okinawa_lat = 26.212319;
        double okinawa_lon = 127.679188;

        int[] array_x = {sapporo_x, sendai_x, tokyo_x, hiroshima_x, kagoshima_x};
        int[] array_y = {sapporo_y, sendai_y, tokyo_y, hiroshima_y, kagoshima_y};
        double[] array_lat =
            {sapporo_lat, sendai_lat, tokyo_lat, hiroshima_lat, kagoshima_lat};
        double[] array_lon =
            {sapporo_lon, sendai_lon, tokyo_lon, hiroshima_lon, kagoshima_lon};

        // 下記の連立方程式を解いて、a b c d を求める
        //  92 * a + b =  43.0344
        // 423 * a + b =  31.355
        // 289 * c + d = 141.2116
        //  44 * c + d = 130.3329

        // 全都市の平均値を算出する
        double ave_x = 0;
        for ( int x : array_x )
            ave_x += x;
        ave_x /= array_x.length;
        double ave_y = 0;
        for ( int y : array_y )
            ave_y += y;
        ave_y /= array_y.length;
        double ave_lat = 0;
        for ( double lat : array_lat )
            ave_lat += lat;
        ave_lat /= array_lat.length;
        double ave_lon = 0;
        for ( double lon : array_lon )
            ave_lon += lon;
        ave_lon /= array_lon.length;


        double a = 0;
        double c = 0;
        for ( int i = 0; i < array_x.length; i++ ) {
            a += (ave_lat - (double)array_lat[i]) / (ave_y - array_y[i]);
            c += (ave_lon - (double)array_lon[i]) / (ave_x - array_x[i]);
        }
        a /= array_lat.length;
        double b = ave_lat - (ave_y * a);
        c /= array_lon.length;
        double d = ave_lon - (ave_x * c);

        System.out.println("    private static final double LAT_GRADIENTS = " + a + ";");
        System.out.println("    private static final double LAT_INTERCEPTS = " + b + ";");
        System.out.println("    private static final double LON_GRADIENTS = " + c + ";");
        System.out.println("    private static final double LON_INTERCEPTS = " + d + ";");

        double okinawa_lat_int = okinawa_lat - (okinawa_y * a);
        double okinawa_lon_int = okinawa_lon - (okinawa_x * c);
        System.out.println("    private static final double LAT_INTERCEPTS_OKINAWA = " + okinawa_lat_int + ";");
        System.out.println("    private static final double LON_INTERCEPTS_OKINAWA = " + okinawa_lon_int + ";");
    }

}
