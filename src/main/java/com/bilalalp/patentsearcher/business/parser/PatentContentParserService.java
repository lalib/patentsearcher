package com.bilalalp.patentsearcher.business.parser;

import com.bilalalp.patentsearcher.dto.ConfigDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;

public interface PatentContentParserService {

    void getPatentContents(PatentInfo patentInfo, ConfigDto configDto);

    String getSiteUrl();
}