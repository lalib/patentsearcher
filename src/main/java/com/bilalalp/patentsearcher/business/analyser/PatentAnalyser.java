package com.bilalalp.patentsearcher.business.analyser;

import com.bilalalp.patentsearcher.dto.AnalyseDto;
import com.bilalalp.patentsearcher.dto.ConfigDto;
import com.bilalalp.patentsearcher.dto.SearchInfoDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.service.patentinfo.PatentInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PatentAnalyser {

    @Autowired
    private PatentInfoService patentInfoService;

    @Transactional
    public void analyse(final List<SearchInfoDto> searchInfoDtoList, final AnalyseDto analyseDto) {

        final List<Long> searchInfoIdList = searchInfoDtoList.stream().map(SearchInfoDto::getId).collect(Collectors.toList());

        final List<PatentInfo> patentInfoList = patentInfoService.getPatentInfoList(searchInfoIdList);

        for (final PatentInfo patentInfo : patentInfoList) {
            analyseDto.setCurrentCount(analyseDto.getCurrentCount() + 1);

            boolean change = false;

            final String abstractContent = patentInfo.getAbstractContent();
            final ConfigDto configDto = analyseDto.getConfigDto();

            if (Boolean.TRUE.equals(configDto.getCrawlAbstract()) && StringUtils.isNotEmpty(abstractContent)) {

                final List<String> analysedAbstractContentList = analyse(abstractContent);
                patentInfo.setAbstractContentWordList(analysedAbstractContentList);
                change = true;
            }

            if (change) {
                patentInfoService.update(patentInfo);
            }
        }
    }

    public List<String> analyse(final String content) {

        final List<String> stringList = new ArrayList<>();
        stringList.addAll(analyseSingleContent(content));
        stringList.addAll(analysePairContent(content));

        return stringList;
    }

    private List<String> analyseSingleContent(final String content) {

        final String replacedString = clearString(content);

        final String[] splittedString = replacedString.split(" ");
        return Arrays.asList(splittedString);
    }

    private List<String> analysePairContent(final String content) {
        return Collections.emptyList();
    }

    private String clearString(final String content) {

        return content.toLowerCase()
                .replaceAll("\\.", " ")
                .replaceAll(",", " ")
                .replaceAll("\\?", " ")
                .replaceAll("!", " ")
                .replaceAll("  ", " ").trim();
    }
}