package com.bilalalp.patentsearcher.service.keyword;

import com.bilalalp.patentsearcher.dto.KeywordDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.service.base.BaseService;

public interface KeywordInfoService extends BaseService<KeywordInfo> {

    void persist(String keywordText);

    void update(KeywordDto keywordDto);
}