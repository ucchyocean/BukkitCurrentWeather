/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * プラグインのメッセージリソース管理クラス
 * @author ucchy
 */
public class Messages {

    private static final String FILE_NAME = "messages.yml";
    private static final String FILE_NAME_JA = "messages_ja.yml";

    private static YamlConfiguration defaultMessages;
    private static YamlConfiguration resources;

    /**
     * 初期化する
     */
    protected static void initialize() {

        // messages_ja.yml を書き出す
        File file = new File(
                CurrentWeather.instance.getDataFolder() +
                File.separator + FILE_NAME_JA);

        if ( !file.exists() ) {
            Utility.copyFileFromJar(
                    CurrentWeather.instance.getPluginJarFile(), file, FILE_NAME_JA);
        }
        
        // messages.yml を書き出す
        file = new File(
                CurrentWeather.instance.getDataFolder() +
                File.separator + FILE_NAME);

        if ( !file.exists() ) {
            Utility.copyFileFromJar(
                    CurrentWeather.instance.getPluginJarFile(), file, FILE_NAME);
        }

        // ファイルをロードする
        defaultMessages = loadDefaultMessages();
        resources = YamlConfiguration.loadConfiguration(file);
    }

    /**
     * リソースを取得する
     * @param key リソースキー
     * @param defvalue デフォルト
     * @return リソース
     */
    public static String get(String key, String defvalue) {

        if ( resources == null ) {
            initialize();
        }
        String def = defaultMessages.getString(key, defvalue);
        return Utility.replaceColorCode(resources.getString(key, def));
    }

    /**
     * リソースを取得する
     * @param key リソースキー
     * @return リソース
     */
    public static String get(String key) {
        return get(key, "");
    }

    /**
     * Jarファイル内から直接 messages.yml を読み込み、YamlConfigurationにして返すメソッド
     * @return
     */
    private static YamlConfiguration loadDefaultMessages() {

        YamlConfiguration messages = new YamlConfiguration();
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(CurrentWeather.instance.getPluginJarFile());
            ZipEntry zipEntry = jarFile.getEntry(FILE_NAME);
            InputStream inputStream = jarFile.getInputStream(zipEntry);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            String line;
            while ( (line = reader.readLine()) != null ) {
                if ( line.contains(":") && !line.startsWith("#") ) {
                    String key = line.substring(0, line.indexOf(":")).trim();
                    String value = line.substring(line.indexOf(":") + 1).trim();
                    if ( value.startsWith("'") && value.endsWith("'") )
                        value = value.substring(1, value.length()-1);
                    messages.set(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if ( jarFile != null ) {
                try {
                    jarFile.close();
                } catch (IOException e) {
                    // do nothing.
                }
            }
        }

        return messages;
    }
}
