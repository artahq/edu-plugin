package kz.arta.synergy.maps.gwt.server;




import org.apache.commons.codec.binary.Base64;

import java.io.*;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 12.05.14
 * Time: 0:07
 * To change this template use File | Settings | File Templates.
 */
public class Configuration {

    private static String login;

    private static String pass;

    private static String url;

    public static String FORM_UUID_SUPPORTS;
    public static String FORM_UUID_PROGRAMMS;

    public static String REGISTRY_ID_SUPPORTS;
    public static String CONTRACT_FORM_UUID;
    //////////////////////////////////////////////////////
    public  static String FORM_UUID = "a00172fb-eb4d-473b-b0b3-6b889840c7fa";

    public  static String API_LIST_URL = "/rest/api/asforms/data/list/";
    public  static String API_SEARCH_URL = "/rest/api/asforms/search";
    public  static String API_GETDATA_URL = "/rest/api/asforms/data/";
    public  static String API_GETUSERDATA_URL = "/rest/api/person/auth?getGroups=true";
    public  static String API_FORMINFO_URL = "/rest/api/asforms/form/";
    public  static String API_SAVEDATA_URL = "/rest/api/asforms/data/save";

    public  static String API_SOTYPES_DICTIONARY_KEY = "cmp-vid-SO";
    public  static String API_FEDERATION_DICTIONARY_KEY = "cmp-federaciya";
    public  static String API_PLACES_DICTIONARY_KEY="cmp-mesto-SO";

    public  static String API_SONAME_DATA_KEY = "cmp-yur-naim-SO";
    public  static String API_SOTYPE_DATA_KEY = "cmp-vid-SO";
    public  static String API_SOPLACE_DATA_KEY = "cmp-mesto-SO";
    public  static String API_SOADDRESS_DATA_KEY = "cmp-address-SO";
    public  static String API_SOCHIFE_DATA_KEY = "cmp-fio-ruk";
    public  static String API_SOFEDERATION_DATA_KEY = "cmp-federaciya";
    public  static String API_SOXCOORD_DATA_KEY = "cmp-k0syuc";
    public  static String API_SOYCOORD_DATA_KEY = "cmp-lvhn4h";

    public  static String API_SECURITY_FIELD = "cmp-lvhn4h";

    public  static String API_SO_IMAGE_ID;
    public  static String API_SO_REDIRECT_URL;
    public  static String API_ACCRORG_DICTIONARY_KEY;
    public  static String API_SO_ACCRORG_DATA_KEY;
    public  static  Long LOWER_VERSION = Long.valueOf(2);
    public  static  Long LOWER_FORM_VERSION = Long.valueOf(4);

    public static String LABEL_NAME;
    public static String LABEL_ADDRESS;
    public static String LABEL_CHIEF;

    public static FilterConfig[] FILTERS;
    //////////////////////////////////////////////////////////
    static {

        FileInputStream fin = null;
        InputStreamReader inr = null;
        try {

            String configDir = System.getProperty("jboss.server.config.dir");
            File cfgFile = new File(configDir, "arta/integration/supports/config.properties");
//            System.out.println("CFG "+cfgFile.getAbsoluteFile());
            fin = new FileInputStream(cfgFile);
            inr = new InputStreamReader(fin, "UTF-8");
            Properties properties = new Properties();
            properties.load(inr);

            login = properties.get("login").toString();
            pass = properties.get("pass").toString();
            url = properties.get("url").toString();
            ////////////////////////////////////////
            FORM_UUID = properties.get("FORM_UUID").toString();
            API_LIST_URL = properties.get("API_LIST_URL").toString();
            API_SEARCH_URL = properties.get("API_SEARCH_URL").toString();
            API_GETDATA_URL = properties.get("API_GETDATA_URL").toString();
            API_GETUSERDATA_URL = properties.get("API_GETUSERDATA_URL").toString();
            API_FORMINFO_URL = properties.get("API_FORMINFO_URL").toString();
            API_SAVEDATA_URL = properties.get("API_SAVEDATA_URL").toString();
            API_SOTYPES_DICTIONARY_KEY = properties.get("API_SOTYPES_DICTIONARY_KEY").toString();
            API_FEDERATION_DICTIONARY_KEY = properties.get("API_FEDERATION_DICTIONARY_KEY").toString();
            API_PLACES_DICTIONARY_KEY = properties.get("API_PLACES_DICTIONARY_KEY").toString();
            API_ACCRORG_DICTIONARY_KEY = properties.get("API_ACCRORG_DICTIONARY_KEY").toString();

            API_SONAME_DATA_KEY = properties.get("API_SONAME_DATA_KEY").toString();
            API_SOTYPE_DATA_KEY = properties.get("API_SOTYPE_DATA_KEY").toString();
            API_SOPLACE_DATA_KEY = properties.get("API_SOPLACE_DATA_KEY").toString();
            API_SOADDRESS_DATA_KEY = properties.get("API_SOADDRESS_DATA_KEY").toString();
            API_SOCHIFE_DATA_KEY = properties.get("API_SOCHIFE_DATA_KEY").toString();
            API_SOFEDERATION_DATA_KEY = properties.get("API_SOFEDERATION_DATA_KEY").toString();
            API_SOXCOORD_DATA_KEY = properties.get("API_SOXCOORD_DATA_KEY").toString();
            API_SOYCOORD_DATA_KEY = properties.get("API_SOYCOORD_DATA_KEY").toString();
            API_SO_ACCRORG_DATA_KEY = properties.get("API_SO_ACCRORG_DATA_KEY").toString();
            API_SO_IMAGE_ID = properties.get("API_SO_IMAGE_ID").toString();
            API_SO_REDIRECT_URL = properties.get("API_SO_REDIRECT_URL").toString();

            API_SECURITY_FIELD =  properties.get("API_SECURITY_FIELD").toString();

            LOWER_VERSION = Long.valueOf(properties.get("LOWER_VERSION").toString());
            LOWER_FORM_VERSION = Long.valueOf(properties.get("LOWER_FORM_VERSION").toString());

            LABEL_NAME = properties.get("LABEL_NAME").toString();
            LABEL_ADDRESS = properties.get("LABEL_ADDRESS").toString();
            LABEL_CHIEF = properties.get("LABEL_CHIEF").toString();

            //////////////////////////////////////////


            FORM_UUID_SUPPORTS = properties.get("supports_form_uuid").toString();
            FORM_UUID_PROGRAMMS = properties.get("programm_form_uuid").toString();
            REGISTRY_ID_SUPPORTS = properties.get("registry_support").toString();

            FILTERS = FilterConfig.LoadFilters(properties);

        } catch (Exception exc){
//            logger.error("Failed to read configuration", exc);
        } finally {
            try {
                fin.close();
            } catch (IOException e) {
//                logger.error(e.getMessage(), e);
            }
        }
    }

    public static String getAuthHash(){
        try {
            System.out.println(login + ":" + pass);
            return new String(Base64.encodeBase64((login + ":" + pass).getBytes("UTF-8")),"UTF-8");
        } catch (UnsupportedEncodingException e) {
//            logger.error("Failed to get auth hash", e);
            return null;
        }
    }

    public static String getURL(){
        return url;
    }
}
