package kz.arta.synergy.maps.gwt.server;

//import kz.arta.synergy.integrations.supports.Configuration;
import kz.arta.synergy.maps.gwt.client.ItemInfo;
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
 * Date: 1/20/14
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class GetInfoProgramm {

    private ArrayList<ItemInfo> datas = new ArrayList<ItemInfo>();
    private String id;
    private ArrayList<String> synergyid = new ArrayList<String>();

    public GetInfoProgramm(String id) {
        this.id = id;
    }

    private ArrayList<String> getFormUUID() {

        ArrayList<String> result = new ArrayList<String>();
        Connection connection = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "SELECT * FROM supports.programms WHERE id IN (SELECT programm_id FROM supports.programm_supports WHERE support_id = '" + id + "');";
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            try {
                while (rs.next()) {
                    result.add(rs.getString("dataUUID"));
                    synergyid.add(rs.getString("synergyid"));
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

    public ArrayList<ItemInfo> getDatas() {

        Connection connection = null;
        String res = null;

        try {

            ArrayList<String> formUUIDs = getFormUUID();

            for (int i = 0; i < formUUIDs.size(); i++) {

                URL url = new URL(Configuration.getURL() + "/rest/api/asforms/data/" + formUUIDs.get(i));
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

                ItemInfo itemInfo = new ItemInfo();

                JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());

                System.out.println(genreJsonObject.get("title"));

                JSONArray genreArray = (JSONArray) genreJsonObject.get("data");

                for (int z = 0; z < genreArray.size(); z++) {
                    JSONObject firstGenre = (JSONObject) genreArray.get(z);

                    if (z == 0) {
                        itemInfo.setApNum(firstGenre.get("value").toString());
                    }

                    if (z == 1) {
                        itemInfo.setContractNum(firstGenre.get("value").toString());
                    }

                    if (firstGenre.get("id").toString().equals("contract_name")) { // ap_org_name
                        itemInfo.setArendator(firstGenre.get("value").toString());
                    }

                    if (z == 3) {
                        itemInfo.setStartDate(firstGenre.get("value").toString());
                    }

                    if (z == 4) {
                        itemInfo.setFinishDate(firstGenre.get("value").toString());
                    }

                    if (firstGenre.get("id").toString().equals("ap_device_name")) {
                        itemInfo.setOborud(firstGenre.get("value").toString());
                    }
                }

                itemInfo.setSynergyid(synergyid.get(i));
                itemInfo.setUrl(Configuration.getURL());

                datas.add(itemInfo);
            }

            return datas;

        } catch (Exception e) {
            return null;
        } finally {
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (Exception e) {
                return null;
            }

        }
    }
}
