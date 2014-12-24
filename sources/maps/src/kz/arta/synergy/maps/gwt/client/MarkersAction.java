package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.dev.util.collect.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.maps.gwt.client.*;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import kz.arta.synergy.maps.gwt.server.GetMarkersImpl;
import kz.arta.synergy.maps.gwt.server.ServerGetwayImpl;

import java.util.*;
import java.util.HashMap;

/**
 * Created by root on 27.01.14.
 */
public class MarkersAction {

    public static final String CREATED_MARKER = "images/created.png";
    public static final String NEW_MARKER = "images/new.png";
    public static final String SELECTED_MARKER = "images/selected.png";

    private String selectedMarkerID;

    private HashMap<String, Markers> allMarkers = new HashMap<String, Markers>();
    private HashMap<String, Marker> myMarker = new HashMap<String, Marker>();

    private double moveLat;
    private double moveLng;

    public GoogleMap getMap() {
        return map;
    }

    private GoogleMap map;
    private Geocoder geocoder;

    public Components getComponents() {
        return components;
    }

    private Components components;

    private Marker selMarker;
    private int programmID;
    private boolean canAdd;

    private Button addSupportInProgram;
    private Button removeSupportInProgram;
    private ListBox addingSupports;
    private List<SportObject> sportObjects;

    public List<SportObject> getSportObjects() {
        return sportObjects;
    }

    public void setSportObjects(List<SportObject> sportObjects) {
        this.sportObjects = sportObjects;
    }

    Map<Marker,SportObject> mapMarker = new HashMap<Marker, SportObject>();
    ///////////////////////////////////////////////////////////////////
    List<Mapper> mappers = new ArrayList<Mapper>();

    public List<Mapper> getMappers() {
        return mappers;
    }
    ///////////////////////////////////////////////////////////////////

    public List<String> supportsNames = new ArrayList<String>();
    //координаты опор в списке с опорами
    public List<String> coordinates = new ArrayList<String>();

    public HashMap<String, Markers> getAllMarkers() {
        return allMarkers;
    }

    public void setAllMarkers(HashMap<String, Markers> allMarkers) {
        this.allMarkers = allMarkers;
    }

    public void setCanAdd(boolean canAdd) {
        this.canAdd = canAdd;
    }

    public Marker getSelMarker() {
        return selMarker;
    }

    public void setSelMarker(Marker selMarker) {
        this.selMarker = selMarker;
    }

    public void setSelectedMarkerID(String selectedMarkerID) {
        this.selectedMarkerID = selectedMarkerID;
    }

    public int getProgrammID() {
        return programmID;
    }

    public void setProgrammID(int programmID) {
        this.programmID = programmID;
    }

    public String getSelectedMarkerID() {
        return selectedMarkerID;
    }

    public void setGeocoder(Geocoder geocoder) {
        this.geocoder = geocoder;
    }

    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public void setComponents(Components components) {
        this.components = components;
    }

    public int getSizeMyMarker() {
        return myMarker.size();
    }

    public HashMap<String, Marker> getMyMarker() {
        return myMarker;
    }

