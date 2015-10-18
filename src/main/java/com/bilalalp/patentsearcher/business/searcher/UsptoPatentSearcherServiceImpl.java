package com.bilalalp.patentsearcher.business.searcher;

import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class UsptoPatentSearcherServiceImpl implements PatentSearcherService {

    @Override
    public List<PatentInfo> getPatentInfoList(SearchingDto searchingDto, UIInfoDto uiInfoDto) throws IOException {
        return null;
    }

    @Override
    public String getSiteUrl() {
        return null;
    }

    @Override
    public String createSearchUrl(List<KeywordInfoDto> keywordInfoDtoList) {
        return null;
    }
}