/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.japan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.github.ucchyocean.cw.owm.OpenWeatherMapAccessException;
import com.github.ucchyocean.cw.owm.OpenWeatherMapAccesser;
import com.github.ucchyocean.cw.owm.OpenWeatherMapCity;
import com.github.ucchyocean.cw.owm.OpenWeatherMapWeather;

/**
 * 取得された都市の候補から、必要な都市を選択するダイアログを提供するクラス
 * @author ucchy
 */
public class CitySelectionDialog extends JDialog implements Runnable {

    private double lat;
    private double lon;
    private OpenWeatherMapAccessException lastException;

    private JPanel rootPane;
    private JLabel progressLabel1;
    private JLabel progressLabel2;
    private JButton cancelButton;

    private CitySelectionDialog dialog;
    private JapanSelectorFrame owner;

    /**
     * コンストラクタ
     * @param owner モーダル元
     * @param lat 緯度
     * @param lon 経度
     */
    public CitySelectionDialog(JapanSelectorFrame owner, double lat, double lon) {

        super(owner, true);
        dialog = this;
        this.owner = owner;
        this.lat = lat;
        this.lon = lon;

        Container contentPane = getContentPane();
        rootPane = new JPanel();

        contentPane.setLayout(new FlowLayout());
        contentPane.add(rootPane, BorderLayout.CENTER);

        rootPane.setLayout(new GridLayout(0, 1, 5, 5));

        progressLabel1 = new JLabel("情報取得中です...", JLabel.CENTER);
        progressLabel2 = new JLabel("", JLabel.CENTER);
        cancelButton = new JButton("キャンセル");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        rootPane.add(progressLabel1);
        rootPane.add(progressLabel2);
        rootPane.add(cancelButton);

        pack();
    }

    /**
     * ダイアログを開く。
     * 開くと同時にスレッドが起動し、緯度経度に見合った都市の取得を行う。
     * 取得が完了しだい、選択肢を表示する。
     */
    public void showDialog() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        new Thread(this).start();
        setVisible(true);
    }

    /**
     * 非同期実行部分。緯度経度に見合った都市の取得を行う。
     * 完了しだい、コールバックが呼ばれる。
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {

        ArrayList<OpenWeatherMapWeather> results = null;
        try {
            results = OpenWeatherMapAccesser.findCity(lat, lon);
        } catch (OpenWeatherMapAccessException e) {
            lastException = e;
            e.printStackTrace();
        }
        callback(results);
    }

    /**
     * 非同期処理のコールバック。
     * @param results 非同期処理の実行結果
     */
    private void callback(ArrayList<OpenWeatherMapWeather> results) {

        if ( results == null ) {
            // エラーが発生した場合

            progressLabel1.setText("都市情報取得でエラーが発生しました。");
            progressLabel1.setForeground(Color.RED);
            progressLabel2.setText(lastException.toString());
            progressLabel2.setForeground(Color.RED);
            cancelButton.setText("閉じる");
            pack();
            return;
        }

        if ( results.size() == 0 ) {
            // 一つも該当が見つからない場合

            progressLabel1.setText(
                    "クリックした場所に都市が見つかりませんでした。");
            cancelButton.setText("閉じる");
            pack();
            return;
        }

        progressLabel1.setText(results.size() + " 個の都市が見つかりました。");
        progressLabel2.setText("都市を選択してください。");
        for ( OpenWeatherMapWeather r : results ) {
            final OpenWeatherMapWeather weather = r;
            OpenWeatherMapCity city = weather.getCity();
            double distance = calcDistance(lat, lon, city.getLat(), city.getLon());
            String desc = String.format(
                    "%s, %s (%.2f, %.2f) %.1fm",
                    city.getName(), city.getCountry(), city.getLat(), city.getLon(), distance );
            JButton button = new JButton(desc);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    owner.selected = weather;
                    dialog.dispose();
                    owner.dispose();
                }
            });
            rootPane.add(button);
        }
        pack();
    }

    private static double calcDistance(double lat1, double lng1,
                                        double lat2, double lng2){

        double a = 6378137.000;
        double e2 = 0.00669438002301188;
        double mnum = 6335439.32708317;

        double my = deg2rad((lat1 + lat2) / 2.0);
        double dy = deg2rad(lat1 - lat2);
        double dx = deg2rad(lng1 - lng2);

        double sin = Math.sin(my);
        double w = Math.sqrt(1.0 - e2 * sin * sin);
        double m = mnum / (w * w * w);
        double n = a / w;

        double dym = dy * m;
        double dxncos = dx * n * Math.cos(my);

        return Math.sqrt(dym * dym + dxncos * dxncos);
    }

    private static double deg2rad(double deg){
        return deg * Math.PI / 180.0;
    }
}
