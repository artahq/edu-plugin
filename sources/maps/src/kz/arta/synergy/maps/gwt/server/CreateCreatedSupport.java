package kz.arta.synergy.maps.gwt.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
//import kz.arta.synergy.integrations.supports.Configuration;
import sun.security.krb5.Config;

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
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class CreateCreatedSupport {

    private Integer id;
    private String number;
    private String comment;
    private String address;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean createCreatedMarker() {

        boolean res = false;

        String dataUUID = createNewDocument(Configuration.REGISTRY_ID_SUPPORTS);

        if (addDatasInDocument(Configuration.REGISTRY_ID_SUPPORTS, dataUUID, address, comment, number)) {

            Connection connection = null;

            try {

                String driverName = "com.mysql.jdbc.Driver";

                Class.forName(driverName);

                Properties properties = new Properties();
                properties.setProperty("user", MySqlServerParams.getUsername());
                properties.setProperty("password", MySqlServerParams.getPassword());
                properties.setProperty("useUnicode", "true");
                properties.setProperty("characterEncoding", "utf8");
                connection = DriverManager.getConnection(MySqlServerParams.getUrl(), properties);

                String query = "UPDATE supports.supports SET number = '" + number + "', comment = '" + comment +
                        "', address = '" + address + "', synergyid = '" + dataUUID + "' WHERE id = " + id;

                Statement stmt = connection.createStatement();
                stmt.executeUpdate(query);

                connection.close();

                res = true;
            } catch (Exception exc) {
                res = false;
            } finally {
                try {
                    if (connection != null) {
                        connection.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            res = false;
        }

        return res;
    }

    private String createNewDocument(String registryID) {

        String asfDataUUD = null;

        try {

            URL url = new URL(Configuration.getURL() + "/rest/api/registry/create_doc?registryID=" + registryID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Configuration.getAuthHash());

            String output;
            StringBuffer contactsInfo = new StringBuffer();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                contactsInfo.append(output);
            }

            conn.disconnect();

            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createJsonParser(contactsInfo.toString());
            JsonToken token = null;

            while ((token = parser.nextToken()) != null) {
                if (token == JsonToken.FIELD_NAME) {
                    String fieldName = parser.getText();
                    token = parser.nextToken();
                    if (token == JsonToken.VALUE_STRING) {
                        if (fieldName.equals("dataUUID")) {
                            asfDataUUD = parser.getText();
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (asfDataUUD == null) {
            return null;
        } else {
            return asfDataUUD;
        }
    }

    private boolean addDatasInDocument(String formUUID, String asfDataUUD, String address, String comment, String number) {

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
