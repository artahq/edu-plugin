package kz.arta.synergy.maps.gwt.client;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 10.05.14
 * Time: 18:05
 * To change this template use File | Settings | File Templates.
 */
public class Utils {
    public static SportObject findSOById(List<SportObject> sportsObjects, String id){
        for(SportObject sportObject:sportsObjects ){
            if(sportObject.getDataUUID().equals(id)){
                return sportObject;
            }
        }
        return null;
    }
}
