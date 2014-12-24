package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import kz.arta.synergy.maps.gwt.server.SynergyDataException;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 08.05.14
 * Time: 13:51
 * To change this template use File | Settings | File Templates.
 */
@RemoteServiceRelativePath("ServerGateway")
public interface ServerGateway extends RemoteService {
    public void test();
    public List<SportObject> getSportObjects(String sso_hash);
    public void SaveSportObject(SportObject sportObject) throws Exception;
    public List<DictionaryItem> getPlases() throws  Exception;
    public List<DictionaryItem> getFederations(String sso_hash) throws  Exception;
    public List<DictionaryItem> getTypes() throws Exception;
    public List<DictionaryItem> getAccrOrgs() throws Exception;
    public Labels getLabels();
    public static class App {
        private static final ServerGatewayAsync ourInstance = (ServerGatewayAsync) GWT.create(ServerGateway.class);

        public static ServerGatewayAsync getInstance() {
            return ourInstance;
        }
    }
}
