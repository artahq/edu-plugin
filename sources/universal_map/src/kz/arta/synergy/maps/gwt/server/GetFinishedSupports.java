package kz.arta.synergy.maps.gwt.server;

import kz.arta.synergy.maps.gwt.client.Markers;

import java.sql.*;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/17/14
 * Time: 1:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetFinishedSupports {

    private int programmID;

    public void setProgrammID(int programmID) {
        this.programmID = programmID;
    }

    public HashMap<String, Markers> getSelectedMarkers() {

        HashMap<String, Markers> markers = new HashMap<String, Markers>();

        Connection connection = null;

        try {

//            String driverName = "com.mysql.jdbc.Driver";
//
//            Class.forName(driverName);
//
//            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());
//
//            String query = "SELECT lat, lng, synergyid, id, address, comment, number FROM supports.supports WHERE id IN " +
//                    " (SELECT support_id FROM supports.programm_supports WHERE programm_id LIKE " + programmID + ");";
//            Statement stmt = connection.createStatement();
//
//            ResultSet rs = stmt.executeQuery(query);
//            try {
//                while (rs.next()) {
//
//                    Markers marker = new Markers();
//                    marker.setId(rs.getInt("id"));
//                    marker.setLng(rs.getDouble("lng"));
//                    marker.setLat(rs.getDouble("lat"));
//                    marker.setSynergyID(rs.getString("synergyid"));
//                    marker.setAddress(rs.getString("address"));
//                    marker.setComment(rs.getString("comment"));
//                    marker.setNumber(rs.getString("number"));
//                    marker.setType(2);
//                    markers.put(String.valueOf(rs.getInt("id")), marker);
//                }
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

            return markers;

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
