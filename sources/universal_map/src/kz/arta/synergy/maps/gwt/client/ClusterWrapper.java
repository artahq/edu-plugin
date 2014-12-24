package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.maps.gwt.client.Animation;
import com.google.maps.gwt.client.Marker;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 11.05.14
 * Time: 3:43
 * To change this template use File | Settings | File Templates.
 */
public class ClusterWrapper extends DialogBox {

    public static ClusterWrapper getInstance(MarkersAction markersAction, Marker[] markers) {
//        SC.say("MarkersCount Create = "+markers.length);
        return new ClusterWrapper(markersAction,markers);
    }

    private ClusterWrapper(final MarkersAction markersAction, Marker[] markers) {
//        SC.say("MarkersCount Create");
//        if(markers==null){
//            SC.say("Markers is null");
//        }  else{
//            SC.showPrompt("Markers is " + markers);
//        }
//        SC.say("MarkersCount="+markersAction.getMappers().size());
        setText("Выбор объекта");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        setWidth("650px");
        setHeight("500px");
        CellTable<Contact> table = new CellTable<Contact>();
        table.setWidth("100%");
        TextColumn<Contact> typeColumn = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact contact) {
                return contact.type;
            }
        };
        typeColumn.setCellStyleNames("so_types_th");
        TextColumn<Contact> countColumn = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact contact) {
                return String.valueOf(contact.count);
            }
        };
        table.addColumn(typeColumn, "ВИД объекта");
        table.addColumn(countColumn, "Количество");
        List<Mapper> mappers = markersAction.getMappers();
        final List<Contact> contracts = new ArrayList<Contact>();
        for(int i=0;i<markers.length;i++){
           Mapper mapper = Mapper.findMapperByMarker(markers[i],mappers);
           SportObject so = mapper.getSportObject();
           Contact contract = findContractByType(contracts,so.getTypeKey());
            if(contract==null){
                contract = new Contact(so.getTypeValue(),0,new ArrayList<Mapper>(),so.getTypeKey());
                contracts.add(contract);
            }
            contract.setCount(contract.getCount()+1);
            contract.getMappers().add(mapper);
        }
        ListDataProvider<Contact> dataProvider = new ListDataProvider<Contact>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(table);
        table.setVisibleRange(0,contracts.size());
        // Add the data to the data provider, which automatically pushes it to the
        // widget.
        List<Contact> list = dataProvider.getList();
        for(Contact contact:contracts){
            list.add(contact);
        }
        ///////////////////////////////////////////
        final CellTable<Contact> table2 = new CellTable<Contact>();
        final ListDataProvider<Contact> dataProvider2 = new ListDataProvider<Contact>();
        dataProvider2.addDataDisplay(table2);
        final VerticalPanel panel2 = new VerticalPanel();
        panel2.setWidth("600px");
//        panel2.setSpacing(0);
        ////////////////////////////////////////////
        final NoSelectionModel<Contact> selectionModel = new NoSelectionModel<Contact>();
        selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Contact contract = selectionModel.getLastSelectedObject();
//                SC.say("Cell click row ="+contract.getCount());
                if(contract.getCount()==1){
//                    SC.showPrompt("Cell click one");
                    Marker marker = contract.getMappers().get(0).getMarker();
                    markersAction.getMap().setCenter(marker.getPosition());
                    markersAction.getMap().setZoom(15);
                    panel2.setVisible(false);
                    ////////////////////////////////
                    SportObject so = contract.getMappers().get(0).getSportObject();
                    if(so!=null){
                        markersAction.getComponents().getAddress().setText(so.getChiefFIO());
                        markersAction.getComponents().getComment().setText(so.getAddress());
                        markersAction.getComponents().getNumber().setText(so.getName());
                        markersAction.getComponents().getPhoto().setVisible(true);
                        markersAction.getComponents().getPhoto().setUrl(so.getImageUrl());
                    }else{
                        markersAction.getComponents().getAddress().setText("");
                        markersAction.getComponents().getComment().setText("");
                        markersAction.getComponents().getNumber().setText("");
                        markersAction.getComponents().getPhoto().setVisible(false);
                    }
                    if (markersAction.getSelMarker() != null) {
                        markersAction.getSelMarker().setAnimation(null);
                        markersAction.getSelMarker().setIcon(markersAction.CREATED_MARKER);
                    }
                    marker.setAnimation(Animation.BOUNCE);
                    marker.setIcon(markersAction.SELECTED_MARKER);
                    markersAction.setSelMarker(marker);
                    markersAction.getComponents().getGeneralComponents().setVisible(true);
                    /////////////////////////////////////
                    hide();
                }else{
//                    SC.showPrompt("Cell click else");
                    List<Mapper>mappers = contract.getMappers();
                    List<Contact> contracts = new ArrayList<Contact>();
                    for(Mapper mapper:mappers){
                        Contact c = new Contact(mapper.getSportObject().getName(),1,new ArrayList<Mapper>(),"");
                        c.getMappers().add(mapper);
                        contracts.add(c);
                    }
                    dataProvider2.setList(contracts);
                    table2.setVisibleRange(0,contracts.size());
                    dataProvider2.refresh();
                    panel2.setVisible(true);
                }
            }
        });
        table.setSelectionModel(selectionModel);
        ///////////////////////////////////////////
        final NoSelectionModel<Contact> selectionModel2 = new NoSelectionModel<Contact>();
        selectionModel2.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Contact contract = selectionModel2.getLastSelectedObject();
