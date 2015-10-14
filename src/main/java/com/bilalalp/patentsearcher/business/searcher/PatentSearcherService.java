package com.bilalalp.patentsearcher.business.searcher;

import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;

import java.io.IOException;
import java.util.List;

public interface PatentSearcherService {

    List<PatentInfo> getPatentInfoList(List<KeywordInfoDto> keywordInfoDtoList, UIInfoDto uiInfoDto) throws IOException;

    String getSiteUrl();

    String createSearchUrl(List<KeywordInfoDto> keywordInfoDtoList);
}