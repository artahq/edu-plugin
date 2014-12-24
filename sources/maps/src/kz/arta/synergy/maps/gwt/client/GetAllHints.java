package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import kz.arta.synergy.maps.gwt.server.GetMarkersImpl;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 1/20/14
 * Time: 9:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class GetAllHints {

    public MultiWordSuggestOracle getSupportsHints() {
        final MultiWordSuggestOracle supports = new MultiWordSuggestOracle();

        // получаем просто опоры
        GetMarkersImpl.App.getInstance().getSupports(new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                if (result.size() != 0) {
                    for (int i = 0; i < result.size(); i++) {
                        supports.add(result.get(i));
                    }
                }
            }
        });

        return supports;
    }

    public MultiWordSuggestOracle getContractsHints() {
        final MultiWordSuggestOracle contracts = new MultiWordSuggestOracle();

        GetMarkersImpl.App.getInstance().getContracts(new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                if (result.size() != 0) {
                    for (int i = 0; i < result.size(); i++) {
                        contracts.add(result.get(i));
                    }
                }
            }
        });

        return contracts;
    }

    public MultiWordSuggestOracle getProgramms() {

        final MultiWordSuggestOracle programms = new MultiWordSuggestOracle();

        // получаем адресные программы
        GetMarkersImpl.App.getInstance().getProgramms(new AsyncCallback<ArrayList<String>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<String> result) {
                if (result.size() != 0) {
                    for (int i = 0; i < result.size(); i++) {
                        programms.add(result.get(i));
                    }
                }
            }
        });

        return programms;
    }
}
