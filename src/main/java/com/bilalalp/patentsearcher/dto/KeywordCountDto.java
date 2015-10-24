package com.bilalalp.patentsearcher.dto;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class KeywordCountDto {

    private SimpleLongProperty count;

    private SimpleStringProperty keyword;

    public KeywordCountDto(final Long count, final String keyword) {
        this.count = new SimpleLongProperty(count);
        this.keyword = new SimpleStringProperty(keyword);
    }

    public long getCount() {
        return count.get();
    }

    public SimpleLongProperty countProperty() {
        return count;
    }

    public void setCount(long count) {
        this.count.set(count);
    }

    public String getKeyword() {
        return keyword.get();
    }

    public SimpleStringProperty keywordProperty() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword.set(keyword);
    }
}