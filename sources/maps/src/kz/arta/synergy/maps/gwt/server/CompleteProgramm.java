package kz.arta.synergy.maps.gwt.server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
//import kz.arta.synergy.integrations.supports.Configuration;
import org.omg.CosNaming._NamingContextExtStub;
import sun.management.resources.agent_fr;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.*;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/17/14
 * Time: 1:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class CompleteProgramm implements Serializable {

    private int programmID;

    public void setProgrammID(int programmID) {
        this.programmID = programmID;
    }

    public Boolean programmCompleted() {
        Connection connection = null;
        Statement stmt = null;

        String executionID = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());

            String query = "SELECT executionID FROM supports.programms WHERE id=" + programmID + " ";

            stmt = connection.createStatement();

            ResultSet res = stmt.executeQuery(query);

            if (res.next()) {
                executionID = res.getString(1);
            }

            if (executionID == null) {
                return false;
            }

            boolean isSuccess = false;

            try {
                URL url = new URL(Configuration.getURL() + "/rest/api/processes/signal?signal=got_agree&executionID=" + executionID +
                        "&param1=resolution&value1=completed");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json; charset=utf-8");
                conn.setRequestProperty("Authorization", "Basic " + Configuration.getAuthHash());

                String output;
                StringBuffer result = new StringBuffer();

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                while ((output = br.readLine()) != null) {
                    result.append(output);
                }

                conn.disconnect();
                System.out.println(result.toString());

                JsonFactory factory = new JsonFactory();
                JsonParser parser = factory.createJsonParser(result.toString());
                JsonToken token = null;

                while ((token = parser.nextToken()) != null) {
                    if (token == JsonToken.FIELD_NAME) {
                        System.out.println();
                        System.out.print(parser.getText() + " -> ");
                        String fieldName = parser.getText();
                        token = parser.nextToken();
                        System.out.print(parser.getText());
                        if (fieldName.equals("errorCode") && parser.getText().equals("0")) {
                            isSuccess = true;
                        }
                    }
                }
            } catch (Exception exc) {
                return false;
            }

            if (!isSuccess) {
                return false;
            }

            stmt.execute("UPDATE supports.programms SET status=2 WHERE id=" + programmID + ";");
            return true;
        } catch (Exception exc) {
            return false;
        } finally {
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private String getJsonData() {
        try {
            URL url = new URL(Configuration.getURL() + "/rest/api/asforms/data/" + getAsf());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Configuration.getAuthHash());

            String output;
            StringBuffer result = new StringBuffer();

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            while ((output = br.readLine()) != null) {
                result.append(output);
            }

            return result.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String parseJson(String count) {
        String asfData = getJsonData();
        asfData = asfData.substring(asfData.indexOf("data")-1, asfData.length()-2);
        String countOKS = ",{\"id\": \"op_count\" , \"value\": " + "\"" + count + "\"" + "}]";
        return asfData.concat(countOKS);
    }


    public void saveDatas(String count, String formUUID) {

        try {

            URL url = new URL(Configuration.getURL() + "/rest/api/asforms/data/save?formUUID=" + formUUID + "&uuid=" + getAsf());
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

            String str = parseJson(count);

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

    private String getAsf() {

        Connection connection = null;
        String dataUUID = null;

        try {

            String driverName = "com.mysql.jdbc.Driver";

            Class.forName(driverName);

            connection = DriverManager.getConnection(MySqlServerParams.getUrl(), MySqlServerParams.getUsername(), MySqlServerParams.getPassword());


            String query = "SELECT dataUUID from supports.programms where id = " + programmID;
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                dataUUID = rs.getString(1);
            }

            return dataUUID;

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
