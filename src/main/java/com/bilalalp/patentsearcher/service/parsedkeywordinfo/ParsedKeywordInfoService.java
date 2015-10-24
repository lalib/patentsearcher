package com.bilalalp.patentsearcher.service.parsedkeywordinfo;

import com.bilalalp.patentsearcher.dto.KeywordCountDto;
import com.bilalalp.patentsearcher.entity.ContentType;
import com.bilalalp.patentsearcher.entity.ParsedKeywordInfo;
import com.bilalalp.patentsearcher.service.base.BaseService;

import java.util.List;

public interface ParsedKeywordInfoService extends BaseService<ParsedKeywordInfo> {

    void removeAllAbstractKeywordsWithNewTransaction(Long patentInfoId);

    void persistWithNewTransaction(List<ParsedKeywordInfo> parsedKeywordInfoList);

    List<KeywordCountDto> getKeywordCountDto(final List<Long> searchInfoIdList, final List<String> keywordInfoIdList, final List<ContentType> contentTypeList);

}