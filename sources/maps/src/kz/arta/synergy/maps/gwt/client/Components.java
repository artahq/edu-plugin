package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.datepicker.client.DateBox;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.maps.gwt.client.*;
import com.smartgwt.client.util.SC;
import kz.arta.synergy.maps.gwt.server.Configuration;
import kz.arta.synergy.maps.gwt.server.GetMarkersImpl;
import kz.arta.synergy.maps.gwt.server.ServerGetwayImpl;
import kz.arta.synergy.maps.gwt.server.SynergyDataException;

import java.util.*;

/**
 * Created by root on 27.01.14.
 */
public class Components {

    public static final String HEIGHT_FILTER_COMPONENTS = "25px";
    // компоненты для отображения информации об опоре
    private TextBox number;
    private TextBox comment;
    private TextBox address;

    public Image getPhoto() {
//        Window.alert(String.valueOf(listViewLayout.getAbsoluteTop()));
//        Window.alert(String.valueOf(Window.getClientHeight()));
        int a = Window.getClientHeight()- listViewLayout.getAbsoluteTop()-32-20;
//        int a = (listViewLayout.getOffsetHeight()*9)%10;
//        Window.alert(String.valueOf(a));
        photo.setHeight(a+"px");
        return photo;
    }

    public void setPhoto(Image photo) {
        this.photo = photo;
    }

    private Image photo;
    private VerticalPanel generalComponents;
    private MarkersAction markersAction;
    private HorizontalPanel horizontal2;
    private GoogleMap map;
    public HorizontalPanel listViewLayout;
    private ScrollPanel scrollPanel;
    public void redirect(String redirect_url){
//        Window.Location.replace(ClientProperties.REDERECT_FILE_URL);
//        redirectParentWindow(ClientProperties.REDERECT_FILE_URL);
        if(redirect_url!=null)
            redirectParentWindow(redirect_url);
        else{
            SC.say("Переход","Папка для перехода не задана!");
        }
    }
    public static native void redirectParentWindow(String location) /*-{
        top.location.href = location ;
    }-*/;
    public void setMap(GoogleMap map) {
        this.map = map;
    }

    public HorizontalPanel getHorizontal2() {
        return horizontal2;
    }

    public TextBox getNumber() {
        return number;
    }

    public void setNumber(TextBox number) {
        this.number = number;
    }

    public TextBox getComment() {
        return comment;
    }

    public void setComment(TextBox comment) {
        this.comment = comment;
    }

    public TextBox getAddress() {
        return address;
    }

    public void setAddress(TextBox address) {
        this.address = address;
    }

    public VerticalPanel getGeneralComponents() {
        return generalComponents;
    }

    public void setMarkersAction(MarkersAction markersAction) {
        this.markersAction = markersAction;
    }
    String sso_hash;
    public Components(String sso_hash) {
        number = new TextBox();
        comment = new TextBox();
        address = new TextBox();
        this.sso_hash = sso_hash;
        generalComponents = new VerticalPanel();
        createFilters();
    }

    // метод создания компонентов. при клике на опору отображаем в них информацию об опоре
    public void createComponent() {

        listViewLayout = new HorizontalPanel();
        listViewLayout.setSpacing(5);

        generalComponents.setVisible(false);
        generalComponents.setSpacing(5);
        generalComponents.setVisible(false);

        HorizontalPanel horizontal1 = new HorizontalPanel();
        horizontal2 = new HorizontalPanel();
        HorizontalPanel horizontal3 = new HorizontalPanel();
        HorizontalPanel horizontal4 = new HorizontalPanel();

        final Label num = new Label("Юридическое наименование спортивного объекта");
//        Label num = new Label(Configuration.LABEL_NAME);
        num.addStyleName("gwt-LabelTextColor");
        num.setWidth("500px");
        num.setWordWrap(false);
        final Label com = new Label("Юридический адрес спортивного объекта");
        com.setWidth("500px");
        com.addStyleName("gwt-LabelTextColor");
        final Label add = new Label("Ф.И.О Руководителя спортивного объекта, его контакты");
        add.addStyleName("gwt-LabelTextColor");
        add.setWidth("500px");
        Image image = new Image();
        image.setAltText("Фото");
//        image.setHeight("90%");
        image.setStyleName("so_photo");
        image.setHeight("120px");
//        image.setWidth("200px");
        image.setVisible(false);
        this.setPhoto(image);
//        final Button moreInfo = new Button("");

        ServerGetwayImpl.App.getInstance().getLabels(new AsyncCallback<Labels>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(Labels result) {
                num.setText(result.getName());
                com.setText(result.getAddress());
                add.setText(result.getChief());
            }
        });

