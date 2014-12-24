package kz.arta.synergy.maps.gwt.server;

import com.google.maps.gwt.client.Marker;
import kz.arta.synergy.maps.gwt.client.Markers;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by root on 31.01.14.
 */
public class CenterSupport {

    public String documentID;

    private String getDataUUID() {

        String dataUUID = null;
        Connection connection = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "select asfDataID from synergy.registry_documents where documentID = '" + documentID +"'";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                dataUUID = rs.getString("asfDataID");
            }

            return dataUUID;

        } catch (Exception exc) {
            return null;
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

    public Markers getCoordinates() {

        Markers marker = null;
        Connection connection = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "select * from supports.supports where synergyid = '" + getDataUUID() + "'";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                marker = new Markers();
                marker.setId(rs.getInt("id"));
                marker.setLng(rs.getDouble("lng"));
                marker.setLat(rs.getDouble("lat"));
                marker.setSynergyID(rs.getString("synergyid"));
                marker.setAddress(rs.getString("address"));
                marker.setComment(rs.getString("comment"));
                marker.setNumber(rs.getString("number"));
                marker.setType(1);
            }

            return marker;

        } catch (Exception exc) {
            return null;
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