    public void clearMap() {

        Set set = myMarker.entrySet();
        Iterator i = set.iterator();

        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            Marker m = (Marker) me.getValue();
            m.setMap((GoogleMap) null);
        }
        myMarker.clear();
    }

    // метод устанавливает опору на карту (на вход принимает координаты, позицию и тип)
    public void setMarker(final double lat, double lng, int id, int type, final boolean canMove) {

        LatLng markerPosition = LatLng.create(lat, lng);

        final MarkerOptions markerOptions = MarkerOptions.create();
        markerOptions.setPosition(markerPosition);
        markerOptions.setMap(map);

        if (canMove) {
            markerOptions.setDraggable(true);
        } else {
            markerOptions.setDraggable(false);
        }

        final Marker marker = Marker.create(markerOptions);
        marker.setTitle(String.valueOf(id));

        if (allMarkers.get(marker.getTitle()).getSynergyID() != null) {

            if (type == 0) {
                marker.setIcon(NEW_MARKER);
            }
            if (type == 1) {
                marker.setIcon(CREATED_MARKER);
            }
            if (type == 2) {
                marker.setIcon(SELECTED_MARKER);
            }

        } else {
            marker.setIcon(NEW_MARKER);
            allMarkers.get(marker.getTitle()).setType(0);
        }

        if (canMove) {
            // что происходит при начале перемещения опоры
            marker.addDragStartListener(new Marker.DragStartHandler() {
                @Override
                public void handle(MouseEvent event) {
                    // запоминаю координаты
                    moveLat = event.getLatLng().lat();
                    moveLng = event.getLatLng().lng();
                }
            });
            // после того как пользователь закончил перемещать опору
            marker.addDragEndListener(new Marker.DragEndHandler() {
                @Override
                public void handle(MouseEvent event) {
                    changePositionSupport(marker);
                }
            });
        }

        marker.addDblClickListener(new Marker.DblClickHandler() {
            @Override
            public void handle(MouseEvent event) {
                LatLng myLatLng = LatLng.create(event.getLatLng().lat(), event.getLatLng().lng());
                map.setCenter(myLatLng);
                map.setZoom(16);
            }
        });

        // что происходит при клике на опору
        marker.addClickListener(new Marker.ClickHandler() {
            @Override
            public void handle(MouseEvent event) {

                setSelectedMarkerID(marker.getTitle());

                components.getAddress().setText(allMarkers.get(marker.getTitle()).getAddress());
                components.getComment().setText(allMarkers.get(marker.getTitle()).getComment());
                components.getNumber().setText(allMarkers.get(marker.getTitle()).getNumber());

                if (selMarker != null) {
                    selMarker.setAnimation(null);
                }

                marker.setAnimation(Animation.BOUNCE);
                selMarker = marker;

                if (allMarkers.get(marker.getTitle()).getType() == 1) {
                    components.getGeneralComponents().setVisible(true);
                    if (canAdd) {
                        addSupports(marker);
                    }
                }

                if (allMarkers.get(marker.getTitle()).getType() == 2) {
                    components.getGeneralComponents().setVisible(true);
                    if (canAdd) {
                        removeSupports(marker);
                    }
                }

                if (allMarkers.get(marker.getTitle()).getType() == 0) {
                    components.getGeneralComponents().setVisible(false);
                    SC.ask("Опора не создана. Создать?", new BooleanCallback() {
                        @Override
                        public void execute(Boolean value) {
                            if (value) {
                                // создаем диалоговое окошко
                                MyDialog myDialog = new MyDialog();
                                myDialog.center();
                                myDialog.show();
                            } else {
                                selMarker.setAnimation(null);
                            }
                        }
                    });
                }
            }
        });

        myMarker.put(marker.getTitle(), marker);
    }
    public void setMarker(final double lat, double lng, String id, int type, final boolean canMove,final SportObject sportObject) {

        LatLng markerPosition = LatLng.create(lat, lng);

        final MarkerOptions markerOptions = MarkerOptions.create();
        markerOptions.setPosition(markerPosition);
        markerOptions.setMap(map);

        if (canMove) {
            markerOptions.setDraggable(true);
        } else {
            markerOptions.setDraggable(false);
        }
        final Marker marker = Marker.create(markerOptions);
        marker.setTitle(id);
        Mapper mapper = new Mapper(marker, sportObject);
        mappers.add(mapper);
//
//        if (allMarkers.get(marker.getTitle()).getSynergyID() != null) {
//
//            if (type == 0) {
//                marker.setIcon(NEW_MARKER);
//            }
//            if (type == 1) {
//                marker.setIcon(CREATED_MARKER);
//            }
//            if (type == 2) {
//                marker.setIcon(SELECTED_MARKER);
//            }
//
//        } else {
//            marker.setIcon(NEW_MARKER);
//            allMarkers.get(marker.getTitle()).setType(0);
//        }
        marker.setIcon(CREATED_MARKER);
        if (canMove) {
            // что происходит при начале перемещения опоры
            marker.addDragStartListener(new Marker.DragStartHandler() {
                @Override
                public void handle(MouseEvent event) {
                    // запоминаю координаты
                    moveLat = event.getLatLng().lat();
                    moveLng = event.getLatLng().lng();
                }
            });
            // после того как пользователь закончил перемещать опору
            marker.addDragEndListener(new Marker.DragEndHandler() {
                @Override
                public void handle(MouseEvent event) {
                    changePositionSupport(marker);
                }
            });
        }

        marker.addDblClickListener(new Marker.DblClickHandler() {
            @Override
            public void handle(MouseEvent event) {
//                LatLng myLatLng = LatLng.create(event.getLatLng().lat(), event.getLatLng().lng());
//                map.setCenter(myLatLng);
//                map.setZoom(16);
                components.redirect(sportObject.getRedirectedUrl());

            }
        });
        marker.addRightClickListener(new Marker.RightClickHandler() {
            @Override
            public void handle(MouseEvent event) {
                //To change body of implemented methods use File | Settings | File Templates.
                SC.ask("Вы действительно хотите удалить привязку спортивного объекта?", new BooleanCallback() {
                    @Override
                    public void execute(Boolean aBoolean) {
                           if(aBoolean){
                               final Mapper mapper = Mapper.findMapperByMarker(marker, mappers);
                               mapper.getSportObject().setCoordX(null);
                               mapper.getSportObject().setCoordY(null);
                               try {
                                   ServerGetwayImpl.App.getInstance().SaveSportObject(mapper.getSportObject(),new AsyncCallback<Void>() {
                                       @Override
                                       public void onFailure(Throwable caught) {
                                           caught.printStackTrace();
                                           SC.say("Не удалось сохранить изменения");
                                           //marker.setPosition(LatLng.create(moveLat, moveLng));
                                       }
                                       @Override
                                       public void onSuccess(Void result) {
                                           mappers.remove(mapper);
                                           marker.setVisible(false);
                                       }
                                   });
                               } catch (Exception e) {
                                   e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                   SC.say("Не удалось сохранить изменения");
                                  // marker.setPosition(LatLng.create(moveLat, moveLng));
                               }


                           }
                    }
                });
            }
        });
        // что происходит при клике на опору
        marker.addClickListener(new Marker.ClickHandler() {
            @Override
            public void handle(MouseEvent event) {
                setSelectedMarkerID(marker.getTitle());

                SportObject so = Mapper.findSportObjectByMarker(marker,mappers);
                if(so!=null){
                    components.getAddress().setText(so.getChiefFIO());
                    components.getComment().setText(so.getAddress());
                    components.getNumber().setText(so.getName());
                    components.getPhoto().setVisible(true);
                    components.getPhoto().setUrl(so.getImageUrl());
                }else{
                    components.getAddress().setText("");
                    components.getComment().setText("");
                    components.getNumber().setText("");
                    components.getPhoto().setVisible(false);
                }
                if (selMarker != null) {
                    selMarker.setAnimation(null);
                    selMarker.setIcon(CREATED_MARKER);
                }
                marker.setAnimation(Animation.BOUNCE);
                marker.setIcon(SELECTED_MARKER);
                selMarker = marker;
                components.getGeneralComponents().setVisible(true);

//                if ( == 1) {
//                    components.getGeneralComponents().setVisible(true);
////                    if (canAdd) {
////                        addSupports(marker);
////                    }
//                }
//
//                if (allMarkers.get(marker.getTitle()).getType() == 2) {
//                    components.getGeneralComponents().setVisible(true);
////                    if (canAdd) {
////                        removeSupports(marker);
////                    }
//                }

//                if (allMarkers.get(marker.getTitle()).getType() == 0) {
//                    components.getGeneralComponents().setVisible(false);
//                    SC.ask("Опора не создана. Создать?", new BooleanCallback() {
//                        @Override
//                        public void execute(Boolean value) {
//                            if (value) {
//                                // создаем диалоговое окошко
//                                MyDialog myDialog = new MyDialog();
//                                myDialog.center();
//                                myDialog.show();
//                            } else {
//                                selMarker.setAnimation(null);
//                            }
//                        }
//                    });
//                }
            }
        });
    }
    private void codeLatLng(final double lat, final double lng, final Marker marker) {
        SC.ask("Вы действительно хотите изменить положение спортивного объекта?", new BooleanCallback() {
            @Override
            public void execute(Boolean aBoolean) {
                if (aBoolean) {
                    final LatLng latLng = LatLng.create(lat, lng);
                    GeocoderRequest request = GeocoderRequest.create();
                    request.setLocation(latLng);
                    geocoder.geocode(request, new Geocoder.Callback() {
                        public void handle(JsArray<GeocoderResult> results, GeocoderStatus status) {
                            if (status == GeocoderStatus.OK) {
                                if (results.length() > 0) {
                                    GeocoderResult geocoderResult = results.get(0);
                                    final StringBuffer sb = new StringBuffer();

                                    JsArray<GeocoderAddressComponent> addressComponents =
                                            geocoderResult.getAddressComponents();
                                    for (int i = 0; i < 3; i++) {
                                        if (i > 0) {
                                            sb.append(", ");
                                        }
                                        sb.append(addressComponents.get(i).getLongName());
                                        SportObject sportObject = Mapper.findSportObjectByMarker(marker,mappers);
                                        sportObject.setCoordX(String.valueOf(marker.getPosition().lat()));
                                        sportObject.setCoordY(String.valueOf(marker.getPosition().lng()));
                                        try {
                                            ServerGetwayImpl.App.getInstance().SaveSportObject(sportObject,new AsyncCallback<Void>() {
                                                @Override
                                                public void onFailure(Throwable caught) {
                                                    caught.printStackTrace();
                                                    SC.say("Не удалось сохранить новые координаты");
                                                    marker.setPosition(LatLng.create(moveLat, moveLng));
                                                }

                                                @Override
                                                public void onSuccess(Void result) {
                                                    //To change body of implemented methods use File | Settings | File Templates.
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                                            SC.say("Не удалось сохранить новые координаты");
                                            marker.setPosition(LatLng.create(moveLat, moveLng));
                                        }
//                                        GetMarkers.App.getInstance().changePosition(Integer.parseInt(marker.getTitle()), marker.getPosition().lat(), marker.getPosition().lng(), sb.toString(), allMarkers.get(marker.getTitle()).getComment(), allMarkers.get(marker.getTitle()).getNumber(), allMarkers.get(marker.getTitle()).getSynergyID(), new AsyncCallback<Boolean>() {
//                                            @Override
//                                            public void onFailure(Throwable caught) {
//                                                Window.alert(caught.getMessage());
//                                            }
//
//                                            @Override
//                                            public void onSuccess(Boolean result) {
//                                                if (result) {
//                                                    allMarkers.get(marker.getTitle()).
//                                                            setLat(marker.getPosition().lat());
//                                                    allMarkers.get(marker.getTitle()).
//                                                            setLng(marker.getPosition().lng());
//                                                    allMarkers.get(marker.getTitle()).
//                                                            setAddress(sb.toString());
//                                                    components.getGeneralComponents().setVisible(false);
//                                                    refreshList();
//                                                } else {
//                                                    Window.alert("error move support");
//                                                    marker.setPosition(LatLng.create(moveLat, moveLng));
//                                                }
//                                            }
//                                        });
                                    }
                                } else {
                                    Window.alert("No results found");
                                }
                            } else {
                                Window.alert("Geocode failed due to: " + status);
                            }
                        }
                    });
                } else {
                    marker.setPosition(LatLng.create(moveLat, moveLng));
                }
            }
        });
    }

    private void changePositionSupport(final Marker marker) {
        codeLatLng(marker.getPosition().lat(), marker.getPosition().lng(), marker);
    }

    private void removeSupports(final Marker marker) {

        if (removeSupportInProgram != null) {
            components.getHorizontal2().remove(removeSupportInProgram);
        }

        if (addSupportInProgram != null) {
            components.getHorizontal2().remove(addSupportInProgram);
        }

        removeSupportInProgram = new Button("Удалить опору");
        removeSupportInProgram.setWidth("150px");
        removeSupportInProgram.setHeight("30px");
        removeSupportInProgram.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent clickEvent) {
                GetMarkersImpl.App.getInstance().removeSupportFromProgramm(Integer.parseInt(
                        marker.getTitle()), programmID, new AsyncCallback<Boolean>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            allMarkers.get(marker.getTitle()).setType(1);
                            getMyMarker().get(marker.getTitle()).setIcon(MarkersAction.CREATED_MARKER);
                            getMyMarker().get(marker.getTitle()).setAnimation(null);
                            components.getGeneralComponents().setVisible(false);
                            refreshList();
                        } else {
                            Window.alert("error delete programm supports");
                        }
                    }
                });
            }
        });
        components.getHorizontal2().add(removeSupportInProgram);
    }

    private void addSupports(final Marker marker) {

        if (removeSupportInProgram != null) {
            components.getHorizontal2().remove(removeSupportInProgram);
        }

        if (addSupportInProgram != null) {
            components.getHorizontal2().remove(addSupportInProgram);
        }

        addSupportInProgram = new Button("Добавить опору");
        addSupportInProgram.setWidth("150px");
        addSupportInProgram.setHeight("30px");
        addSupportInProgram.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent clickEvent) {

                GetMarkersImpl.App.getInstance().addSupportToProgramm(Integer.parseInt(
                        marker.getTitle()), programmID, new AsyncCallback<Boolean>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert(caught.getMessage());
                    }

                    @Override
                    public void onSuccess(Boolean result) {
                        if (result) {
                            allMarkers.get(marker.getTitle()).setType(2);
                            getMyMarker().get(marker.getTitle()).setIcon(MarkersAction.SELECTED_MARKER);
                            getMyMarker().get(marker.getTitle()).setAnimation(null);
                            components.getGeneralComponents().setVisible(false);
                            refreshList();
                        } else {
                            Window.alert("error adding support in programms");
                        }
                    }
                });
            }
        });
        components.getHorizontal2().add(addSupportInProgram);
    }

    private class MyDialog extends DialogBox {

        public MyDialog() {

            setText("Создание новой опоры");
            setAnimationEnabled(true);
            setGlassEnabled(true);

            Label labelNumber = new Label("Инвентарный номер");
            labelNumber.addStyleName("gwt-LabelTextColor");
            Label labelComment = new Label("Комментарий");
            labelComment.addStyleName("gwt-LabelTextColor");
            Label labelAddress = new Label("Адрес");
            labelAddress.addStyleName("gwt-LabelTextColor");

            final TextBox textBoxNumber = new TextBox();
            final TextBox textBoxComment = new TextBox();
            final TextBox textBoxAddress = new TextBox();

            Button ok = new Button("Добавить");
            ok.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
                @Override
                public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {

                    hide();

                    String num = textBoxNumber.getText();
                    String com = textBoxComment.getText();
                    String adr = textBoxAddress.getText();

                    components.getNumber().setText(num);
                    components.getComment().setText(com);
                    components.getAddress().setText(adr);

                    createCreatedMarkers(Integer.parseInt(selectedMarkerID), num, com, adr);
                }
            });

            VerticalPanel panel = new VerticalPanel();
            panel.setHeight("100px");
            panel.setWidth("250px");
            panel.setSpacing(5);
            panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            panel.add(labelNumber);
            panel.add(textBoxNumber);
            panel.add(labelComment);
            panel.add(textBoxComment);
            panel.add(labelAddress);
            panel.add(textBoxAddress);
            panel.add(ok);

            setWidget(panel);
        }
    }

    // метод создает уже созданную опору и обновляет информацию в бд
    private void createCreatedMarkers(Integer id, final String number, final String comment, final String address) {
        GetMarkers.App.getInstance().createCreatedMarker(id, number, comment, address, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(Boolean result) {
                // если запрос прошел успешно - расставляем заного маркеры на карте
                if (result) {

                    getMyMarker().get(getSelectedMarkerID()).setIcon(MarkersAction.CREATED_MARKER);
                    getMyMarker().get(getSelectedMarkerID()).setAnimation(null);

                    allMarkers.get(getSelectedMarkerID()).setType(1);
                    allMarkers.get(getSelectedMarkerID()).setAddress(address);
                    allMarkers.get(getSelectedMarkerID()).setComment(comment);
                    allMarkers.get(getSelectedMarkerID()).setNumber(number);
                }
            }
        });
    }

    // метод создания списка
    public void createListView(List<String> list, final List<String> coordinates) {

        if (addingSupports == null) {
            addingSupports = new ListBox();
            addingSupports.setWidth("400px");
            addingSupports.setHeight("100px");
        }

        addingSupports.clear();

        // заполянем его данными
        for (int i = 0; i < list.size(); i++) {
            addingSupports.addItem(list.get(i));
        }

        addingSupports.setVisibleItemCount(10);
        addingSupports.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {

                String buf = coordinates.get(addingSupports.getSelectedIndex());
                String lat = buf.substring(0, buf.indexOf(":"));
                String lng = buf.substring(buf.indexOf(":") + 1, buf.length());
                map.setCenter(LatLng.create(Double.parseDouble(lat), Double.parseDouble(lng)));
                map.setZoom(18);
            }
        });

        components.listViewLayout.add(addingSupports);

        if (list.size() == 0) {
            addingSupports.setVisible(false);
        } else {
            addingSupports.setVisible(true);
        }
    }

    public void refreshList() {
        clearArrays();
        Set set = getAllMarkers().entrySet();
        Iterator i = set.iterator();

        while (i.hasNext()) {
            Map.Entry me = (Map.Entry) i.next();
            Markers mar = (Markers) me.getValue();
            if (mar.getType() == 2) {
                supportsNames.add(mar.getAddress());
                coordinates.add(mar.getLng() + ":" + mar.getLng());
            }
        }
        createListView(supportsNames, coordinates);
    }

    public void clearArrays() {
        supportsNames.clear();
        coordinates.clear();
    }

    public Marker[] getMarkers() {

        ArrayList<Marker> list = new ArrayList<Marker>();

        for(Map.Entry<String, Marker> entry : myMarker.entrySet()) {
            Marker marker = entry.getValue();
            list.add(marker);
        }

        ArrayList<Marker> buf = new ArrayList<Marker>(myMarker.values());
        Marker[] marker = buf.toArray(new Marker[myMarker.size()]);

        return marker;
    }
}
