package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public interface GetMarkersAsync {

    void getMarkers(int programmID, AsyncCallback<HashMap<String, Markers>> async);
    void createCreatedMarker(Integer id, String number, String comment, String address, AsyncCallback<Boolean> async);
    void createNewMarker(Double lat, Double lng, AsyncCallback<Boolean> async);
    void changePosition(Integer zIndex, Double lat, Double lng, String address, String comment, String number, String synergyid, AsyncCallback<Boolean> async);
    void addSupportToProgramm(int supportID, int programmID, AsyncCallback<Boolean> async);
    void removeSupportFromProgramm(int supportID, int programmID, AsyncCallback<Boolean> async);
    void programmCompleted(int programmID, String countSupports, AsyncCallback<Boolean> async);
    void getStatusProgramm(int programmID, AsyncCallback<Boolean> async);
    void getSelectedMarkers(int programmID, AsyncCallback<HashMap<String, Markers>> async);
    void getIdPgoramm(String synergyid, AsyncCallback<Integer> async);
    void getAllMarkers(AsyncCallback<HashMap<String, Markers>> async);
    void searchSupports(String searchText, AsyncCallback<HashMap<String, Markers>> async);
    void filterSupportsProgramms(String searchText, AsyncCallback<HashMap<String, Markers>> async);
    void filterSupportsDogovors(String searchText, AsyncCallback<HashMap<String, Markers>> async);
    void getSupports(AsyncCallback<ArrayList<String>> async);
    void getProgramms(AsyncCallback<ArrayList<String>> async);
    void getContracts(AsyncCallback<ArrayList<String>> async);
    void changeDataSupp(String number, String comment, String address, String id, String synergyid, AsyncCallback<Boolean> async);
    void getInfoProgramms(String id, AsyncCallback<ArrayList<ItemInfo>> async);
    void filterDatas(Date startDate, Date finishDate, AsyncCallback<HashMap<String, Markers>> async);
    void getCenterSelectedSupport(String documentUUID, AsyncCallback<Markers> async);
}

