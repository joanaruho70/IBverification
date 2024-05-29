package com.akanwkasa.dcic.nssf.requests_and_response;

import java.io.Serializable;
import java.sql.Blob;

//finger print images of the foreighner
public class FingerImages implements Serializable {
    String fingerType;
    String fingerImage;
    int fingerPrintQuality=0;

    public String getFingerType() {
        return fingerType;
    }

    public void setFingerType(String fingerType) {
        this.fingerType = fingerType;
    }

    public String getFingerImage() {
        return fingerImage;
    }

    public void setFingerImage(String fingerImage) {
        this.fingerImage = fingerImage;
    }

    public int getFingerPrintQuality() {
        return fingerPrintQuality;
    }

    public void setFingerPrintQuality(int fingerPrintQuality) {
        this.fingerPrintQuality = fingerPrintQuality;
    }
}
