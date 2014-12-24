package kz.arta.synergy.maps.gwt.server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/17/14
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateNewSupport {

    private double lat;
    private double lng;

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Boolean createNewMarker() {

        Connection connection = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);
            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "INSERT INTO supports.supports (lat, lng) VALUES (" +
                    "'" + lat + "'," + "'" + lng + "');";

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);
            connection.close();

            return true;
        } catch (Exception exc) {
            return false;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
