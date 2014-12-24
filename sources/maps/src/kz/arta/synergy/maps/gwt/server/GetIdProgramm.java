package kz.arta.synergy.maps.gwt.server;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/17/14
 * Time: 12:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetIdProgramm {

    private String synergyid;

    public void setSynergyid(String synergyid) {
        this.synergyid = synergyid;
    }

    public Integer getId() {
        Connection connection = null;
        Integer status = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());


            String query = "SELECT id FROM supports.programms WHERE synergyid = " + "'" + synergyid + "'";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                status = rs.getInt(1);
            }

            return status;

        } catch (Exception exc) {
            return 0;
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
