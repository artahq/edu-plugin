package kz.arta.synergy.maps.gwt.server;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/17/14
 * Time: 12:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class MySqlServerParams {

    private static String serverName = "127.0.0.1";
    private static String mydatabase = "supports";
    private static String url = "jdbc:mysql://" + serverName + "/" + mydatabase;
    private static String username = "root";
    private static String password = "root";

    public static String getUrl() {
        return url;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }
}
