package kz.arta.synergy.maps.gwt.server;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 08.05.14
 * Time: 13:41
 * To change this template use File | Settings | File Templates.
 */
public class SynergyDataException extends Exception {
    public SynergyDataException(String text)
    {
        super(text);
    }
    public SynergyDataException(String text,Exception innerEx)
    {
        super(text,innerEx);

    }
}
