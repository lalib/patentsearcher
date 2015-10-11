package com.bilalalp.patentsearcher.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class SiteInfoDto {

    private final SimpleLongProperty id;

    private final SimpleStringProperty siteName;

    private final SimpleStringProperty siteAddress;

    public SiteInfoDto(Long id, String siteName, String siteAddress) {
        this.id = new SimpleLongProperty(id);
        this.siteName = new SimpleStringProperty(siteName);
        this.siteAddress = new SimpleStringProperty(siteAddress);
    }

    public long getId() {
        return id.get();
    }

    public String getSiteAddress() {
        return siteAddress.get();
    }

    public String getSiteName() {
        return siteName.get();
    }
}