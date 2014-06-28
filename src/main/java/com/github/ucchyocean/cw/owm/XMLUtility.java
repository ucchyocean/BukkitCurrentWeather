/*
 * @author     kebin, ucchy
 * @license    LGPLv3
 * @copyright  Copyright kebin, ucchy 2013
 */
package com.github.ucchyocean.cw.owm;

import java.util.Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * XMLユーティリティクラス
 * @author ucchy
 */
public class XMLUtility {

    private static DatatypeFactory dtfactory;

    /**
     * 指定されたノードの直下にある、指定された名前のタグを取得する。
     * @param root ノード
     * @param name 取得するタグ名
     * @return 取得されたノード、ただしnameがnullの場合はrootが返される
     * @throws OpenWeatherMapAccessException
     */
    protected static Node getNodeFromPath(Node root, String name)
            throws OpenWeatherMapAccessException {

        if ( name == null || name.equals("") ) {
            return root;
        }

        Node node = getNodeFromNodeList(root.getChildNodes(), name);
        if ( node == null ) {
            throw new OpenWeatherMapAccessException(
                    "Error: Could not find tag \"" + name + "\".");
        }

        return node;
    }

    /**
     * 指定されたノードの直下にある、指定された名前のタグの中のテキストを取得する。
     * @param root ノード
     * @param name 取得するタグ名
     * @return 取得されたテキスト
     * @throws OpenWeatherMapAccessException
     */
    protected static String getNodeValueFromPath(Node root, String name)
            throws OpenWeatherMapAccessException {

        Node node = getNodeFromPath(root, name);
        return node.getTextContent();
    }

    /**
     * 指定されたノードの直下にある、指定された名前のタグの属性値を取得する。
     * @param root ノード
     * @param name 取得するタグ名
     * @param attr 取得する属性名
     * @return 取得された属性値
     * @throws OpenWeatherMapAccessException
     */
    protected static String getStringFromPathAttr(Node root, String name, String attr)
            throws OpenWeatherMapAccessException {

        Node node = getNodeFromPath(root, name);
        NamedNodeMap attrs = node.getAttributes();
        Node attrNode = attrs.getNamedItem(attr);
        if ( attrNode == null ) {
            throw new OpenWeatherMapAccessException(
                    "Error: Could not find attr \"" + attr + "\" of \"" + name + "\".");
        }
        return attrNode.getNodeValue();
    }

    /**
     * 指定されたノードの直下にある、指定された名前のタグの属性値を、Date型で取得する。
     * @param root ノード
     * @param name 取得するタグ名
     * @param attr 取得する属性名
     * @return 取得された属性値
     * @throws OpenWeatherMapAccessException
     */
    protected static Date getDateFromPathAttr(Node root, String name, String attr)
            throws OpenWeatherMapAccessException {

        if ( dtfactory == null ) {
            try {
                dtfactory = DatatypeFactory.newInstance();
            } catch (DatatypeConfigurationException e) {
                throw new OpenWeatherMapAccessException(
                        "Error: Could not make DatataypeFactory.", e);
            }
        }

        String value = getStringFromPathAttr(root, name, attr);
        XMLGregorianCalendar cal =
                dtfactory.newXMLGregorianCalendar(value);
        cal.setTimezone(0); // 取得された日時をグリニッジ標準時として解釈させる。
        return cal.toGregorianCalendar().getTime();
    }

    /**
     * 指定されたノードの直下にある、指定された名前のタグの属性値を、double型で取得する。
     * @param root ノード
     * @param name 取得するタグ名
     * @param attr 取得する属性名
     * @return 取得された属性値
     * @throws OpenWeatherMapAccessException
     */
    protected static double getDoubleFromPathAttr(Node root, String name, String attr)
            throws OpenWeatherMapAccessException {

        String value = getStringFromPathAttr(root, name, attr);
        return Double.parseDouble(value);
    }

    /**
     * 指定されたノードの直下にある、指定された名前のタグの属性値を、int型で取得する。
     * @param root ノード
     * @param name 取得するタグ名
     * @param attr 取得する属性名
     * @return 取得された属性値
     * @throws OpenWeatherMapAccessException
     */
    protected static int getIntFromPathAttr(Node root, String name, String attr)
            throws OpenWeatherMapAccessException {

        String value = getStringFromPathAttr(root, name, attr);
        return Integer.parseInt(value);
    }

    /**
     * 指定されたノードの直下にある、指定された名前のタグの属性値を、boolean型で取得する。
     * @param root ノード
     * @param name 取得するタグ名
     * @param attr 取得する属性名
     * @return 取得された属性値
     * @throws OpenWeatherMapAccessException
     */
    protected static boolean getBooleanFromPathAttr(Node root, String name, String attr)
            throws OpenWeatherMapAccessException {

        String value = getStringFromPathAttr(root, name, attr);
        return (value.equalsIgnoreCase("yes") || value.equalsIgnoreCase("true"));
    }

    /**
     * 指定されたノードリスト内にある、指定された名前のタグを取得する。
     * @param list ノードリスト
     * @param name 取得するタグ名
     * @return 取得されたノード、見つからない場合はnullになる
     */
    protected static Node getNodeFromNodeList(NodeList list, String name) {

        for ( int i=0; i<list.getLength(); i++ ) {
            Node node = list.item(i);
            if ( node.getNodeName().equals(name) ) {
                return node;
            }
        }
        return null;
    }
}
