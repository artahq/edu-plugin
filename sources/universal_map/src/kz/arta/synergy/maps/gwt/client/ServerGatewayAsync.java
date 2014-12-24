package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import kz.arta.synergy.maps.gwt.server.SynergyDataException;

import java.util.List;

public interface ServerGatewayAsync {
    void test(AsyncCallback<Void> async);



    void SaveSportObject(SportObject sportObject, AsyncCallback<Void> async) throws Exception;

    void getPlases(AsyncCallback<List<DictionaryItem>> async) ;



    void getTypes(AsyncCallback<List<DictionaryItem>> async);

    void getSportObjects(String sso_hash, AsyncCallback<List<SportObject>> async);

    void getFederations(String sso_hash, AsyncCallback<List<DictionaryItem>> async);

    void getAccrOrgs(AsyncCallback<List<DictionaryItem>> async);

    void getLabels(AsyncCallback<Labels> async);

    void getFilters(AsyncCallback<List<Filter>> async);
}
