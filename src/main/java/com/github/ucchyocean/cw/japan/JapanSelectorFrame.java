/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.japan;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.ucchyocean.cw.owm.OpenWeatherMapWeather;

/**
 * 日本地図から都市を選択するGUIを提供するクラス
 * @author ucchy
 */
public class JapanSelectorFrame extends JFrame {

    private static final String WINDOW_TITLE = "Japan Selector";
    protected OpenWeatherMapWeather selected;
    private JapanPanel panel;

    /**
     * コンストラクタ
     */
    public JapanSelectorFrame() {
        super(WINDOW_TITLE);
        panel = new JapanPanel(this);
        getContentPane().add(panel);
        pack();
    }

    /**
     * GUIを開く
     * @return GUI内でユーザーが選択した都市、キャンセルしたり閉じた場合はnullになる
     */
    public OpenWeatherMapWeather showWindow() {

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);

        JOptionPane.showMessageDialog(this, "設定したい都市を、地図上でクリックしてください。");

        try {
            while ( this.isVisible() ) {
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        panel.flushImage();

        return selected;
    }

    /**
     * デバッグ用エントリ
     * @param args
     */
    public static void main(String[] args) {
        JapanSelectorFrame f = new JapanSelectorFrame();
        System.out.println( f.showWindow() );
    }
}
