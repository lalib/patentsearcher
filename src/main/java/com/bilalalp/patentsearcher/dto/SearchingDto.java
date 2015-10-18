package com.bilalalp.patentsearcher.dto;

import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.entity.SearchInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchingDto {

    private List<KeywordInfoDto> keywordInfoList;

    private SiteInfoDto siteInfoDto;

    private SearchInfo searchInfo;
}