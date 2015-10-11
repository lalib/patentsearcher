package com.bilalalp.patentsearcher.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class KeywordDto {

    private final SimpleLongProperty id;

    private final SimpleStringProperty text;

    public KeywordDto(Long id, String text) {
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
}