//               SC.showPrompt("Cell click row selectionmodal2");
                    Marker marker = contract.getMappers().get(0).getMarker();
                    markersAction.getMap().setCenter(marker.getPosition());
                    markersAction.getMap().setZoom(15);
                    ////////////////////////////////
                SportObject so = contract.getMappers().get(0).getSportObject();
                if(so!=null){
                    markersAction.getComponents().getAddress().setText(so.getChiefFIO());
                    markersAction.getComponents().getComment().setText(so.getAddress());
                    markersAction.getComponents().getNumber().setText(so.getName());
                    markersAction.getComponents().getPhoto().setVisible(true);
                    markersAction.getComponents().getPhoto().setUrl(so.getImageUrl());

                }else{
                    markersAction.getComponents().getAddress().setText("");
                    markersAction.getComponents().getComment().setText("");
                    markersAction.getComponents().getNumber().setText("");
                    markersAction.getComponents().getPhoto().setVisible(false);
                }
                if (markersAction.getSelMarker() != null) {
                    markersAction.getSelMarker().setAnimation(null);
                    markersAction.getSelMarker().setIcon(markersAction.CREATED_MARKER);
                }
                marker.setAnimation(Animation.BOUNCE);
                marker.setIcon(markersAction.SELECTED_MARKER);
                markersAction.setSelMarker(marker);
                markersAction.getComponents().getGeneralComponents().setVisible(true);
                /////////////////////////////////////
                    panel2.setVisible(false);
                    hide();

            }
        });
        table2.setSelectionModel(selectionModel2);
        table2.setWidth("99%");
        //////////////////////////////////
        Button cancel = new Button("Отмена");
        cancel.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event){
                hide();
            }
        });


//        Label label = new Label("Выберите спортивный объект:");
//        label.addStyleName("gwt-LabelTextColor");

        TextColumn<Contact> soColumn = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact contact) {
                return contact.type;
            }
        };
        soColumn.setCellStyleNames("so_types_th");
//        soColumn.setHorizontalAlignment(HasHorizontalAlignment.HorizontalAlignmentConstant.);
        table2.addColumn(soColumn,"Выберите объект");
//        panel2.add(label);
        panel2.add(table2);
        VerticalPanel panel = new VerticalPanel();
        panel2.setVisible(false);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel.setSpacing(5);
        panel.setHeight("500px");
        panel.setWidth("600px");
        panel.add(table);
        panel.add(panel2);
        panel.add(cancel);

        ScrollPanel scrollPanel = new ScrollPanel(panel);
        scrollPanel.setHeight("500px");
        scrollPanel.setWidth("650px");
        setWidget(scrollPanel);
//        center();
//        show();
//        center();
    }
    public static void Center(DialogBox box){
        box.show();
        box.center();
    }
    public static native void setJavaScriptData(MarkersAction markersActionObject) /*-{
        $wnd.clusterClickHandler = $entry(@kz.arta.synergy.maps.gwt.client.ClusterWrapper::getInstance(Lkz/arta/synergy/maps/gwt/client/MarkersAction;[Lcom/google/maps/gwt/client/Marker;));
//        alert('function seted') ;
        $wnd.clusterDialogShow = $entry(@kz.arta.synergy.maps.gwt.client.ClusterWrapper::Center(Lcom/google/gwt/user/client/ui/DialogBox;));
        $wnd.markersAction =  markersActionObject;
//        alert('marker setted  = '+markersAction) ;

    }-*/;

    private static Contact findContractByType(List<Contact> contacts, String typeKey){
        for(Contact contact:contacts){
            if(contact.getTypeKey().equals(typeKey)){
                return contact;
            }
        }
        return null;
    }
    private static class Contact {
        private final String type;

        public String getTypeKey() {
            return typeKey;
        }

        private final String typeKey;

        public int getCount() {
            return mappers.size();
        }

        public void setCount(int count) {
            this.count = count;
        }

        private int count;

        public List<Mapper> getMappers() {
            return mappers;
        }

        private final List<Mapper> mappers;

        public Contact(String type, int count, List<Mapper> mappers, String typeKey) {
            this.type = type;
            this.count = count;
            this.mappers=mappers;
            this.typeKey = typeKey;
        }
    }

}
