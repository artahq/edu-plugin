package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@RemoteServiceRelativePath("GetMarkers")
public interface GetMarkers extends RemoteService {

    HashMap<String, Markers> getMarkers(int programmID);
    Boolean createNewMarker(Double lat, Double lng);
    Boolean createCreatedMarker(Integer id, String number, String comment, String address);
    Boolean changePosition(Integer zIndex, Double lat, Double lng, String address, String comment, String number, String synergyid);
    Boolean addSupportToProgramm(int supportID, int programmID);
    Boolean removeSupportFromProgramm(int supportID, int programmID);

    Boolean programmCompleted(int programmID, String countSupports);

    Boolean getStatusProgramm(int programmID);
    HashMap<String, Markers> getSelectedMarkers(int programmID);
    Integer getIdPgoramm (String synergyid);
    HashMap<String, Markers> getAllMarkers();
    HashMap<String, Markers> filterDatas(Date startDate, Date finishDate);
    Boolean changeDataSupp(String number, String comment, String address, String id, String synergyid);

    HashMap<String, Markers> searchSupports(String searchText);
    HashMap<String, Markers> filterSupportsProgramms(String searchText);
    HashMap<String, Markers> filterSupportsDogovors(String searchText);

    ArrayList<String> getSupports();
    ArrayList<String> getProgramms();
    ArrayList<String> getContracts();

    ArrayList<ItemInfo> getInfoProgramms(String id);

    Markers getCenterSelectedSupport(String documentUUID);

    public static class App {
        private static final GetMarkersAsync ourInstance = (GetMarkersAsync) GWT.create(GetMarkers.class);

        public static GetMarkersAsync getInstance() {
            return ourInstance;
        }
    }
}
