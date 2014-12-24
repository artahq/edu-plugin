package kz.arta.synergy.maps.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
//import kz.arta.synergy.integrations.supports.Configuration;
import kz.arta.synergy.maps.gwt.client.GetMarkers;
import kz.arta.synergy.maps.gwt.client.ItemInfo;
import kz.arta.synergy.maps.gwt.client.Markers;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class GetMarkersImpl extends RemoteServiceServlet implements GetMarkers {

    public HashMap<String, Markers> getMarkers(int programmID) {
        GetSupports getMarkers = new GetSupports();
        getMarkers.setProgrammID(programmID);
        return getMarkers.getMarkers();
    }

    public HashMap<String, Markers> getAllMarkers() {
        return new GetAllSupports().getAllMarkers();
    }

    public HashMap<String, Markers> getSelectedMarkers(int programmID) {
        GetFinishedSupports getFinishedSupportsInProgramm = new GetFinishedSupports();
        getFinishedSupportsInProgramm.setProgrammID(programmID);
        return getFinishedSupportsInProgramm.getSelectedMarkers();
    }

    public Boolean createNewMarker(Double lat, Double lng) {
        CreateNewSupport createNewSupport = new CreateNewSupport();
        createNewSupport.setLat(lat);
        createNewSupport.setLng(lng);
        return createNewSupport.createNewMarker();
    }

    public Boolean changePosition(Integer id, Double lat, Double lng, String address, String comment, String number, String synergyid) {
        ChangePositionSupport changePositionSupport = new ChangePositionSupport();
        changePositionSupport.setLat(lat);
        changePositionSupport.setLng(lng);
        changePositionSupport.setFormUUID(Configuration.FORM_UUID_SUPPORTS);
        changePositionSupport.setAsfDataUUD(synergyid);
        changePositionSupport.setId(String.valueOf(id));
        changePositionSupport.setAddress(address);
        changePositionSupport.setComment(comment);
        changePositionSupport.setNumber(number);
        changePositionSupport.saveDatas();
        return changePositionSupport.changePosition();
    }

    public Boolean createCreatedMarker(Integer id, String number, String comment, String address) {
        CreateCreatedSupport createCreatedSupport = new CreateCreatedSupport();
        createCreatedSupport.setId(id);
        createCreatedSupport.setNumber(number);
        createCreatedSupport.setComment(comment);
        createCreatedSupport.setAddress(address);
        return createCreatedSupport.createCreatedMarker();
    }

    public Boolean addSupportToProgramm(int supportID, int programmID) {
        IAddSupport addSupport = new DRSupports();
        addSupport.setProgrammID(programmID);
        addSupport.setSupportID(supportID);
        return addSupport.addSupportToProgramm();
    }

    public Boolean removeSupportFromProgramm(int supportID, int programmID) {
        IRemoveSupport removeSupport = new DRSupports();
        removeSupport.setProgrammID(programmID);
        removeSupport.setSupportID(supportID);
        return removeSupport.removeSupportFromProgramm();
    }

    public Boolean programmCompleted(int programmID, String countSupports) {
        CompleteProgramm completeProgramm = new CompleteProgramm();
        completeProgramm.setProgrammID(programmID);
        completeProgramm.saveDatas(countSupports, Configuration.FORM_UUID_PROGRAMMS);
        return completeProgramm.programmCompleted();
    }

    public Boolean getStatusProgramm(int programmID) {
        GetStatusProgramm getStatusProgramm = new GetStatusProgramm();
        getStatusProgramm.setProgrammID(programmID);
        return getStatusProgramm.getStatus();
    }

    public Integer getIdPgoramm(String synergyid) {
        GetIdProgramm getIdProgramm = new GetIdProgramm();
        getIdProgramm.setSynergyid(synergyid);
        return getIdProgramm.getId();
    }

    public HashMap<String, Markers> searchSupports(String searchText) {

        HashMap<String, Markers> markers = new HashMap<String, Markers>();

        SearchSupports searchSupports = new SearchSupports();
        searchSupports.setSearchText(searchText);
        searchSupports.setFormUUID(Configuration.FORM_UUID_SUPPORTS);

        ArrayList<String> list = searchSupports.getDatasForSuggestBox();

        if (list.size() != 0) {
            markers = searchSupports.getSupport(list);
        }
        return markers;
    }

    public HashMap<String, Markers> filterSupportsProgramms(String searchText) {

        HashMap<String, Markers> markers = new HashMap<String, Markers>();

        SearchSupports searchSupports = new SearchSupports();
        searchSupports.setSearchText(searchText);
        searchSupports.setFormUUID(Configuration.FORM_UUID_PROGRAMMS);

        ArrayList<String> list = searchSupports.getDatasForSuggestBox();

        if (list.size() != 0) {
            markers = searchSupports.getSupportsProgramms(list);
        }
        return markers;
    }

    public HashMap<String, Markers> filterSupportsDogovors(String searchText) {

        HashMap<String, Markers> markers = new HashMap<String, Markers>();

        SearchSupports searchSupports = new SearchSupports();
        searchSupports.setSearchText(searchText);
        searchSupports.setFormUUID(Configuration.CONTRACT_FORM_UUID);

        ArrayList<String> list = searchSupports.getDatasForSuggestBox();

        if (list.size() != 0) {
            markers = searchSupports.getSupportsInDogovor(list);
        }
        return markers;
    }

    public HashMap<String, Markers> filterDatas(Date startDate, Date finishDate) {
        SearchSupports searchSupports = new SearchSupports();
        return searchSupports.getDataContract(startDate, finishDate);
    }

    public Boolean changeDataSupp(String number, String comment, String address, String id, String synergyid) {

        ChangeDatasSupport changeDatasSupport = new ChangeDatasSupport();
        changeDatasSupport.setNumber(number);
        changeDatasSupport.setComment(comment);
        changeDatasSupport.setAddress(address);
        changeDatasSupport.setId(id);
        changeDatasSupport.setAsfDataUUD(synergyid);
        changeDatasSupport.setFormUUID(Configuration.FORM_UUID_SUPPORTS);
        changeDatasSupport.changeDatasInMySql();

        return changeDatasSupport.saveDatas();
    }

    @Override
    public ArrayList<String> getSupports() {
        GetHints hint = new GetHints();
        return hint.getSupports();
    }

    @Override
    public ArrayList<String> getProgramms() {
        GetHints hint = new GetHints();
        return hint.getProgramms();
    }

    @Override
    public ArrayList<String> getContracts() {
        GetHints hint = new GetHints();
        return hint.getContract();
    }

    @Override
    public ArrayList<ItemInfo> getInfoProgramms(String id) {
        GetInfoProgramm getInfoProgramm = new GetInfoProgramm(id);
        return getInfoProgramm.getDatas();
    }

    @Override
    public Markers getCenterSelectedSupport(String documentUUID) {
        CenterSupport centerSupport = new CenterSupport();
        centerSupport.documentID = documentUUID;
        return centerSupport.getCoordinates();
    }
}