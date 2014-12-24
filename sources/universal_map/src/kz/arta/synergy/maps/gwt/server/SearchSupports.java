package kz.arta.synergy.maps.gwt.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
//import kz.arta.synergy.integrations.supports.Configuration;
import kz.arta.synergy.maps.gwt.client.Markers;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/10/14
 * Time: 2:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchSupports {

    private String searchText;
    private String formUUID;

    public void setSearchText(String searchText) {
        this.searchText = searchText;
    }

    public void setFormUUID(String formUUID) {
        this.formUUID = formUUID;
    }

    public ArrayList<String> getDatasForSuggestBox() {

        ArrayList<String> oracle = new ArrayList<String>();

        try {

            URL url = new URL(Configuration.getURL() + "/rest/api/asforms/search?formUUID=" +
                    formUUID + "&search=" + URLEncoder.encode(searchText, "utf-8") + "&type=exact");
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

            JsonFactory factory = new JsonFactory();
            JsonParser parser = factory.createJsonParser(buffer.toString());
            JsonToken token = null;

            while ((token = parser.nextToken()) != null) {
                if (token == JsonToken.VALUE_STRING) {
                    oracle.add(parser.getText());
                }
            }

            return oracle;

        } catch (Exception exc) {
            return null;
        }
    }

    public HashMap<String, Markers> getSupport(ArrayList<String> uuids) {

        HashMap<String, Markers> markers = new HashMap<String, Markers>();
        Connection connection = null;

        try {

            for (int i = 0; i < uuids.size(); i++) {

                String driverName = "com.mysql.jdbc.Driver";

                Class.forName(driverName);

                connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

                String query = "SELECT * FROM supports.supports WHERE synergyid = " + "\"" + uuids.get(i) + "\"";
                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery(query);
                try {
                    while (rs.next()) {

                        Markers marker = new Markers();
                        marker.setId(rs.getInt("id"));
                        marker.setLng(rs.getDouble("lng"));
                        marker.setLat(rs.getDouble("lat"));
                        marker.setSynergyID(rs.getString("synergyid"));
                        marker.setAddress(rs.getString("address"));
                        marker.setComment(rs.getString("comment"));
                        marker.setNumber(rs.getString("number"));
                        marker.setType(1);
                        markers.put(String.valueOf(rs.getInt("id")), marker);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            return markers;

        } catch (Exception e) {
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

    public HashMap<String, Markers> getSupportsProgramms(ArrayList<String> uuids) {

        HashMap<String, Markers> markers = new HashMap<String, Markers>();

        Connection connection = null;

        try {

            for (int i = 0; i < uuids.size(); i++) {
                String driverName = "com.mysql.jdbc.Driver";

                Class.forName(driverName);

                connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

                String query = "SELECT * FROM supports.supports WHERE id IN " +
                        "(SELECT support_id FROM supports.programm_supports WHERE programm_id IN " +
                        "(SELECT id FROM supports.programms WHERE dataUUID = '" + uuids.get(i) + "'));";

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery(query);
                try {
                    while (rs.next()) {

                        Markers marker = new Markers();
                        marker.setId(rs.getInt("id"));
                        marker.setLng(rs.getDouble("lng"));
                        marker.setLat(rs.getDouble("lat"));
                        marker.setSynergyID(rs.getString("synergyid"));
                        marker.setAddress(rs.getString("address"));
                        marker.setComment(rs.getString("comment"));
                        marker.setNumber(rs.getString("number"));
                        marker.setType(1);
                        markers.put(String.valueOf(rs.getInt("id")), marker);

                    }
                } catch (SQLException e) {
                    return null;
                }
            }
        } catch (Exception exc) {
            return null;
        }

        return markers;
    }

    public HashMap<String, Markers> getSupportsInDogovor(ArrayList<String> uuids) {

        HashMap<String, Markers> markers = new HashMap<String, Markers>();

        Connection connection = null;

        try {

            for (int i = 0; i < uuids.size(); i++) {
                String driverName = "com.mysql.jdbc.Driver";

                Class.forName(driverName);

                connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

                String query = "SELECT lat, lng, synergyid, id, address, comment, number FROM supports.supports " +
                        "WHERE id IN (SELECT support_id FROM supports.programm_supports WHERE " +
                        "programm_id IN (SELECT id FROM supports.programm_supports WHERE id " +
                        "IN (SELECT id FROM supports.programms WHERE contract_synergyid = '" + uuids.get(i) + "')));";

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery(query);
                try {
                    while (rs.next()) {

                        Markers marker = new Markers();
                        marker.setId(rs.getInt("id"));
                        marker.setLng(rs.getDouble("lng"));
                        marker.setLat(rs.getDouble("lat"));
                        marker.setSynergyID(rs.getString("synergyid"));
                        marker.setAddress(rs.getString("address"));
                        marker.setComment(rs.getString("comment"));
                        marker.setNumber(rs.getString("number"));
                        marker.setType(1);
                        markers.put(String.valueOf(rs.getInt("id")), marker);

                    }
                } catch (SQLException e) {
                    return null;
                }
            }

        } catch (Exception exc) {
            return null;
        }

        return markers;
    }

    private ArrayList<String> getDataUUIDProgramms(Date startDate, Date finishDate) {

        Connection connection = null;
        ArrayList<String> contractUUID = new ArrayList<String>();

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "SELECT DISTINCT contract_synergyid FROM supports.programms ORDER BY contract_synergyid";

            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

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

                String sDate = null;
                String fDate = null;

                for (int z = 0; z < genreArray.size(); z++) {
                    JSONObject firstGenre = (JSONObject) genreArray.get(z);

                    if (firstGenre.get("id").toString().equals("d_date_start")) {
                        sDate = firstGenre.get("value").toString();
                    }

                    if (firstGenre.get("id").toString().equals("d_date_finish")) {
                        fDate = firstGenre.get("value").toString();
                    }
                }

                Date startServerDate = setDate(sDate);
                Date finisServerDate = setDate(fDate);

                if (startDate.before(startServerDate) && finishDate.after(finisServerDate)) {
                    contractUUID.add(contract);
                }
            }

            return contractUUID;

        } catch (Exception exc) {
            exc.printStackTrace();
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

    private String getRequest(ArrayList<String> arr) {

        String str = "";
        String query = "SELECT lat, lng, synergyid, id, address, comment, number FROM supports.supports " +
                "WHERE id IN (SELECT support_id FROM supports.programm_supports WHERE " +
                "programm_id IN (SELECT id FROM supports.programm_supports WHERE id " +
                "IN (SELECT id FROM supports.programms WHERE ";

        if (arr.size() == 0) {
            query = "null";
        } else {
            if (arr.size() == 1) {
                str = "contract_synergyid = '" + arr.get(0) + "'";
            } else {
                for (int i = 0; i < arr.size(); i++) {
                    str += "contract_synergyid = '" + arr.get(i) + "' or ";
                }
                str = str.substring(0, str.length()-4);
            }
            query += str + ")));";
        }
        return query;
    }

    private HashMap<String, Markers> getFilteredDatasSupports(String request) {
        HashMap<String, Markers> markers = new HashMap<String, Markers>();

        Connection connection = null;

        try {
                String driverName = "com.mysql.jdbc.Driver";

                Class.forName(driverName);

                connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

                Statement stmt = connection.createStatement();

                ResultSet rs = stmt.executeQuery(request);
                try {
                    while (rs.next()) {

                        Markers marker = new Markers();
                        marker.setId(rs.getInt("id"));
                        marker.setLng(rs.getDouble("lng"));
                        marker.setLat(rs.getDouble("lat"));
                        marker.setSynergyID(rs.getString("synergyid"));
                        marker.setAddress(rs.getString("address"));
                        marker.setComment(rs.getString("comment"));
                        marker.setNumber(rs.getString("number"));
                        marker.setType(1);
                        markers.put(String.valueOf(rs.getInt("id")), marker);
                    }
                } catch (SQLException e) {
                    return null;
                }

        } catch (Exception exc) {
            return null;
        }

        return markers;
    }

    public HashMap<String, Markers> getDataContract(Date startDate, Date finishDate) {
        ArrayList<String> arr = getDataUUIDProgramms(startDate, finishDate);
        String request = getRequest(arr);
        HashMap<String, Markers> hashMap = getFilteredDatasSupports(request);
        return hashMap;
    }

    private Date setDate(String str) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}