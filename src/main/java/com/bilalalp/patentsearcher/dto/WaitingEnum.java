package com.bilalalp.patentsearcher.dto;

public enum WaitingEnum {

    ONE_SECOND(1000),

    FIVE_SECONDS(5000),

    TEN_SECONDS(10000),

    TWENTY_SECONDS(20000),

    HALF_MINUTE(30000),

    ONE_MINUTE(60000);

    private Integer time;

    WaitingEnum(int time) {
        this.time = time;
    }

    public WaitingEnum getNext() {

        return values().length == ordinal() ? values()[0] : values()[ordinal() + 1];
    }

    public Integer getTime() {
        return time;
    }
}
