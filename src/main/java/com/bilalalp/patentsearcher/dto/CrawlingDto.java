package com.bilalalp.patentsearcher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrawlingDto {

    private Integer crawlCount = 0;

    private Integer totalCrawlingCount = 0;

    private Integer errorCount = 0;

    private Integer totalWaitTime = 0;

    private String currentLink;
}