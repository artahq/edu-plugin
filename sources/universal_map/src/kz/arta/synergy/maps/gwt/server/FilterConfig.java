package kz.arta.synergy.maps.gwt.server;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 21.12.14
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */
public class FilterConfig {
    private String filterFieldKey;
    private String filterText;
    private String filterDictionaryKey;

    public FilterConfig(java.util.Properties properties, int number){
        number++;
        this.filterFieldKey = properties.getProperty("FILTER"+number+"_DATA_FIELD_KEY");
        this.filterText = properties.getProperty("FILTER"+number+"_TEXT");
        this.filterDictionaryKey = properties.getProperty("FILTER"+number+"_DICTIONARY_KEY");
    }
    public String getFilterFieldKey() {
        return filterFieldKey;
    }

    public void setFilterFieldKey(String filterFieldKey) {
        this.filterFieldKey = filterFieldKey;
    }

    public String getFilterText() {
        return filterText;
    }

    public void setFilterText(String filterText) {
        this.filterText = filterText;
    }

    public String getFilterDictionaryKey() {
        return filterDictionaryKey;
    }

    public void setFilterDictionaryKey(String filterDictionaryKey) {
        this.filterDictionaryKey = filterDictionaryKey;
    }

    public static FilterConfig[] LoadFilters(java.util.Properties properties){
        String strFiltersCount = properties.get("FILTERS_COUNT").toString();
        if(strFiltersCount!=null){
            int filtersCount = 0;
            try{
                filtersCount =  Integer.parseInt(strFiltersCount);
            }catch(Exception e){
                System.out.println("Filter count is not number");
            }
            FilterConfig[] filters = new FilterConfig[filtersCount];
            for(int i=0;i<filtersCount;i++){
                FilterConfig filterConfig = new FilterConfig(properties,i);
                filters[i]=filterConfig;
            }
            return filters;
        }
        return  null;
    }
}
