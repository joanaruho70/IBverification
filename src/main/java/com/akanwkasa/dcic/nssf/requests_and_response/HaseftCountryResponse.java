package com.akanwkasa.dcic.nssf.requests_and_response;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * Author: Akankwasa Brian
 * Date: 12/7/2020
 * Time: 3:28 PM
 * Contacts : Email = donbrians@ymail.com, Mobile 00256778693362
 */

public class HaseftCountryResponse implements Serializable {

    private Object travelStatus;
    private Object pointOfExit;
    private Object travelDate;
    private Object travelType;

    public Object getTravelType() {
        return travelType;
    }

    public void setTravelType(Object travelType) {
        this.travelType = travelType;
    }


    public Object getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(Object travelStatus) {
        this.travelStatus = travelStatus;
    }

    public Object getPointOfExit() {
        return pointOfExit;
    }

    public void setPointOfExit(Object pointOfExit) {
        this.pointOfExit = pointOfExit;
    }

    public Object getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Object travelDate) {
        this.travelDate = travelDate;
    }
}

