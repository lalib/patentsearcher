package com.bilalalp.patentsearcher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UIInfoDto {

    private Integer totalRecordCount = 0;

    private Integer currentRecordCount = 0;

    private Integer errorCount = 0;

    private Integer totalWaitTime = 0;

    private String generatedLink;
}