        Label zon = new Label("Зона");
        zon.addStyleName("gwt-LabelTextColor");
        zon.setWidth("180px");

        number = new TextBox();
        number.setWidth("500px");
        number.setHeight("30px");
        number.setReadOnly(true);
        comment = new TextBox();
        comment.setWidth("500px");
        comment.setHeight("30px");
        comment.setReadOnly(true);
        address = new TextBox();
        address.setWidth("500px");
        address.setHeight("30px");
        address.setReadOnly(true);
        Button changeDatas = new Button("Сохранить");
        changeDatas.setWidth("110px");
        changeDatas.setHeight("30px");
        changeDatas.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {

            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent clickEvent) {
                GetMarkersImpl.App.getInstance().changeDataSupp(number.getText(),
                        comment.getText(),
                        address.getText(),
                        markersAction.getSelectedMarkerID(),
                        markersAction.getAllMarkers().get(markersAction.getSelectedMarkerID()).getSynergyID(),
                        new AsyncCallback<Boolean>() {

                            @Override
                            public void onFailure(Throwable caught) {
                                Window.alert(caught.getMessage());
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                if (result) {
                                    markersAction.getAllMarkers().get(markersAction.getSelectedMarkerID()).
                                            setNumber(number.getText());
                                    markersAction.getAllMarkers().get(markersAction.getSelectedMarkerID()).
                                            setAddress(address.getText());
                                    markersAction.getAllMarkers().get(markersAction.getSelectedMarkerID()).
                                            setComment(comment.getText());
                                    markersAction.refreshList();
                                }
                            }
                        });
            }
        });
        changeDatas.setVisible(false);
        Label link = new Label("Информация");
        link.addStyleName("gwt-LabelLink");
        link.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                getInfo();
            }
        });
        link.setVisible(false);
        horizontal1.add(num);
        horizontal1.add(number);
        horizontal1.add(changeDatas);
        horizontal1.add(link);

        horizontal2.add(com);
        horizontal2.add(comment);
        horizontal3.add(add);
        horizontal3.add(address);
        horizontal4.add(zon);
        VerticalPanel leftPanel = new VerticalPanel();
        leftPanel.add(horizontal1);
        leftPanel.add(horizontal2);
        leftPanel.add(horizontal3);
        HorizontalPanel rightPanel = new HorizontalPanel();
        rightPanel.add(image);
        HorizontalPanel hpanel = new HorizontalPanel();
        hpanel.add(leftPanel);
        hpanel.add(rightPanel);
        generalComponents.add(hpanel);

//        generalComponents.add(horizontal1);
//        generalComponents.add(horizontal2);
//        generalComponents.add(horizontal3);


