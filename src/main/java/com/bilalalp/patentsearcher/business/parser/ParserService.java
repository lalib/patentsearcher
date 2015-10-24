package com.bilalalp.patentsearcher.business.parser;

import com.bilalalp.patentsearcher.config.PatentSearcherApplicationContextAware;
import com.bilalalp.patentsearcher.dto.ConfigDto;
import com.bilalalp.patentsearcher.dto.CrawlingDto;
import com.bilalalp.patentsearcher.dto.SearchInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.entity.SearchInfo;
import com.bilalalp.patentsearcher.service.searchinfo.SearchInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ParserService {

    @Autowired
    private PatentSearcherApplicationContextAware patentSearcherApplicationContextAware;

    @Autowired
    private SearchInfoService searchInfoService;

    @Transactional
    public void crawl(final SearchInfoDto searchInfoDto, final ConfigDto configDto) {

        final Map<String, PatentContentParserService> beansOfType = patentSearcherApplicationContextAware.getContext().getBeansOfType(PatentContentParserService.class);

        final SearchInfo searchInfo = searchInfoService.findById(searchInfoDto.getId());
        final List<PatentInfo> patentInfoList = searchInfo.getPatentInfoList();

        final CrawlingDto crawlingDto = configDto.getCrawlingDto();
        crawlingDto.setTotalCrawlingCount(patentInfoList.size());


        for (final PatentInfo patentInfo : patentInfoList) {

            crawlingDto.setCrawlCount(crawlingDto.getCrawlCount() + 1);
            for (final Map.Entry<String, PatentContentParserService> entry : beansOfType.entrySet()) {

                final PatentContentParserService patentContentParserService = entry.getValue();

                if (patentContentParserService.getSiteUrl().equals(patentInfo.getSiteInfo().getSiteAddres())) {
                    patentContentParserService.getPatentContents(patentInfo, configDto);
                }
            }
        }
    }
}