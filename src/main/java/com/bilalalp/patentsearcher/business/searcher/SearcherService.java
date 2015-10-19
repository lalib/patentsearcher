package com.bilalalp.patentsearcher.business.searcher;

import com.bilalalp.patentsearcher.config.PatentSearcherApplicationContextAware;
import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.dto.UIInfoDto;
import com.bilalalp.patentsearcher.entity.SearchInfo;
import com.bilalalp.patentsearcher.entity.SearchInfoStatusType;
import com.bilalalp.patentsearcher.service.searchinfo.SearchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

@Service
public class SearcherService {

    @Autowired
    private PatentSearcherApplicationContextAware patentSearcherApplicationContextAware;

    @Autowired
    private SearchInfoService searchInfoService;

    @Transactional
    public void search(SearchingDto searchingDto, UIInfoDto uiInfoDto) throws IOException {

        final Map<String, PatentSearcherService> beansOfType = patentSearcherApplicationContextAware.getContext().getBeansOfType(PatentSearcherService.class);

        SearchInfo searchInfo = searchInfoService.persistWithNewTransaction(searchingDto);
        searchingDto.setSearchInfo(searchInfo);

        final Date startTime = new Date();
        for (Map.Entry<String, PatentSearcherService> serviceEntry : beansOfType.entrySet()) {
            final PatentSearcherService patentSearcherService = serviceEntry.getValue();

            if (patentSearcherService.getSiteUrl() != null && patentSearcherService.getSiteUrl().equals(searchingDto.getSiteInfoDto().getSiteAddress())) {
                patentSearcherService.getPatentInfoList(searchingDto, uiInfoDto);
            }
        }

        final Date endTime = new Date();

        searchInfo = searchInfoService.findById(searchInfo.getId());
        searchInfo.setSearchInfoStatusType(SearchInfoStatusType.FINISHED);
        searchInfo.setStartTime(startTime);
        searchInfo.setEndTime(endTime);

        searchInfoService.update(searchInfo);
    }
}