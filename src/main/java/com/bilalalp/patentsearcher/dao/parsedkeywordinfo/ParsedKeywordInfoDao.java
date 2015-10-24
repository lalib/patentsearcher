package com.bilalalp.patentsearcher.dao.parsedkeywordinfo;

import com.bilalalp.patentsearcher.dao.base.Dao;
import com.bilalalp.patentsearcher.dto.KeywordCountDto;
import com.bilalalp.patentsearcher.entity.ContentType;
import com.bilalalp.patentsearcher.entity.ParsedKeywordInfo;

import java.util.List;

public interface ParsedKeywordInfoDao extends Dao<ParsedKeywordInfo> {

    void removeAllAbstractKeywords(Long patentInfoId);

    List<KeywordCountDto> getKeywordCountDto(final List<Long> searchInfoIdList, final List<String> stopWordInfoIdList,final List<ContentType> contentTypeList);
}
