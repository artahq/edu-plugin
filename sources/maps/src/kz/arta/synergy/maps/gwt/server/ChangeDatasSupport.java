package kz.arta.synergy.maps.gwt.server;



import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/17/14
 * Time: 12:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangeDatasSupport {

    private String number;
    private String comment;
    private String address;
    private String id;
    private String formUUID;
    private String asfDataUUD;

    public void setNumber(String number) {
        this.number = number;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFormUUID(String formUUID) {
        this.formUUID = formUUID;
    }

    public void setAsfDataUUD(String asfDataUUD) {
        this.asfDataUUD = asfDataUUD;
    }

    public void changeDatasInMySql() {

        Connection connection = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            Properties properties = new Properties();
            properties.setProperty("user", MySqlServerParams.getUsername());
            properties.setProperty("password",  MySqlServerParams.getPassword());
            properties.setProperty("useUnicode", "true");
            properties.setProperty("characterEncoding", "utf8");

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), properties);

            String query = "UPDATE supports.supports SET number = '" + number + "', comment = '" + comment + "', address = '" + address + "' WHERE id = " + id;

            Statement stmt = connection.createStatement();
            stmt.executeUpdate(query);

            connection.close();

        } catch (Exception exc) {

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

    public boolean saveDatas() {

        try {

            URL url = new URL(Configuration.getURL() + "/rest/api/asforms/data/save?formUUID=" + formUUID + "&uuid=" + asfDataUUD);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Configuration.getAuthHash());

            OutputStream out = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");

            String str = "\"data\": [";
            str += "{\"id\": \"op_number\" , \"value\": " + "\"" + number + "\"" + "},";
            str += "{\"id\": \"op_addres\" , \"value\": " + "\"" + address + "\"" + "},";
            str += "{\"id\": \"op_comment\" , \"value\": " + "\"" + comment + "\"" + "}";
            str += "]";

            writer.write("data");
            writer.write("=");
            writer.write(URLEncoder.encode(str, "utf-8"));
            writer.write("&");

            writer.close();
            out.close();

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String output;
            StringBuffer result = new StringBuffer();

            while ((output = br.readLine()) != null) {
                result.append(output);
            }

            conn.disconnect();

            if (result.length() != 0) {
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
