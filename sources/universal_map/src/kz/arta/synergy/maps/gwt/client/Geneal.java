package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.maps.gwt.client.*;
import com.google.maps.gwt.client.MouseEvent;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import kz.arta.synergy.maps.gwt.server.GetMarkersImpl;
import kz.arta.synergy.maps.gwt.server.ServerGetwayImpl;

import java.io.UnsupportedEncodingException;

import java.util.*;



/**
 * Created by root on 27.01.14.
 */
public class Geneal {

    private boolean filter = false;
    // маркер при поиске
    private Marker searchMarker;
    // для обратного геокодирования
    private Geocoder geocoder;
    // сама карта
    private GoogleMap map;
    // тут компоненты
    private Components components;
    // тут действия с опорами
    private MarkersAction markersAction;
    //
    String sso_hash=null;
    private List<SportObject> sportsObjects;
    private List<DictionaryItem> sportObjectTypes;
    private List<DictionaryItem> sportObjectPlases;
    private List<DictionaryItem> federations;
    public void start(String host) {
        if(host.contains("sso_hash=")) {
            int start =  host.lastIndexOf("sso_hash=")+9;
            int end = host.substring(start,host.length()).lastIndexOf("&")+start;
            if(end==-1)end = host.length();
            sso_hash = host.substring(start , end);
//            Window.alert(sso_hash);
//            sso_hash="Lvbv%252BdfU2FZtfAKtIgzSzsSu";
//            if(sso_hash!=null)sso_hash = com.google.gwt.http.client.URL.encode(sso_hash);
        }
//        sso_hash="Lvbv%252BdfU2FZtfAKtIgzSzsSu";
        //if(sso_hash!=null)sso_hash = com.google.gwt.http.client.URL.encode(sso_hash);
        components = new Components(sso_hash);
        markersAction = new MarkersAction();
        components.setMarkersAction(markersAction);

        if (host.contains("programmID=")) {
            host = host.substring(host.lastIndexOf("programmID=") + 11, host.lastIndexOf("programmID=") + 47);
            getProgrammID(host);
        } else {
            createMap();
            components.createComponent();
            markersAction.setCanAdd(false);
            createHeadPanel(false);
            setAllMarkersOnMap(host);
            ////////////////////

        }
    }

