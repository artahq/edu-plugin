package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.user.client.ui.Widget;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 21.12.14
 * Time: 22:55
 * To change this template use File | Settings | File Templates.
 */
public class FilterWidget {
    public final static int LIST_BOX_TYPE = 1;
    public final static int TEXT_BOX_TYPE = 2;
    Filter filter;
    Widget widget;
    String value;
    int type;
    public FilterWidget(Filter filter, Widget widget){
        this.filter=filter;
        this.widget=widget;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