//        generalComponents.add(horizontal4);


        listViewLayout.add(generalComponents);

        RootPanel.get("components").add(listViewLayout);
    }

    private void getInfo() {
        GetMarkersImpl.App.getInstance().getInfoProgramms(markersAction.getSelectedMarkerID(), new AsyncCallback<ArrayList<ItemInfo>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.toString());
            }

            @Override
            public void onSuccess(final ArrayList<ItemInfo> result) {
                if (result.size() == 0) {
                    Window.alert("Эта опора не состоит ни в одной из адресных программ");
                } else {
                    Grid grid = new Grid(result.size() + 1, 7);
                    grid.setVisible(true);
                    grid.setWidth("850px");
                    grid.setHeight("200px");

                    grid.clear();
                    grid.setText(0, 0, "№ договора");
                    grid.setText(0, 1, "Арендатор");
                    grid.setText(0, 2, "№ ад. программы");
                    grid.setText(0, 3, "Начало срока аренды");
                    grid.setText(0, 4, "Завершение срока аренды");
                    grid.setText(0, 5, "Установленное оборудование");
                    grid.setText(0, 6, "Подробнее");

                    grid.getCellFormatter().setStyleName(0, 0, "tableCell-odd");
                    grid.getCellFormatter().setStyleName(0, 1, "tableCell-odd");
                    grid.getCellFormatter().setStyleName(0, 2, "tableCell-odd");
                    grid.getCellFormatter().setStyleName(0, 3, "tableCell-odd");
                    grid.getCellFormatter().setStyleName(0, 4, "tableCell-odd");
                    grid.getCellFormatter().setStyleName(0, 5, "tableCell-odd");
                    grid.getCellFormatter().setStyleName(0, 6, "tableCell-odd");

                    for (int i = 0; i < result.size(); i++) {

                        final String synergyid = result.get(i).getSynergyid();
                        final String url = result.get(i).getUrl();

                        grid.setText(i + 1, 0, result.get(i).getContractNum());
                        grid.setText(i + 1, 1, result.get(i).getArendator());
                        grid.setText(i + 1, 2, result.get(i).getApNum());
                        grid.setText(i + 1, 3, result.get(i).getStartDate());
                        grid.setText(i + 1, 4, result.get(i).getFinishDate());
                        grid.setText(i + 1, 5, result.get(i).getOborud());

                        Label link = new Label("Подробнее");
                        link.setStyleName("gwt-LabelLinkTable");
                        link.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
                            @Override
                            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                                Window.open(url + "/?locale=ru&submodule=common&server_id=remote&action=open_document&document_identifier=" + synergyid, "_blank", "");
                            }
                        });

                        grid.setWidget(i + 1, 6, link);

                        grid.getCellFormatter().setStyleName(i + 1, 0, "tableCell-even");
                        grid.getCellFormatter().setStyleName(i + 1, 1, "tableCell-even");
                        grid.getCellFormatter().setStyleName(i + 1, 2, "tableCell-even");
                        grid.getCellFormatter().setStyleName(i + 1, 3, "tableCell-even");
                        grid.getCellFormatter().setStyleName(i + 1, 4, "tableCell-even");
                        grid.getCellFormatter().setStyleName(i + 1, 5, "tableCell-even");
                        grid.getCellFormatter().setStyleName(i + 1, 6, "tableCell-even");
                    }

                    DialogTable showTable = new DialogTable(grid);
                    showTable.center();
                    showTable.show();
                }
            }
        });
    }


    public static native int getBodyHeight() /*-{
        var h;
        h = $doc.body.clientHeight;
        return h;

    }-*/;


    public void setHeightScroll() {
//        int height = getBodyHeight()-RootPanel.get("components").getOffsetHeight()-scrollPanel.getAbsoluteTop()-50;
        int height = RootPanel.get("components").getAbsoluteTop()-scrollPanel.getAbsoluteTop()+RootPanel.get("components").getOffsetHeight();
//        Window.alert(String.valueOf(height));
        if(height<1)height=1;
        scrollPanel.setHeight(String.valueOf(height)+"px");

    }

    public class DialogTable extends DialogBox {

        public DialogTable(Grid g) {

            setText("Информация");
            setAnimationEnabled(true);
            setGlassEnabled(true);
            setModal(true);

            VerticalPanel panel = new VerticalPanel();
            panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

            Button b1 = new Button("Закрыть");
            b1.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
                @Override
                public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
                    removeFromParent();
                }
            });

            ScrollPanel showPanel = new ScrollPanel();
            showPanel.setHeight("250px");
            showPanel.add(g);

            panel.add(showPanel);
            panel.add(b1);
            setWidget(panel);
        }
    }

    public void createFilters() {

//        GetAllHints getAllHints = new GetAllHints();

        VerticalPanel generalPanel = new VerticalPanel();
        generalPanel.setSpacing(6);

        VerticalPanel verticalPanel = new VerticalPanel();
        verticalPanel.setSpacing(6);

        final Label lb_name = new Label("Наименование");
        lb_name.setStyleName("gwt-LabelFilter");

        Label lb_address = new Label("Адрес");
        lb_address.setStyleName("gwt-LabelFilter");
//        lb_address.setWordWrap(false);
//
//        Label support = new Label("ФИО руководителя");
//        support.setStyleName("gwt-LabelFilter");
        final SuggestBox tb_name = new SuggestBox(/*getAllHints.getContractsHints()*/);
        tb_name.setWidth("270px");
        tb_name.setHeight(HEIGHT_FILTER_COMPONENTS);
        tb_name.setStyleName("gwt-filterField");
        tb_name.setLimit(10);
//        contractTextBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//            @Override
//            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
//                searchDogovor(contractTextBox.getText());
//            }
//        });
        final SuggestBox tb_address = new SuggestBox(/*getAllHints.getProgramms()*/);
        tb_address.setWidth("270px");
        tb_address.setHeight(HEIGHT_FILTER_COMPONENTS);
        tb_address.setLimit(10);
//        programmTextBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//            @Override
//            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
//                searchAdressProgramm(programmTextBox.getText());
//            }
//        });
        Label lb_federation = new Label("Федерация");
        lb_federation.setStyleName("gwt-LabelFilter");

        final ListBox list_federations = new ListBox();
        list_federations.setWidth("270px");
        list_federations.setHeight(HEIGHT_FILTER_COMPONENTS);

            ServerGetwayImpl.App.getInstance().getFederations(sso_hash,new AsyncCallback<List<DictionaryItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(List<DictionaryItem> result) {
                    if(result!=null&&result.size()>0){
                        list_federations.addItem("","");
                        list_federations.setSelectedIndex(0);
                        for(DictionaryItem item:result)
                        list_federations.addItem(item.getValue(),item.getKey());
                    }
                }
            });


        Label lb_region = new Label("Регион");
        lb_region.setStyleName("gwt-LabelFilter");

        final ListBox list_region = new ListBox();
        list_region.setWidth("270px");
        list_region.setHeight(HEIGHT_FILTER_COMPONENTS);

            ServerGetwayImpl.App.getInstance().getPlases(new AsyncCallback<List<DictionaryItem>>() {
                @Override
                public void onFailure(Throwable caught) {
                    //To change body of implemented methods use File | Settings | File Templates.
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(List<DictionaryItem> result) {
                    if(result!=null&&result.size()>0){
                        list_region.addItem("","");
                        list_region.setSelectedIndex(0);
                        for(DictionaryItem item:result)
                            list_region.addItem(item.getValue(),item.getKey());
                    }
                }
            });

        Label lb_type = new Label("Тип СО");
        lb_type.setStyleName("gwt-LabelFilter");

        final ListBox list_type = new ListBox();
        list_type.setWidth("270px");
        list_type.setHeight(HEIGHT_FILTER_COMPONENTS);

        ServerGetwayImpl.App.getInstance().getTypes(new AsyncCallback<List<DictionaryItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(List<DictionaryItem> result) {
                if(result!=null&&result.size()>0){
                    list_type.addItem("","");
                    list_type.setSelectedIndex(0);
                    for(DictionaryItem item:result)
                        list_type.addItem(item.getValue(),item.getKey());
                }
            }
        });

        Label lb_accrorg = new Label("Аккредитация");
        lb_accrorg.setStyleName("gwt-LabelFilter");

        final ListBox list_accrorg = new ListBox();
        list_accrorg.setWidth("270px");
        list_accrorg.setHeight(HEIGHT_FILTER_COMPONENTS);

        ServerGetwayImpl.App.getInstance().getAccrOrgs(new AsyncCallback<List<DictionaryItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                //To change body of implemented methods use File | Settings | File Templates.
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(List<DictionaryItem> result) {
                if (result != null && result.size() > 0) {
                    list_accrorg.addItem("", "");
                    list_accrorg.setSelectedIndex(0);
                    for (DictionaryItem item : result)
                        list_accrorg.addItem(item.getValue(), item.getKey());
                }
            }
        });

        final CellTable<Contact> table2 = new CellTable<Contact>();
        table2.setWidth("250px");
        final ListDataProvider<Contact> dataProvider2 = new ListDataProvider<Contact>();
        dataProvider2.addDataDisplay(table2);
