package kz.arta.synergy.maps.gwt.client;


import kz.arta.synergy.maps.gwt.server.Properties;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: alimjan
 * Date: 08.05.14
 * Time: 12:20
 * To change this template use File | Settings | File Templates.
 */
public class SportObject implements Serializable {
//    public SportObject(){
//        formUUID = Properties.FORM_UUID;
//    }
    /**
     * Идентификатор данных
     */
    private String dataUUID;
    /**
     * Версия0
     */
    private Long version;
    /**
     * Идентификатор формы
     */
    private String formUUID;
    /**
     * Версия формы
     */
    private Long formVersion;
    /**
     * Дата модификации
     */
    private String modified;
    /**
     * Юридическое имя оспортивного объекта
     */
    private String name;
    /**
     * Тип спортивного объекта(значение справочника)
     */
    private String typeValue;
    /**
     * Тип спортивного объекта(ключ справочника)
     */
    private String typeKey;
    /**
     * Место расположения спортивного объкта(город или область)
     */
    private String placeKey;

    public String getPlaceValue() {
        return placeValue;
    }

    public void setPlaceValue(String placeValue) {
        this.placeValue = placeValue;
    }

    public String getPlaceKey() {
        return placeKey;
    }

    public void setPlaceKey(String placeKey) {
        this.placeKey = placeKey;
    }

    private String placeValue;
    /**
     * Адрес спортивного объекта
     */

    private String address;
    /**
     * ФИО руководителя спортивного объекта
     */
    private String chiefFIO;
    /**
     * К какой федерации принадлежит объект (значение справочника)
     */
    private String federationValue;
    /**
     * К какой федерации принадлежит объект (ключ справочника)
     */
    private String federationKey;
    /**
     * Координата объкта на карте (широта)
     */
    private String coordX;
    /**
     * Координата объкта на карте (долгота)
     */
    private String coordY;
    private String a="привет";
    public String getTypeValue() {
        return typeValue;
    }

    public void setTypeValue(String typeValue) {
        this.typeValue = typeValue;
    }

    public String getDataUUID() {
        return dataUUID;
    }

    public void setDataUUID(String dataUUID) {
        this.dataUUID = dataUUID;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getFormUUID() {
        return formUUID;
    }

    public void setFormUUID(String formUUID) {
        this.formUUID = formUUID;
    }

    public Long getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(Long formVersion) {
        this.formVersion = formVersion;
    }

    public String getModified() {
        return modified;
    }

    public void setModified(String modified) {
        this.modified = modified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypeKey() {
//        return "1";
        return typeKey;
    }

    public void setTypeKey(String typeKey) {
        this.typeKey = typeKey;
    }



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChiefFIO() {
        return chiefFIO;
    }

    public void setChiefFIO(String chiefFIO) {
        this.chiefFIO = chiefFIO;
    }

    public String getFederationValue() {
        return federationValue;
    }

    public void setFederationValue(String federationValue) {
        this.federationValue = federationValue;
    }

    public String getFederationKey() {
        return federationKey;
    }

    public void setFederationKey(String federationKey) {
        this.federationKey = federationKey;
    }

    public String getCoordX() {
        return coordX;
    }

    public void setCoordX(String coordX) {
        this.coordX = coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public void setCoordY(String coordY) {
        this.coordY = coordY;
    }

    public String getAccrOrgKey() {
        return accrOrgKey;
    }

    public void setAccrOrgKey(String accrOrg) {
        this.accrOrgKey = accrOrg;
    }

    private String accrOrgKey;
    private String accrOrgValue;

    public String getAccrOrgValue() {
        return accrOrgValue;
    }

    public void setAccrOrgValue(String accrOrgValue) {
        this.accrOrgValue = accrOrgValue;
    }

    public String getImageId() {
//          return "3c645556-ac13-4c7f-9382-7d74b107b6f2";
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getRedirectUrl() {
//          return "3c645556-ac13-4c7f-9382-7d74b107b6f2";
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String imageId;
    public String redirectUrl;
    public String getImageUrl(){
        if(imageId!=null){
        return "../../Synergy/resource_downloader?identifier="+this.getImageId();
        }else return "http://amber.md/upload/no.jpg";

//        return "http://test6.arta.kz/Synergy/resource_downloader?identifier="+  this.getImageId();
//        String url1 = "http://jj-bg.info/gallery/album_1/min/test.png";
//        String url2 = "http://test6.arta.kz/Synergy/resource_downloader?identifier=3c645556-ac13-4c7f-9382-7d74b107b6f2";
//        String url3 = "http://tiandebeauty.com/wp-content/uploads/2012/06/testtiande.gif";
//        Random rand = new Random();
//        int a = rand.nextInt((3 - 1) + 1) + 1;
//        switch (a){
//            case 1: return url1;
//            case 2: return url2;
//            case 3: return url3;
//            default:return url1;
//        }
    }
    public String getRedirectedUrl(){
        if(redirectUrl!=null)
        return "../../Synergy/Synergy.html?"+this.redirectUrl;
        else return null;
    }
}
