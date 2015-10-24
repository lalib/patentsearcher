package com.bilalalp.patentsearcher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyseDto {

    private Long abstractCount = 0L;

    private Long claimCount = 0L;

    private Long descriptionCount = 0L;

    private Integer currentCount = 0;

    private ConfigDto configDto = new ConfigDto();

}