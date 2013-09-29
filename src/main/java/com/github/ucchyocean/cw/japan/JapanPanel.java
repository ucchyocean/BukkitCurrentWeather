/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.japan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

/**
 * 日本地図パネル
 * @author ucchy
 */
public class JapanPanel extends JPanel {

    // 地図画像の幅と高さ
    private static final int WIDTH = 392;
    private static final int HEIGHT = 436;
    
    // 地図画像の (x, y) 座標から、緯度経度を求めるための、係数と切片
    private static final double LAT_GRADIENTS = -0.03469395065957088;
    private static final double LAT_INTERCEPTS = 45.94387998927136;
    private static final double LON_GRADIENTS = 0.04440741528434739;
    private static final double LON_INTERCEPTS = 128.63095815711137;
    private static final double LAT_INTERCEPTS_OKINAWA = 29.542938263318806;
    private static final double LON_INTERCEPTS_OKINAWA = 123.06081681042787;
    private static final int ENDS_OKINAWA_X = 207;
    private static final int ENDS_OKINAWA_Y = 177;
    
    private static final String IMAGE_FILE_PATH = "japan.png";
    
    private JapanSelectorFrame ownerFrame;
    private Image image;
    
    /**
     * コンストラクタ
     * @param owner 親となるフレームクラス
     */
    public JapanPanel(JapanSelectorFrame owner) {
        ownerFrame = owner;
        setSize(getPreferredSize());
        setBackground(Color.WHITE);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                double[] temp = getLatLonFromXY(e.getX(), e.getY());
                double lat = temp[0];
                double lon = temp[1];
                CitySelectionDialog dialog = new CitySelectionDialog(ownerFrame, lat, lon);
                dialog.showDialog();
            }
        });
    }

    /**
     * パネルのサイズを返す
     * @see javax.swing.JComponent#getPreferredSize()
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDTH, HEIGHT);
    }
    
    /**
     * パネルの描画処理
     * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // 画像データのロードと描画
        Toolkit kit = Toolkit.getDefaultToolkit();
        image = kit.getImage(
                getClass().getClassLoader().getResource(IMAGE_FILE_PATH));
        g.drawImage(image, 0, 0, WIDTH, HEIGHT, this);
    }
    
    /**
     * 内部に保持している画像データを破棄して、メモリを開放します。
     * ウィンドウが閉じるときに必ず呼び出してください。
     */
    protected void flushImage() {
        if ( image != null ) {
            image.flush();
        }
    }
    
    /**
     * パネル内で選択されたX座標・Y座標を、緯度・経度に変換して返す
     * @param x X座標
     * @param y Y座標
     * @return {経度, 緯度}
     */
    private double[] getLatLonFromXY(int x, int y) {
        double lat;
        double lon;
        if ( x <= ENDS_OKINAWA_X && y <= ENDS_OKINAWA_Y ) {
            lat = LAT_GRADIENTS * y + LAT_INTERCEPTS_OKINAWA;
            lon = LON_GRADIENTS * x + LON_INTERCEPTS_OKINAWA;
        } else {
            lat = LAT_GRADIENTS * y + LAT_INTERCEPTS;
            lon = LON_GRADIENTS * x + LON_INTERCEPTS;
        }
        return new double[]{lat, lon};
    }
}