//        final SuggestBox supportTextBox = new SuggestBox(/*getAllHints.getSupportsHints()*/);
//        supportTextBox.setWidth("270px");
//        supportTextBox.setHeight(HEIGHT_FILTER_COMPONENTS);
//        supportTextBox.setLimit(10);
//        supportTextBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
//            @Override
//            public void onSelection(SelectionEvent<SuggestOracle.Suggestion> suggestionSelectionEvent) {
//                searchSupp(supportTextBox.getText());
//            }
//        });

//        DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");

//        final DateBox startDate = new DateBox();
//        startDate.setWidth("140px");
//        startDate.setHeight(HEIGHT_FILTER_COMPONENTS);
//        startDate.setEnabled(false);
//        startDate.setFormat(new DateBox.DefaultFormat(dateFormat));
//
//        final DateBox finishDate = new DateBox();
//        finishDate.setWidth("140px");
//        finishDate.setHeight(HEIGHT_FILTER_COMPONENTS);
//        finishDate.setEnabled(false);
//        finishDate.setFormat(new DateBox.DefaultFormat(dateFormat));

        final Button okSearch = new Button("Фильтровать");
        okSearch.setEnabled(true);
        okSearch.setHeight(HEIGHT_FILTER_COMPONENTS);
        okSearch.setStyleName("gwt-ButtonFilter");
        okSearch.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
