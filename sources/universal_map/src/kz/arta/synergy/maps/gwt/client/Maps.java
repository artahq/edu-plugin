package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.maps.gwt.client.*;
import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import kz.arta.synergy.maps.gwt.server.GetMarkersImpl;
import com.google.gwt.core.client.JsArray;
import com.google.maps.gwt.client.Geocoder;
import com.google.maps.gwt.client.Geocoder.Callback;
import com.google.maps.gwt.client.GeocoderAddressComponent;
import com.google.maps.gwt.client.GeocoderRequest;
import com.google.maps.gwt.client.GeocoderResult;
import com.google.maps.gwt.client.GeocoderStatus;

import java.util.*;

public class Maps implements EntryPoint {

    @Override
    public void onModuleLoad() {
        createMarkerClusterer();
        Geneal geneal = new Geneal();
        geneal.start(Window.Location.getHref());
    }

    public native void createMarkerClusterer() /*-{
        var markerCluster = null;
//        var markersAction = null;
//        alert("MarkerCluster = "+markerCluster);
    }-*/;
}