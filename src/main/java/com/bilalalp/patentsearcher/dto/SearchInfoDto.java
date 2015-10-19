package com.bilalalp.patentsearcher.dto;

import com.bilalalp.patentsearcher.entity.SearchInfoStatusType;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Date;

public class SearchInfoDto {

    private final SimpleLongProperty id;

    private final SimpleStringProperty state;

    private final SimpleStringProperty searchDate;

    private final SimpleLongProperty linkCount;

    private final SimpleLongProperty totalTime;

    public SearchInfoDto(Long id, SearchInfoStatusType state, String searchDate, Long linkCount, Long totalTime) {
        this.id = new SimpleLongProperty(id);
        this.state = new SimpleStringProperty(state.toString());
        this.searchDate = new SimpleStringProperty(searchDate);
        this.linkCount = new SimpleLongProperty(linkCount);
        this.totalTime = new SimpleLongProperty(totalTime);
    }

    public SearchInfoDto(Long id, SearchInfoStatusType state, Date searchDate, Long linkCount, Date startTime, Date endTime) {
        this(id, state, "", linkCount, 0L);
    }

    public long getLinkCount() {
        return linkCount.get();
    }

    public String getSearchDate() {
        return searchDate.get();
    }


    public String getState() {
        return state.get();
    }

    public long getId() {
        return id.get();
    }

    public long getTotalTime() {
        return totalTime.get();
    }
}