//                Window.alert("Filter");

                List<Contact> contracts = new ArrayList<Contact>();
                List<Mapper> mappers = markersAction.getMappers();
                String name = tb_name.getText();
                if(name!=null&&name.length()==0)name=null;
//                Window.alert("name = "+name);
                String address = tb_address.getText();
                if(address!=null&&address.length()==0)address=null;
//                Window.alert("address = "+address);
                String federation_key = list_federations.getValue(list_federations.getSelectedIndex());
                if(federation_key!=null&&federation_key.length()==0)federation_key=null;
//                Window.alert("federation_key = "+federation_key);
                String place_key = list_region.getValue(list_region.getSelectedIndex());
                if(place_key!=null&&place_key.length()==0)place_key=null;
//                Window.alert("place_key = "+place_key);
                String type_key = list_type.getValue(list_type.getSelectedIndex());
                if(type_key!=null&&type_key.length()==0)type_key=null;
                String accrorg_key = list_accrorg.getValue(list_accrorg.getSelectedIndex());
                if(accrorg_key!=null&&accrorg_key.length()==0)accrorg_key=null;
//                Window.alert("type_key = "+type_key);
                for(Mapper mapper:mappers){
                    if((name==null||mapper.getSportObject().getName().indexOf(name)!=-1)&&
                            (address==null||mapper.getSportObject().getAddress().indexOf(address)!=-1)&&
                            (federation_key==null||mapper.getSportObject().getFederationKey().equals(federation_key))&&
                            (place_key==null||mapper.getSportObject().getPlaceKey().equals(place_key))&&
                            (type_key==null||mapper.getSportObject().getTypeKey().equals(type_key))&&
                            (accrorg_key==null||mapper.getSportObject().getAccrOrgKey().equals(accrorg_key))){
                        Contact c = new Contact(mapper.getSportObject().getName(),1,new ArrayList<Mapper>(),"");
//                        Window.alert("Find");
                        c.getMappers().add(mapper);
                        contracts.add(c);
                    }
                }
                if(contracts.size()>0){
                    dataProvider2.setList(contracts);
                    dataProvider2.refresh();
                    table2.setVisible(true);
//                    Window.alert("Table Visible");
                }else{
                    dataProvider2.setList(contracts);
                    dataProvider2.refresh();
                    Window.alert("Ничего не найдено!");
                }
            }
        });




        ///////////////
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


            }
        });
        table2.setSelectionModel(selectionModel2);

        TextColumn<Contact> soColumn = new TextColumn<Contact>() {
            @Override
            public String getValue(Contact contact) {
                return contact.type;
            }
        };
        soColumn.setCellStyleNames("so_types_th");
        table2.addColumn(soColumn);
        ////////////////

        verticalPanel.add(lb_name);
        verticalPanel.add(tb_name);
        verticalPanel.add(lb_address);
        verticalPanel.add(tb_address);
        verticalPanel.add(lb_federation);
        verticalPanel.add(list_federations);
        verticalPanel.add(lb_region);
        verticalPanel.add(list_region);
        verticalPanel.add(lb_type);
        verticalPanel.add(list_type);
        verticalPanel.add(lb_accrorg);
        verticalPanel.add(list_accrorg);
        verticalPanel.add(okSearch);
        table2.setVisible(false);
        //verticalPanel.add(table2);
