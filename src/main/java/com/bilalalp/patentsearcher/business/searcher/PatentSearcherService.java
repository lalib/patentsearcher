package com.bilalalp.patentsearcher.business.searcher;

import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.entity.SiteInfo;

import java.io.IOException;
import java.util.List;

public interface PatentSearcherService {

    List<PatentInfo> getPatentInfoList(SearchingDto searchingDto, UIInfoDto uiInfoDto) throws IOException;

    String getSiteUrl();

    String createSearchUrl(List<KeywordInfoDto> keywordInfoDtoList);
}