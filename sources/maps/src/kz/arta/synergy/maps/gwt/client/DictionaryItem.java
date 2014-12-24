package kz.arta.synergy.maps.gwt.client;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 08.05.14
 * Time: 12:54
 * To change this template use File | Settings | File Templates.
 */
public class DictionaryItem implements Serializable {
    /**
     * Ключ элемента справочника
     */
    private String key;
    /**
     * Значение элемента справочника
     */
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