//        verticalPanel.add(support);
//        verticalPanel.add(supportTextBox);
//        verticalPanel.add(checkBox);
//        verticalPanel.add(labelStartDate);
//        verticalPanel.add(startDate);
//        verticalPanel.add(labelFinishDate);
//        verticalPanel.add(hp);
        scrollPanel = new ScrollPanel();
        VerticalPanel panel3 = new VerticalPanel();
        panel3.setSpacing(6);
        panel3.add(table2);
        scrollPanel.add(panel3);

       // Window.alert(String.valueOf(Window.getClientHeight()-listViewLayout.getAbsoluteTop()-scrollPanel.getElement().getOffsetTop()));
//        scrollPanel.setHeight(String.valueOf(Window.getClientHeight()-listViewLayout.getAbsoluteTop()-scrollPanel.getElement().getOffsetTop())+"px");
        Window.addResizeHandler(new ResizeHandler() {

            public void onResize(ResizeEvent event) {
                //int height = event.getHeight();
//                scrollPanel.setHeight(String.valueOf(Window.getClientHeight()-listViewLayout.getOffsetHeight()-scrollPanel.getElement().getAbsoluteTop()));
                setHeightScroll();
            }
        });
        verticalPanel.add(scrollPanel);

        generalPanel.add(verticalPanel);

        RootPanel.get("filter").add(generalPanel);
    }

    private void searchDogovor(String searchText) {
        GetMarkers.App.getInstance().filterSupportsDogovors(searchText, new AsyncCallback<HashMap<String, Markers>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(HashMap<String, Markers> result) {
                setMarkerOnMapFiltered(result);
            }
        });
    }

    private void searchAdressProgramm(String searchText) {
        GetMarkers.App.getInstance().filterSupportsProgramms(searchText, new AsyncCallback<HashMap<String, Markers>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(HashMap<String, Markers> result) {
                setMarkerOnMapFiltered(result);
            }
        });
    }

    private void searchSupp(String searchText) {
        GetMarkersImpl.App.getInstance().searchSupports(searchText, new AsyncCallback<HashMap<String, Markers>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(HashMap<String, Markers> result) {
                setMarkerOnMapFiltered(result);
            }
        });
    }

    public void filterDatas(Date dat1, Date dat2) {
        GetMarkersImpl.App.getInstance().filterDatas(dat1, dat2, new AsyncCallback<HashMap<String, Markers>>() {
            @Override
            public void onFailure(Throwable throwable) {
                Window.alert(throwable.getMessage());
            }

            @Override
            public void onSuccess(HashMap<String, Markers> s) {
                if (s.size() != 0) {
                    setMarkerOnMapFiltered(s);
                } else {
                    Window.alert("Ничего не нашлось");
                }
            }
        });
    }

    private void setMarkerOnMapFiltered(HashMap<String, Markers> result) {

        if (result.size() != 0) {
            markersAction.setAllMarkers(result);
            markersAction.clearMap();
            clearClusters();
            Set set = result.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                Markers mar = (Markers) me.getValue();
                markersAction.setMarker(mar.getLat(), mar.getLng(), mar.getId(), mar.getType(), true);
            }
            zoomMap();
        } else {
            Window.alert("empty");
        }
    }

    public native void clearClusters() /*-{
        markerCluster.clearMarkers();
    }-*/;

    public void zoomMap() {
        LatLngBounds latLngBounds = map.getBounds();
        Marker[] markers = markersAction.getMarkers();
        for(int i = 0; i < markers.length; i++) {
            latLngBounds.extend(markers[i].getPosition());
        }
        map.fitBounds(latLngBounds);
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
