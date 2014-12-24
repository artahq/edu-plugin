package kz.arta.synergy.maps.gwt.client;

import com.google.maps.gwt.client.Marker;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 10.05.14
 * Time: 13:24
 * To change this template use File | Settings | File Templates.
 */
public class Mapper {

    public Mapper(Marker marker, SportObject sportObject) {
        this.marker = marker;
        this.sportObject = sportObject;
    }

    private Marker marker;
    private SportObject sportObject;

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }

    public SportObject getSportObject() {
        return sportObject;
    }

    public void setSportObject(SportObject sportObject) {
        this.sportObject = sportObject;
    }
    public static SportObject findSportObjectByMarker(Marker m, List<Mapper> list){
        for(Mapper mapper:list){
            if(mapper.getMarker()==m){
                return mapper.getSportObject();
            }
        }
        return null;
    }
    public static Mapper findMapperByMarker(Marker m, List<Mapper> list){
        for(Mapper mapper:list){
            if(mapper.getMarker()==m){
                return mapper;
            }
        }
        return null;
    }
}
