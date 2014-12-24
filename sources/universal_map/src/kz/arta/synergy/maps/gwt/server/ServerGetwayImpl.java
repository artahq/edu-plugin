package kz.arta.synergy.maps.gwt.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import kz.arta.synergy.maps.gwt.client.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 08.05.14
 * Time: 13:55
 * To change this template use File | Settings | File Templates.
 */
public class ServerGetwayImpl extends RemoteServiceServlet implements ServerGateway {
    @Override
    public void test() {
        SynergyDataWorker dataWorker = new SynergyDataWorker();
        try {
//            dataWorker.getAllSportObjectsList();
//            dataWorker.getSportObjectTypesList();
//            dataWorker.getFederationsList();
            dataWorker.getPlacesList();
//            System.out.println(dataWorker.getSportObject("51d46455-a9a6-4d5c-842a-07bdfe7199aa"));
        } catch (SynergyDataException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Override
    public List<SportObject> getSportObjects(String sso_hash)  {
        SynergyDataWorker dataWorker = new SynergyDataWorker();
        try {
            return dataWorker.getAllSportObjectsList(sso_hash);
        } catch (SynergyDataException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return null;
        }
    }

    @Override
    public void SaveSportObject(SportObject sportObject) throws Exception {
        SynergyDataWorker dataWorker = new SynergyDataWorker();
        try {
            dataWorker.SaveSportObject(sportObject);
        } catch (SynergyDataException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            throw new Exception(e.getCause());
        }
    }

    @Override
    public List<DictionaryItem> getPlases() throws Exception {
        SynergyDataWorker dataWorker = new SynergyDataWorker();
        return dataWorker.getPlacesList();
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DictionaryItem> getFederations(String sso_hash) throws Exception {
        SynergyDataWorker dataWorker = new SynergyDataWorker();
        return dataWorker.getFederationsList(sso_hash);
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DictionaryItem> getTypes() throws Exception  {
        SynergyDataWorker dataWorker = new SynergyDataWorker();
        return dataWorker.getSportObjectTypesList();
//        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<DictionaryItem> getAccrOrgs() throws Exception {
        SynergyDataWorker dataWorker = new SynergyDataWorker();
        return dataWorker.getAccrOrgList();
    }

    @Override
    public Labels getLabels() {
        Labels labels = new Labels();
        labels.setName(Configuration.LABEL_NAME);
        labels.setAddress(Configuration.LABEL_ADDRESS);
        labels.setChief(Configuration.LABEL_CHIEF);
        return labels;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Filter> getFilters() throws Exception{
        SynergyDataWorker dataWorker = new SynergyDataWorker();
        return dataWorker.getFilters();
    }

}
