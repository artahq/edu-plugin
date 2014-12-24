package kz.arta.synergy.maps.gwt.client;

import com.google.gwt.user.client.ui.Widget;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 21.12.14
 * Time: 21:34
 * To change this template use File | Settings | File Templates.
 */
public class Filter implements Serializable {
    String caption;
    List<DictionaryItem> dictionary = new ArrayList<DictionaryItem>();
    String fieldId;

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public List<DictionaryItem> getDictionary() {
        return dictionary;
    }

    public void setDictionary(List<DictionaryItem> dictionary) {
        this.dictionary = dictionary;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }
}
