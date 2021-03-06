package kz.arta.synergy.maps.gwt.server;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
//import kz.arta.synergy.integrations.supports.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/14/14
 * Time: 3:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GetHints {

    public ArrayList<String> getProgramms() {

        ArrayList<String> result = new ArrayList<String>();
        Connection connection = null;
        String res = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "SELECT dataUUID from supports.programms;";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            try {
                while (rs.next()) {

                    String dataUUID = rs.getString("dataUUID");

                    URL url = new URL(Configuration.getURL() + "/rest/api/asforms/data/" + dataUUID);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json; charset=utf-8");
                    conn.setRequestProperty("Authorization", "Basic " + Configuration.getAuthHash());

                    String output;
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    while ((output = br.readLine()) != null) {
                        buffer.append(output);
                    }
                    conn.disconnect();

                    JsonElement jelement3 = new com.google.gson.JsonParser().parse(buffer.toString());
                    JsonObject jobject3 = jelement3.getAsJsonObject();
                    JsonArray jarray3 = jobject3.getAsJsonArray("data");

                    res = jarray3.toString().substring(jarray3.toString().indexOf("value")+8, jarray3.toString().indexOf("}")-1);

                    result.add(res);
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                return null;
            }

            return result;

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

    public ArrayList<String> getSupports() {
        ArrayList<String> result = new ArrayList<String>();
        Connection connection = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "SELECT address, number FROM supports.supports";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            try {
                while (rs.next()) {
                    result.add("" + rs.getString("address"));
                    result.add("" + rs.getString("number"));
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }

            return result;

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

    public ArrayList<String> getContract() {

        ArrayList<String> result = new ArrayList<String>();
        Connection connection = null;
        String res = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "SELECT DISTINCT contract_synergyid FROM supports.programms ORDER BY contract_synergyid;";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            try {
                while (rs.next()) {

                    String contract = rs.getString("contract_synergyid");

                    URL url = new URL(Configuration.getURL() + "/rest/api/asforms/data/" + contract);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json; charset=utf-8");
                    conn.setRequestProperty("Authorization", "Basic " + Configuration.getAuthHash());

                    String output;
                    StringBuffer buffer = new StringBuffer();
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    while ((output = br.readLine()) != null) {
                        buffer.append(output);
                    }
                    conn.disconnect();

                    JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
                    JSONArray genreArray = (JSONArray) genreJsonObject.get("data");
                    for (int z = 0; z < genreArray.size(); z++) {
                        JSONObject firstGenre = (JSONObject) genreArray.get(z);
                        if (z == 0) {
                            res = firstGenre.get("value").toString();
                        }
                    }

                    result.add(res);
                }
            } catch (Exception exc) {
                exc.printStackTrace();
                return null;
            }

            return result;

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
