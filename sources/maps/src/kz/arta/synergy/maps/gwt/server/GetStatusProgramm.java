package kz.arta.synergy.maps.gwt.server;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/17/14
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetStatusProgramm {

    public void setProgrammID(int programmID) {
        this.programmID = programmID;
    }

    private int programmID;

    public Boolean getStatus() {
        Connection connection = null;
        String status = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());


            String query = "SELECT status from supports.programms where id = " + programmID;
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                status = rs.getString(1);
            }

            if (status.equals("2")) {
                return true;
            } else {
                return false;
            }
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
