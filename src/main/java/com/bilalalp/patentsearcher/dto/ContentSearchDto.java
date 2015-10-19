package com.bilalalp.patentsearcher.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContentSearchDto {

    private Long totalLinkCount = 0L;

    private Long analysiedLinkCount = 0L;

    private Long notAnalysiedLinkCount = 0L;
}
