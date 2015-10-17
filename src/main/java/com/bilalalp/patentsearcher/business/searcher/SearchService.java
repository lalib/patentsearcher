package com.bilalalp.patentsearcher.business.searcher;

import com.bilalalp.patentsearcher.config.PatentSearcherApplicationContextAware;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class SearchService {

    @Autowired
    private PatentSearcherApplicationContextAware patentSearcherApplicationContextAware;

    public void search(SearchingDto searchingDto, UIInfoDto uiInfoDto) throws IOException {

        final Map<String, PatentSearcherService> beansOfType = patentSearcherApplicationContextAware.getContext().getBeansOfType(PatentSearcherService.class);

        for (Map.Entry<String, PatentSearcherService> serviceEntry : beansOfType.entrySet()) {
            final PatentSearcherService value = serviceEntry.getValue();

            if (value.getSiteUrl().equals(searchingDto.getSiteInfoDto().getSiteAddress())) {
                final List<PatentInfo> patentInfoList = value.getPatentInfoList(searchingDto.getKeywordInfoList(), uiInfoDto);


            }
        }
    }
}