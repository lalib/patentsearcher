package com.bilalalp.patentsearcher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfigDto {

    private Boolean crawlAbstract = false;

    private Boolean crawlDescription = false;

    private Boolean crawlClaim = false;

    private WaitingEnum waitingEnum = WaitingEnum.ONE_SECOND;

    private CrawlingDto crawlingDto = new CrawlingDto();
}