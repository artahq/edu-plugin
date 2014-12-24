package kz.arta.synergy.maps.gwt.server;

//import kz.arta.synergy.integrations.supports.Configuration;
import kz.arta.synergy.maps.gwt.client.DictionaryItem;
//import kz.arta.synergy.maps.gwt.client.;
import kz.arta.synergy.maps.gwt.client.SportObject;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 08.05.14
 * Time: 13:23
 * To change this template use File | Settings | File Templates.
 */
public class SynergyDataWorker {

    public List<DictionaryItem> getSportObjectTypesList() throws SynergyDataException {
        List<DictionaryItem> sportObjectTypes = new ArrayList<DictionaryItem>();
//        System.out.println("ENCODING = "+System.getProperty("file.encoding")    );
        try {
            URL url = new URL(Properties.getSynergyServerUrl()+Properties.API_FORMINFO_URL + Properties.FORM_UUID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getAuthAlias());
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
            JSONArray genreArray = (JSONArray) genreJsonObject.get("data");
            for (int i = 0; i < genreArray.size(); i++) {
                if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SOTYPES_DICTIONARY_KEY)) {
//                    System.out.println(((JSONObject)genreArray.get(i)).get("values"));
                    if(((JSONObject)genreArray.get(i)).get("values")!=null){
                        JSONArray items = (JSONArray)((JSONObject)genreArray.get(i)).get("values");
                        for(int j=0;j<items.size();j++){
                            DictionaryItem item = new DictionaryItem();
                            item.setKey((String)((JSONObject)items.get(j)).get("value"));
                            item.setValue((String) ((JSONObject) items.get(j)).get("label"));
                            sportObjectTypes.add(item);
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        }
        return sportObjectTypes;
    }
    public List<DictionaryItem> getFederationsList(String sso_hash) throws SynergyDataException {
        if(sso_hash==null)sso_hash=Properties.DEFAULT_SSO_HASH;
        List<DictionaryItem> federations = new ArrayList<DictionaryItem>();
        try {
            URL url = new URL(Properties.getSynergyServerUrl()+Properties.API_FORMINFO_URL + Properties.FORM_UUID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getAuthAlias());
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
            List<DictionaryItem> userFeds = getFederationUserList(sso_hash);
            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
            JSONArray genreArray = (JSONArray) genreJsonObject.get("data");
            for (int i = 0; i < genreArray.size(); i++) {
                if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_FEDERATION_DICTIONARY_KEY)) {
//                    System.out.println(((JSONObject)genreArray.get(i)).get("values"));
                    if(((JSONObject)genreArray.get(i)).get("values")!=null){
                        JSONArray items = (JSONArray)((JSONObject)genreArray.get(i)).get("values");
                        for(int j=0;j<items.size();j++){
                            DictionaryItem item = new DictionaryItem();
                            item.setKey((String)((JSONObject)items.get(j)).get("value"));
                            item.setValue((String)((JSONObject)items.get(j)).get("label"));
                            if(inFederationList(userFeds,item.getKey())){
                                federations.add(item);
                            }
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        }
        return federations;
    }
    public List<DictionaryItem> getAccrOrgList() throws SynergyDataException {
        List<DictionaryItem> accrorg = new ArrayList<DictionaryItem>();
        try {
            URL url = new URL(Properties.getSynergyServerUrl()+Properties.API_FORMINFO_URL + Properties.FORM_UUID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getAuthAlias());
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
            JSONArray genreArray = (JSONArray) genreJsonObject.get("data");
            for (int i = 0; i < genreArray.size(); i++) {
                if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_ACCRORG_DICTIONARY_KEY)) {
//                    System.out.println(((JSONObject)genreArray.get(i)).get("values"));
                    if(((JSONObject)genreArray.get(i)).get("values")!=null){
                        JSONArray items = (JSONArray)((JSONObject)genreArray.get(i)).get("values");
                        for(int j=0;j<items.size();j++){
                            DictionaryItem item = new DictionaryItem();
                            item.setKey((String)((JSONObject)items.get(j)).get("value"));
                            item.setValue((String)((JSONObject)items.get(j)).get("label"));
                            accrorg.add(item);
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        }
        return accrorg;
    }
    public List<DictionaryItem> getPlacesList() throws SynergyDataException {
        List<DictionaryItem> places = new ArrayList<DictionaryItem>();
        try {
            URL url = new URL(Properties.getSynergyServerUrl()+Properties.API_FORMINFO_URL + Properties.FORM_UUID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getAuthAlias());
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
            JSONArray genreArray = (JSONArray) genreJsonObject.get("data");
            for (int i = 0; i < genreArray.size(); i++) {
                if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_PLACES_DICTIONARY_KEY)) {
//                    System.out.println(((JSONObject)genreArray.get(i)).get("values"));
                    if(((JSONObject)genreArray.get(i)).get("values")!=null){
                        JSONArray items = (JSONArray)((JSONObject)genreArray.get(i)).get("values");
                        for(int j=0;j<items.size();j++){
                            DictionaryItem item = new DictionaryItem();
                            item.setKey((String)((JSONObject)items.get(j)).get("value"));
                            item.setValue((String)((JSONObject)items.get(j)).get("label"));
                            places.add(item);
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        }
        return places;
    }


    public SportObject getSportObject(String dataUUID) throws SynergyDataException {
        URL url = null;
        try {
            url = new URL(Properties.getSynergyServerUrl()+Properties.API_GETDATA_URL + dataUUID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getAuthAlias());
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
//            System.out.println("buffer = "+buffer.toString());
            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
            if(genreJsonObject!=null)
            if(genreJsonObject.containsKey("uuid")&&genreJsonObject.containsKey("data")){
                SportObject so = new SportObject();
                so.setDataUUID((String)genreJsonObject.get("uuid"));
                if(genreJsonObject.containsKey("version"))so.setVersion((Long)genreJsonObject.get("version"));
                if(genreJsonObject.containsKey("form"))so.setFormUUID((String)genreJsonObject.get("form"));
                if(genreJsonObject.containsKey("formVersion"))so.setFormVersion((Long)genreJsonObject.get("formVersion"));
                if(genreJsonObject.containsKey("modified"))so.setModified((String)genreJsonObject.get("modified"));
                if(genreJsonObject.containsKey("data")){
                    JSONArray genreArray = (JSONArray) genreJsonObject.get("data");
                    for(int i=0;i<genreArray.size();i++){
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SONAME_DATA_KEY)){
                            so.setName(((JSONObject)genreArray.get(i)).get("value").toString());
                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SOTYPE_DATA_KEY)){
                            so.setTypeValue((String)((JSONObject)genreArray.get(i)).get("value"));
                            so.setTypeKey((String) ((JSONObject) genreArray.get(i)).get("key"));

                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SOPLACE_DATA_KEY)){
                            so.setPlaceValue(((JSONObject)genreArray.get(i)).get("value").toString());
//                            System.out.println("TypeValue = "+so.getTypeValue());
                            so.setPlaceKey((String) ((JSONObject) genreArray.get(i)).get("key"));
                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SOADDRESS_DATA_KEY)){
                            so.setAddress((String) ((JSONObject) genreArray.get(i)).get("value"));
                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SOCHIFE_DATA_KEY)){
                            so.setChiefFIO((String) ((JSONObject) genreArray.get(i)).get("value"));
                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SOFEDERATION_DATA_KEY)){
                            so.setFederationValue((String) ((JSONObject) genreArray.get(i)).get("value"));
                            so.setFederationKey((String) ((JSONObject) genreArray.get(i)).get("key"));
                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SOXCOORD_DATA_KEY)){
                            if(!((JSONObject) genreArray.get(i)).get("value").equals(""))
                            so.setCoordX((String) ((JSONObject) genreArray.get(i)).get("value"));
                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SOYCOORD_DATA_KEY)){
                            if(!((JSONObject) genreArray.get(i)).get("value").equals(""))
                            so.setCoordY((String) ((JSONObject) genreArray.get(i)).get("value"));
                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SO_ACCRORG_DATA_KEY)){
                            so.setAccrOrgValue((String) ((JSONObject) genreArray.get(i)).get("value"));
                            so.setAccrOrgKey((String) ((JSONObject) genreArray.get(i)).get("key"));
                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SO_IMAGE_ID)){
                            so.setImageId((String) ((JSONObject) genreArray.get(i)).get("value"));

                        }
                        if(((JSONObject)genreArray.get(i)).get("id").equals(Properties.API_SO_REDIRECT_URL)){
                            so.setRedirectUrl((String) ((JSONObject) genreArray.get(i)).get("value"));
                        }
                    }
                }
                return so;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        }
        return null;
    }
    public List<SportObject> getAllSportObjectsList(String sso_hash) throws SynergyDataException {
        if(sso_hash==null)sso_hash=Properties.DEFAULT_SSO_HASH;
        List<SportObject> sportObjects = new ArrayList<SportObject>();
        URL url = null;
        try {
//            url = new URL(Properties.getSynergyServerUrl()+Properties.API_LIST_URL + Properties.FORM_UUID);
            url = new URL(Properties.getSynergyServerUrl()+Properties.API_SEARCH_URL+"?"+getUserFederationList(sso_hash)+"showDeleted=false");
//            System.out.println(url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getAuthAlias());
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
            JSONArray genreJsonObject = (JSONArray) JSONValue.parse(buffer.toString());
            for(int i=0;i<genreJsonObject.size();i++){
                SportObject so = getSportObject((String)genreJsonObject.get(i));
                if(so!=null&&so.getVersion()>=Properties.LOWER_VERSION&&so.getFormVersion()>=Properties.LOWER_FORM_VERSION){
                    sportObjects.add(so);
                }
            }
//            System.out.println(genreJsonObject);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        }
        return sportObjects;
    }
    public List<SportObject> getSportObjectsWithCoordinatsList(String sso_hash) throws SynergyDataException {
        if(sso_hash==null)sso_hash=Properties.DEFAULT_SSO_HASH;
        List<SportObject> sportObjects = getAllSportObjectsList(sso_hash);
        for(int i=0;i<sportObjects.size();i++){
            if(sportObjects.get(i).getCoordX()==null||sportObjects.get(i).getCoordY()==null){
                sportObjects.remove(i);
                i--;
            }
        }
        return sportObjects;
    }
    public List<SportObject> getSportObjectsWithCoordinatsList(List<SportObject> sportObjects) throws SynergyDataException {
        List<SportObject> sportObjects2 = new ArrayList<SportObject>();
        for(int i=0;i<sportObjects.size();i++){
            if(sportObjects.get(i).getCoordX()!=null||sportObjects.get(i).getCoordY()!=null){
                sportObjects2.add(sportObjects.get(i));
            }
        }
        return sportObjects2;
    }
    private JSONArray Data(String dataUUID) throws SynergyDataException {
        URL url = null;
        try {
            url = new URL(Properties.getSynergyServerUrl()+Properties.API_GETDATA_URL + dataUUID);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getAuthAlias());
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
//            System.out.println("buffer = "+buffer.toString());
            System.out.println("buffer = "+buffer.toString());



            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
            if(genreJsonObject!=null)
                if(genreJsonObject.containsKey("uuid")&&genreJsonObject.containsKey("data")){
                    return (JSONArray)genreJsonObject.get("data");
                }
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error getting data from Synergy", e);
        }
        return null;
    }
    public void SaveSportObject(SportObject sportObject) throws SynergyDataException {
        try {
            System.out.println("dataUUID: "+sportObject.getDataUUID());

            URL url = new URL(Properties.getSynergyServerUrl() + Properties.API_SAVEDATA_URL +"?formUUID=" + sportObject.getFormUUID() + "&uuid=" + sportObject.getDataUUID());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setAllowUserInteraction(false);
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getAuthAlias());
            OutputStream out = conn.getOutputStream();
            Writer writer = new OutputStreamWriter(out, "UTF-8");
            if(sportObject.getCoordX()==null)sportObject.setCoordX("");
            if(sportObject.getCoordY()==null)sportObject.setCoordY("");
            JSONArray dataArray = Data(sportObject.getDataUUID());
            if(dataArray==null){
                throw new SynergyDataException("Save error object is not in synergy");
            }
            String str = "\"data\": [";
              for(int i=0;i<dataArray.size();i++){
                  JSONObject field = (JSONObject)dataArray.get(i);
                  if(!field.get("id").equals(Properties.API_SOXCOORD_DATA_KEY)||!field.get("id").equals(Properties.API_SOYCOORD_DATA_KEY)) {
                      str += "{\"id\": \""+field.get("id")+"\" , \"type\":\""+field.get("type")+"\",";// \"value\": " + "\"" + sportObject.getTypeValue() + "\"" + " , \"key\": "+"\""+sportObject.getTypeKey()+"\""+"},";
                      if(field.get("type").equals("listbox")){
                           str+="\"value\": " + "\"" + field.get("value").toString().replaceAll("\\\\","\\\\\\\\").replaceAll("\"","\\\\\"") + "\"" + " , \"key\": "+"\""+field.get("key")+"\""+"},";
                      } else{
                           str+="\"value\": " + "\"" + field.get("value").toString().replaceAll("\\\\","\\\\\\\\").replaceAll("\"","\\\\\"")  + "\"" + "},";
                      }
                  }
              }
//            str += "{\"id\": \""+Properties.API_SONAME_DATA_KEY+"\" , \"type\":\"textbox\", \"value\": " + "\"" + sportObject.getName() + "\"" + "},";
//            str += "{\"id\": \""+Properties.API_SOTYPE_DATA_KEY+"\" , \"type\":\"listbox\", \"value\": " + "\"" + sportObject.getTypeValue() + "\"" + " , \"key\": "+"\""+sportObject.getTypeKey()+"\""+"},";
//            str += "{\"id\": \""+Properties.API_SOPLACE_DATA_KEY+"\" , \"type\":\"listbox\", \"value\": " + "\"" + sportObject.getPlaceValue() + "\"" + " , \"key\": "+"\""+sportObject.getPlaceKey()+"\""+"},";
//            str += "{\"id\": \""+Properties.API_SOADDRESS_DATA_KEY+"\" , \"type\":\"textbox\", \"value\": " + "\"" + sportObject.getAddress() + "\"" + "},";
//            str += "{\"id\": \""+Properties.API_SOCHIFE_DATA_KEY+"\" , \"type\":\"textbox\", \"value\": " + "\"" + sportObject.getChiefFIO() + "\"" + "},";
//            str += "{\"id\": \""+Properties.API_SOFEDERATION_DATA_KEY+"\" , \"type\":\"listbox\", \"value\": " + "\"" + sportObject.getFederationValue() + "\"" + " , \"key\": "+"\""+sportObject.getFederationKey()+"\""+"},";
//            str += "{\"id\": \""+Properties.API_SO_ACCRORG_DATA_KEY+"\" , \"type\":\"listbox\", \"value\": " + "\"" + sportObject.getAccrOrgValue() + "\"" + " , \"key\": "+"\""+sportObject.getAccrOrgKey()+"\""+"},";
            str += "{\"id\": \""+Properties.API_SOXCOORD_DATA_KEY+"\" , \"type\":\"label\", \"value\": " + "\"" + sportObject.getCoordX() + "\"" + "},";
            str += "{\"id\": \""+Properties.API_SOYCOORD_DATA_KEY+"\" ,  \"type\":\"label\", \"value\": " + "\"" + sportObject.getCoordY() + "\"" + "}";
            str += "]";
            System.out.println(str);
            writer.write("data");
            writer.write("=");
            writer.write(URLEncoder.encode(str, "utf-8"));
            writer.write("&form=");
            writer.write(Properties.FORM_UUID);
            writer.write("&uuid=");
            writer.write(sportObject.getDataUUID());
            writer.close();
            System.out.println(str);
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
            if(result==null||result.length()==0){
                throw new RuntimeException("Failed : response error: " + result);
            }
//            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(result.toString());
//            if(!genreJsonObject.get("errorCode").equals("0")){
//                throw new RuntimeException("Failed : response error: " + genreJsonObject.get("errorCode "));
//            }
            System.out.println(result);
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error save data to Synergy", e);
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error save data to Synergy", e);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new SynergyDataException("Error save data to Synergy", e);
        }
    }
    private String getUserFederationList(String sso_hash){
        if(sso_hash==null)sso_hash=Properties.DEFAULT_SSO_HASH;
        try {
            System.out.println("SSO_HASH = "+sso_hash);
            URL url = new URL(Properties.getSynergyServerUrl()+Properties.API_GETUSERDATA_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getSessionAuthAlias(sso_hash));
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
            JSONArray genreArray = (JSONArray) genreJsonObject.get("groups");
            String groups = "";
            for(int i=0;i<genreArray.size();i++){
                String substr;
                if(i==0){
//                    System.out.println(((JSONObject)genreArray.get(i)).get("name").toString());
                    substr="formUUID="+Properties.FORM_UUID+"&search="+ java.net.URLEncoder.encode(((JSONObject)genreArray.get(i)).get("name").toString())+"&field="+Properties.API_SOFEDERATION_DATA_KEY+"&type=exact&";

                }else{
                    substr="formUUID"+i+"="+Properties.FORM_UUID+"&search"+i+"="+java.net.URLEncoder.encode(((JSONObject)genreArray.get(i)).get("name").toString())+"&field"+i+"="+Properties.API_SOFEDERATION_DATA_KEY+"&type"+i+"=exact&";
                }
//                System.out.println(((JSONObject)genreArray.get(i)).get("name").toString());
                groups+=substr;
            }
            return groups;
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
    private boolean inFederationList(List<DictionaryItem> list,String id){
        if(list!=null){
              for(DictionaryItem item:list){
                  if(item.getKey().equals(id)){
                      return true;
                  }
              }
            return false;
        }else{
            return false;
        }
    }
    private List<DictionaryItem> getFederationUserList(String sso_hash){
        if(sso_hash==null)sso_hash=Properties.DEFAULT_SSO_HASH;
        try {
            System.out.println("SSO_HASH = "+sso_hash);
            URL url = new URL(Properties.getSynergyServerUrl()+Properties.API_GETUSERDATA_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json; charset=utf-8");
            conn.setRequestProperty("Authorization", "Basic " + Properties.getSessionAuthAlias(sso_hash));
            String output;
            StringBuffer buffer = new StringBuffer();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) {
                buffer.append(output);
            }
            conn.disconnect();
            JSONObject genreJsonObject = (JSONObject) JSONValue.parse(buffer.toString());
            JSONArray genreArray = (JSONArray) genreJsonObject.get("groups");
            List<DictionaryItem> feds = new ArrayList<DictionaryItem>();
            for(int i=0;i<genreArray.size();i++){
                DictionaryItem item = new DictionaryItem();
                item.setKey(((JSONObject)genreArray.get(i)).get("groupID").toString());
                item.setValue(((JSONObject) genreArray.get(i)).get("name").toString());
                feds.add(item);
            }
            return feds;
        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ProtocolException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }
}
