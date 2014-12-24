package kz.arta.synergy.maps.gwt.server;


import org.apache.catalina.util.Base64;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 08.05.14
 * Time: 12:55
 * To change this template use File | Settings | File Templates.
 */
public class Properties {
    /**
     * UUID формы спортивных объектов для карты
     */
//    public final static String FORM_UUID = "a00172fb-eb4d-473b-b0b3-6b889840c7fa";
//
//    public final static String API_LIST_URL = "/rest/api/asforms/data/list/";
//    public final static String API_SEARCH_URL = "/rest/api/asforms/search";
//    public final static String API_GETDATA_URL = "/rest/api/asforms/data/";
//    public final static String API_GETUSERDATA_URL = "/rest/api/person/auth?getGroups=true";
//    public final static String API_FORMINFO_URL = "/rest/api/asforms/form/";
//    public final static String API_SAVEDATA_URL = "/rest/api/asforms/data/save";
//
//        public final static String API_SOTYPES_DICTIONARY_KEY = "cmp-vid-SO";
//    public final static String API_FEDERATION_DICTIONARY_KEY = "cmp-federaciya";
//    public final static String API_PLACES_DICTIONARY_KEY="cmp-mesto-SO";
//
//    public final static String API_SONAME_DATA_KEY = "cmp-yur-naim-SO";
//    public final static String API_SOTYPE_DATA_KEY = "cmp-vid-SO";
//    public final static String API_SOPLACE_DATA_KEY = "cmp-mesto-SO";
//    public final static String API_SOADDRESS_DATA_KEY = "cmp-address-SO";
//    public final static String API_SOCHIFE_DATA_KEY = "cmp-fio-ruk";
//    public final static String API_SOFEDERATION_DATA_KEY = "cmp-federaciya";
//    public final static String API_SOXCOORD_DATA_KEY = "cmp-k0syuc";
//    public final static String API_SOYCOORD_DATA_KEY = "cmp-lvhn4h";
//
//    public final static  Long LOWER_VERSION = Long.valueOf(2);
//    public final static  Long LOWER_FORM_VERSION = Long.valueOf(4);


    /////////////////////////////////////////////////////////////////

    public final static String FORM_UUID = Configuration.FORM_UUID;

    public final static String API_LIST_URL = Configuration.API_LIST_URL;
    public final static String API_SEARCH_URL = Configuration.API_SEARCH_URL;
    public final static String API_GETDATA_URL = Configuration.API_GETDATA_URL;
    public final static String API_GETUSERDATA_URL = Configuration.API_GETUSERDATA_URL;
    public final static String API_FORMINFO_URL = Configuration.API_FORMINFO_URL;
    public final static String API_SAVEDATA_URL = Configuration.API_SAVEDATA_URL;

    public final static String API_SOTYPES_DICTIONARY_KEY = Configuration.API_SOTYPES_DICTIONARY_KEY;
    public final static String API_FEDERATION_DICTIONARY_KEY = Configuration.API_FEDERATION_DICTIONARY_KEY;
    public final static String API_PLACES_DICTIONARY_KEY=Configuration.API_PLACES_DICTIONARY_KEY;
    public final static String API_ACCRORG_DICTIONARY_KEY=Configuration.API_ACCRORG_DICTIONARY_KEY;

    public final static String API_SONAME_DATA_KEY = Configuration.API_SONAME_DATA_KEY;
    public final static String API_SOTYPE_DATA_KEY = Configuration.API_SOTYPE_DATA_KEY;
    public final static String API_SOPLACE_DATA_KEY = Configuration.API_SOPLACE_DATA_KEY;
    public final static String API_SOADDRESS_DATA_KEY = Configuration.API_SOADDRESS_DATA_KEY;
    public final static String API_SOCHIFE_DATA_KEY = Configuration.API_SOCHIFE_DATA_KEY;
    public final static String API_SOFEDERATION_DATA_KEY = Configuration.API_SOFEDERATION_DATA_KEY;
    public final static String API_SOXCOORD_DATA_KEY = Configuration.API_SOXCOORD_DATA_KEY;
    public final static String API_SOYCOORD_DATA_KEY = Configuration.API_SOYCOORD_DATA_KEY;
    public final static String API_SO_ACCRORG_DATA_KEY = Configuration.API_SO_ACCRORG_DATA_KEY;
    public final static String API_SO_IMAGE_ID = Configuration.API_SO_IMAGE_ID;
    public final static String API_SO_REDIRECT_URL = Configuration.API_SO_REDIRECT_URL;
    public final static String API_SECURITY_FIELD = Configuration.API_SECURITY_FIELD;

    public final static  Long LOWER_VERSION = Configuration.LOWER_VERSION;
    public final static  Long LOWER_FORM_VERSION = Configuration.LOWER_FORM_VERSION;





    public static String getSynergyServerUrl(){
//        try{
            return Configuration.getURL();
//        }catch (Exception e){
//            return "http://test6.arta.kz/Synergy";
//        }
    }
    public static String getAuthAlias(){
//        try{
            return Configuration.getAuthHash();
//        }catch(Exception e){
//            String name = "admin_admin";
//            String password = "turan";
//            String authString = name + ":" + password;
////        System.out.println("auth string: " + authString);
//            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
//            String authStringEnc = new String(authEncBytes);
//            return authStringEnc;
//        }
//        return Configuration.getAuthHash();
    }
    public final static String DEFAULT_SSO_HASH="2NzeRLKwKgi05KurX3GXZN8M";//"IP-YdI8ZHp6VySUYszGLvYNV";
    public static String getSessionAuthAlias(String sso_hash){
//            System.out.println(URLDecoder.decode(sso_hash));
            sso_hash=URLDecoder.decode(sso_hash);
            String name = "$session";
            String password = sso_hash;
            String authString = name + ":" + password;
//        System.out.println("auth string: " + authString);
        byte[] authEncBytes  = Base64.encode(authString.getBytes());
//            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            String authStringEnc = new String(authEncBytes);
            return authStringEnc;

    }
}
