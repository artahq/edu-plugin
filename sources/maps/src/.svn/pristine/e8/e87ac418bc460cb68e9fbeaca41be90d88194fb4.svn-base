package kz.arta.synergy.maps.gwt.server;

import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/17/14
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class DRSupports implements IRemoveSupport, IAddSupport {

    private int supportID;
    private int programmID;

    public void setSupportID(int supportID) {
        this.supportID = supportID;
    }

    public void setProgrammID(int programmID) {
        this.programmID = programmID;
    }

    public Boolean addSupportToProgramm() {

        Connection connection = null;
        Statement st = null;
        ResultSet res = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            st = connection.createStatement();

            res = st.executeQuery("SELECT COUNT(*) FROM supports.programm_supports WHERE programm_id=" + programmID + " " +
                    " AND support_id=" + supportID + " AND deleted IS NULL ");
            if (res.next() && res.getInt(1) > 0) {
                return true;
            }

            st.execute("INSERT INTO supports.programm_supports(programm_id, support_id, created) VALUES " +
                    " (" + programmID + ", " + supportID + ", now()) ");

            return true;
        } catch (Exception exc) {
            exc.printStackTrace();
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

    public Boolean removeSupportFromProgramm() {
        Connection connection = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "DELETE FROM supports.programm_supports WHERE programm_id=" + programmID + " " + " AND support_id=" + supportID + " ";

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);

            return true;
        } catch (Exception exc) {
            exc.printStackTrace();
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
