package kz.arta.synergy.maps.gwt.server;

import com.google.gwt.codegen.server.StringGenerator;
//import kz.arta.synergy.integrations.supports.Configuration;

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
 * Time: 1:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class ChangePositionSupport {

    private String id;
    private double lat;
    private double lng;
    private String address;
    private String comment;
    private String number;
    private String synergyid;
    private String formUUID;
    private String asfDataUUD;

    public String getSynergyid() {
        return synergyid;
    }

    public void setSynergyid(String synergyid) {
        this.synergyid = synergyid;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAddress() {
        return address;
    }

    public String getAsfDataUUD() {
        return asfDataUUD;
    }

    public void setAsfDataUUD(String asfDataUUD) {
        this.asfDataUUD = asfDataUUD;
    }

    public String getFormUUID() {
        return formUUID;
    }

    public void setFormUUID(String formUUID) {
        this.formUUID = formUUID;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Boolean changePosition() {

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

            String query = "UPDATE supports.supports SET lat=" + lat + ", lng=" + lng + ", address = '"+ address +"' WHERE id = " + id;

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

    public void saveDatas() {

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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