    private void setCenterMap(final String host) {
        GetMarkersImpl.App.getInstance().getCenterSelectedSupport(host, new AsyncCallback<Markers>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(Markers markers) {
                if (markers != null) {
                    LatLng zoomMarker = LatLng.create(markers.getLat(), markers.getLng());
                    map.setCenter(zoomMarker);
                    map.setZoom(18);
                    if (markersAction.getSelMarker() != null) {
                        markersAction.getSelMarker().setAnimation(null);
                    }
                    markersAction.getMyMarker().get(String.valueOf(markers.getId())).setAnimation(Animation.BOUNCE);
                    markersAction.setSelMarker(markersAction.getMyMarker().get(String.valueOf(markers.getId())));
                    components.getGeneralComponents().setVisible(true);
                    markersAction.setSelectedMarkerID(String.valueOf(markers.getId()));
                    components.getNumber().setText(markers.getNumber());
                    components.getAddress().setText(markers.getAddress());
                    components.getComment().setText(markers.getComment());
                    components.getPhoto().setVisible(true);
//                    components.getPhoto().setUrl(markers.get);
                } else {
                    Window.alert(host);
                }
            }
        });
    }

    private void getProgrammID(String host) {
        GetMarkersImpl.App.getInstance().getIdPgoramm(host, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(Integer result) {
                if (result != 0) {
                    markersAction.setProgrammID(result);
                    // создаем карту
                    createMap();
                    // создаем нужные компоненты
                    components.createComponent();
                    // вызываем метод загрузки опор в адресную программу
                    getStatusProgramm();
                } else {
                    Window.alert("Не могу получить id адресной программы");
                }
            }
        });
    }

    private void createMap() {
        LatLng myLatLng = LatLng.create(49.803669, 73.122728);
       // LatLng myLatLng = LatLng.create(59.940224, 30.308533);
//        LatLng myLatLng = LatLng.create(48.0000, 68.0000);
        MapOptions myOptions = MapOptions.create();
        myOptions.setZoom(5);
        myOptions.setCenter(myLatLng);
        myOptions.setMapTypeId(MapTypeId.ROADMAP);

        geocoder = Geocoder.create();

        map = GoogleMap.create(Document.get().getElementById("map_canvas"), myOptions);

        // клик по карте

        map.addClickListener(new GoogleMap.ClickHandler() {
            @Override
            public void handle(MouseEvent event) {

                // скрываем леяут
                components.getGeneralComponents().setVisible(false);
//                SC.say("EVENT = "+event.getClass().toString());
//                System.out.println("EVENT!!!!!!!!!!!!!!!!!!");
                markersAction.getSelMarker().setAnimation(null);
               // markersAction.getSelMarker()
            }
        });

        map.addRightClickListener(new GoogleMap.RightClickHandler() {
            @Override
            public void handle(final MouseEvent event) {
                SC.ask("Привязать к точке объект?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean value) {
                        if (value) {
                            createNewMarker(event.getLatLng().lat(), event.getLatLng().lng());
                        }
                    }
                });
            }
        });

        markersAction.setMap(map);
        markersAction.setComponents(components);
        markersAction.setGeocoder(geocoder);
        components.setMap(map);
    }

    private void getStatusProgramm() {
        // проверяем статус у адресной программы
        GetMarkersImpl.App.getInstance().getStatusProgramm(markersAction.getProgrammID(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                // создаем верхнюю панель
                createHeadPanel(!result);
                // если данная адресная программа завершенная
                if (result) {
                    markersAction.setCanAdd(false);
                    // то показываем опоры которые в ней находятся
                    getSelectedMarkers();
                } else {
                    markersAction.setCanAdd(true);
                    // иначе пробегаемся по карте и расставляем опоры. данные берутся из бд таблица supports
                    setMarkersOnMap(markersAction.getProgrammID());
                }
            }
        });
    }

    // верхняя панель
    private void createHeadPanel(boolean status) {

        final HorizontalPanel headPanel = new HorizontalPanel();

        Button finish = new Button(" Готово ");
        finish.setStyleName("gwt-ButtonSearch");
        finish.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                endEdit();
            }
        });

        final Label label = new Label("Адресная программа");
        label.setWordWrap(false);

        final TextBox textbox = new TextBox();
        textbox.setWidth("300px");
        textbox.setHeight("25px");
        textbox.setStyleName("gwt-TextBoxSearch");
        textbox.addKeyPressHandler(new KeyPressHandler() {
            @Override
            public void onKeyPress(KeyPressEvent event) {
                int key = event.getNativeEvent().getKeyCode();
                if (key == KeyCodes.KEY_ENTER) {
                    searchSupport(textbox.getText());
                }
            }
        });

        headPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);

        if (status) {
            headPanel.add(finish);
        }

        Image openFilter = new Image("images/filter_button_active.png");
        openFilter.setSize("25px", "25px");
        openFilter.setUrl("images/filter_button_active.png");
        openFilter.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (!filter) {
                    Document.get().getElementById("map_canvas").getStyle().setWidth(Window.getClientWidth()-300, Style.Unit.PX);
                    Document.get().getElementById("filter").getStyle().setVisibility(Style.Visibility.VISIBLE);
                    components.setHeightScroll();
                    filter = true;
                } else {
                    Document.get().getElementById("map_canvas").getStyle().setWidth(Window.getClientWidth(), Style.Unit.PX);
                    Document.get().getElementById("filter").getStyle().setVisibility(Style.Visibility.HIDDEN);
                    filter = false;
                }
            }
        });

        headPanel.add(label);
        headPanel.add(textbox);
        headPanel.setCellWidth(textbox, "3000px");
        headPanel.add(openFilter);
        RootPanel.get("head_panel").add(headPanel);
    }

    // метод вызываетмся при успешном завершении редактирования опор
    private void endEdit() {
        SC.ask("Вы действительно хотите завершить редактирование опор?", new BooleanCallback() {
            @Override
            public void execute(Boolean value) {
                if (value) {
                    if (markersAction.supportsNames.size() != 0) {
                        GetMarkersImpl.App.getInstance().programmCompleted(markersAction.getProgrammID(),
                                String.valueOf(markersAction.supportsNames.size()), new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                Window.alert(caught.getMessage() + " net...");
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                if (result != null) {
                                    Window.Location.reload();
                                }
                            }
                        });
                    } else {
                        Window.alert("no supports");
                    }
                }
            }
        });
    }

    // метод грузит только опоры находящиеся в данной адресной программе
    private void getSelectedMarkers() {

        GetMarkersImpl.App.getInstance().getSelectedMarkers(markersAction.getProgrammID(), new AsyncCallback<HashMap<String, Markers>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(HashMap<String, Markers> result) {
                if (result.size() != 0) {
                    // то что вернул сервер суем в allMarkers
                    markersAction.setAllMarkers(result);
                    // очищаем карту
                    markersAction.clearMap();
                    // очищаем список опор в ListBox'e
                    markersAction.clearArrays();
                    // пробегаемся по хешмапе и расставляем опоры
                    Set set = result.entrySet();
                    Iterator i = set.iterator();

                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry) i.next();
                        Markers mar = (Markers) me.getValue();
                        markersAction.setMarker(mar.getLat(), mar.getLng(), mar.getId(), mar.getType(), false);

                        if (mar.getType() == 2) {
                            markersAction.supportsNames.add(mar.getAddress());
                            markersAction.coordinates.add(mar.getLat() + ":" + mar.getLng());
                        }
                    }
                    // если есть опоры в адресной программе - создаем ListBox
                    if (markersAction.supportsNames.size() != 0) {
                        markersAction.createListView(markersAction.supportsNames, markersAction.coordinates);
                    }
                }
            }
        });
    }

    // нативный метод для кластеризации
    public native void setMarkerCluster(GoogleMap map, Marker[] arr) /*-{
        var mcOptions = {gridSize: 70, maxZoom: 15};
//        alert("MarkerCluster2 = "+$wnd);
        try{
        markerCluster = new $wnd.MarkerClusterer(map, arr, mcOptions);
        }catch(e){
            alert(e);
        }
//       alert("MarkerCluster3 = ");
    }-*/;

    private void setAllMarkersOnMap(final String host) {
//        ServerGetwayImpl.App.getInstance().test(new AsyncCallback<Void>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                //To change body of implemented methods use File | Settings | File Templates.
//                Window.alert(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Void result) {
//                //To change body of implemented methods use File | Settings | File Templates.
//            }
//        });

        ServerGetwayImpl.App.getInstance().getSportObjects(sso_hash,new AsyncCallback<List<SportObject>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(List<SportObject> result) {
                sportsObjects = result;

                if(sportsObjects!=null&&sportsObjects.size()>0){
                    markersAction.setSportObjects(sportsObjects);
//                    markersAction.setAllMarkers(result);

                    markersAction.clearMap();
                    for(SportObject so:sportsObjects){
                        if(so.getCoordX()!=null&&so.getCoordY()!=null){
//                            try {
//                                SC.say(new String(so.getName().getBytes("ISO-8859-1"),"UTF-8"));
//                            } catch (UnsupportedEncodingException e) {
//                                SC.say(e.getMessage());
//                                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
//                            }
                            markersAction.setMarker(Double.valueOf(so.getCoordX()),Double.valueOf(so.getCoordY()),so.getTypeValue()+" \""+so.getName()+"\"",0,false,so);
                        }
                    }
                    ArrayList<Marker> buf = new ArrayList<Marker>();
                    for(int i=0;i<markersAction.getMappers().size();i++){
                        buf.add(markersAction.getMappers().get(i).getMarker());
                    }
                    Marker[] arraySupports = (Marker[])buf.toArray(new Marker[buf.size()]);
                    try{
                        setMarkerCluster(map, arraySupports);
                        ClusterWrapper.setJavaScriptData(markersAction);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    ///////////////////////
                }
            }
        });
//        GetMarkersImpl.App.getInstance().getAllMarkers(new AsyncCallback<HashMap<String, Markers>>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                Window.alert(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(HashMap<String, Markers> result) {
//
//                if (result.size() != 0) {
//                    markersAction.setAllMarkers(result);
//                    markersAction.clearMap();
//
//                    Set set = result.entrySet();
//                    Iterator i = set.iterator();
//
//                    while (i.hasNext()) {
//                        Map.Entry me = (Map.Entry) i.next();
//                        Markers mar = (Markers) me.getValue();
//                        markersAction.setMarker(mar.getLat(), mar.getLng(), mar.getId(), mar.getType(), true);
//                    }
//
//                    ArrayList<Marker> buf = new ArrayList<Marker>(markersAction.getMyMarker().values());
//                    Marker[] arraySupports = buf.toArray(new Marker[markersAction.getSizeMyMarker()]);
//                    setMarkerCluster(map, arraySupports);
//                    String url = null;
//                    if (host.contains("supportID=")) {
//                        url = host.substring(host.lastIndexOf("supportID=") + 10, host.length());
//                        setCenterMap(url);
//                    }
//                }
//            }
//        });
    }

    // метод пробегается по меткам в таблице и расставляет их по карте
    private void setMarkersOnMap(int id) {

        // асинхронный запрос
        GetMarkers.App.getInstance().getMarkers(id, new AsyncCallback<HashMap<String, Markers>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(HashMap<String, Markers> result) {
                if (result.size() != 0) {

                    markersAction.setAllMarkers(result);
                    markersAction.clearMap();
                    markersAction.clearArrays();

                    Set set = result.entrySet();
                    Iterator i = set.iterator();

                    while (i.hasNext()) {
                        Map.Entry me = (Map.Entry) i.next();
                        Markers mar = (Markers) me.getValue();
                        markersAction.setMarker(mar.getLat(), mar.getLng(), mar.getId(), mar.getType(), true);

                        if (mar.getType() == 2) {
                            markersAction.supportsNames.add(mar.getAddress());
                            markersAction.coordinates.add(mar.getLat() + ":" + mar.getLng());
                        }
                    }

                    ArrayList<Marker> buf = new ArrayList<Marker>(markersAction.getMyMarker().values());
                    Marker[] arraySupports = buf.toArray(new Marker[markersAction.getSizeMyMarker()]);

                    setMarkerCluster(map, arraySupports);

                    if (markersAction.supportsNames.size() != 0) {
                        markersAction.createListView(markersAction.supportsNames, markersAction.coordinates);
                    }
                }
            }
        });
    }

    // метод создает новую опору и добавляет ее в таблицу
    private void createNewMarker(final double lat, final double lng) {
          BindSODialogBox dialog = new BindSODialogBox(sportsObjects,lat,lng,markersAction);
        dialog.center();
        dialog.show();

//        GetMarkers.App.getInstance().createNewMarker(lat, lng, new AsyncCallback<Boolean>() {
//            @Override
//            public void onFailure(Throwable caught) {
//                Window.alert(caught.getMessage());
//            }
//
//            @Override
//            public void onSuccess(Boolean result) {
//                // если запрос прошел успешно - расставляем заного маркеры на карте
//                if (result) {
//
//                    Markers markers = new Markers();
//                    markers.setId(markersAction.getAllMarkers().size());
//                    markers.setLat(lat);
//                    markers.setLng(lng);
//                    markers.setType(0);
//
//                    markersAction.getAllMarkers().put(String.valueOf(markersAction.getAllMarkers().size() + 1), markers);
//                    markersAction.setMarker(lat, lng, markersAction.getAllMarkers().size(), 0, true);
//                }
//            }
//        });
    }

    private void searchSupport(String text) {

        GeocoderRequest request = GeocoderRequest.create();
        request.setAddress(text);
        geocoder.geocode(request, new Geocoder.Callback() {
            public void handle(JsArray<GeocoderResult> results, GeocoderStatus status) {
                if (status == GeocoderStatus.OK) {
                    GeocoderResult location = results.get(0);
                    map.setCenter(location.getGeometry().getLocation());
                    map.setZoom(14);

                    if (searchMarker == null) {
                        MarkerOptions markerOptions = MarkerOptions.create();
                        markerOptions.setPosition(location.getGeometry().getLocation());
                        markerOptions.setMap(map);
                        searchMarker = Marker.create(markerOptions);
                    } else {
                        searchMarker.setPosition(location.getGeometry().getLocation());
                    }

                } else {
                    Window.alert("Ничего не нашлось");
                }
            }
        });
    }
}
