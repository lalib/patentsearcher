package com.bilalalp.patentsearcher.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class KeywordInfoDto {

    private final SimpleLongProperty id;

    private final SimpleStringProperty text;

    private Boolean selected = Boolean.FALSE;

    public KeywordInfoDto(Long id, String text) {
        this.id = new SimpleLongProperty(id);
        this.text = new SimpleStringProperty(text);
    }

    public long getId() {
        return id.get();
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}