package kz.arta.synergy.maps.gwt.client;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 14.05.14
 * Time: 22:35
 * To change this template use File | Settings | File Templates.
 */
public class Labels implements Serializable {
    private String name;
    private String address;
    private String chief;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChief() {
        return chief;
    }

    public void setChief(String chief) {
        this.chief = chief;
    }
}
