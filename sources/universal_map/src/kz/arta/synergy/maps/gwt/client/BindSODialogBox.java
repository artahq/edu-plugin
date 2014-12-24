package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.smartgwt.client.util.SC;
import kz.arta.synergy.maps.gwt.server.ServerGetwayImpl;


import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 10.05.14
 * Time: 17:10
 * To change this template use File | Settings | File Templates.
 */
public class BindSODialogBox extends DialogBox {
    List<SportObject> soList;
    public String selecetedId;
    public BindSODialogBox(List<SportObject> sportsObjects, final double lat, final double lng,final MarkersAction markersAction){
        this.soList = sportsObjects;
        setText("Привязка объкта к карте");
        setAnimationEnabled(true);
        setGlassEnabled(true);
        setWidth("500px");
        Label label = new Label("Выберите объект:");
        label.addStyleName("gwt-LabelTextColor");
        final ListBox soBox = new ListBox(false);
        for(int i=0;i<sportsObjects.size();i++ ) {
            if(sportsObjects.get(i).getCoordX()==null&&sportsObjects.get(i).getCoordY()==null){
                soBox.addItem(sportsObjects.get(i).getTypeValue()+" \""+sportsObjects.get(i).getName()+"\"",sportsObjects.get(i).getDataUUID());
            }
        }
        Button ok = new Button("Сохранить");
        ok.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event) {
//                if(soBox.getSelectedIndex()==0)     {
//                    SC.say("Для сохранения необходимо выбрать спортивный объект из списка");
//                    return;
//                }
                selecetedId = soBox.getValue(soBox.getSelectedIndex());
//                System.out.println(selecetedId);
                final SportObject sportObject = Utils.findSOById(soList,selecetedId);
                if(sportObject!=null){
                    sportObject.setCoordX(String.valueOf(lat));
                    sportObject.setCoordY(String.valueOf(lng));
                    try {
                        ServerGetwayImpl.App.getInstance().SaveSportObject(sportObject, new AsyncCallback<Void>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                SC.say("Сохранение","Привязку не удалось сохранить!");
                            }

                            @Override
                            public void onSuccess(Void result) {
                                //To change body of implemented methods use File | Settings | File Templates.
                                markersAction.setMarker(lat,lng,sportObject.getTypeValue()+" \""+sportObject.getName()+"\"",0,true,sportObject);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        SC.say("Сохранение","Привязку не удалось сохранить!");
                    }


                }

                hide();
            }
        });
        Button cancel = new Button("Отмена");
        cancel.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
            @Override
            public void onClick(com.google.gwt.event.dom.client.ClickEvent event){
               hide();
            }
        });
        VerticalPanel panel = new VerticalPanel();
        panel.setHeight("200px");
        panel.setWidth("500px");
        panel.setSpacing(5);
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        panel.add(label);
        panel.add(soBox);
        HorizontalPanel hpanel = new HorizontalPanel();

        hpanel.add(ok);
        hpanel.add(cancel);
        panel.add(hpanel);
        setWidget(panel);

    }
}
