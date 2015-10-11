package com.bilalalp.patentsearcher.service.keywordinfo;

import com.bilalalp.patentsearcher.dto.KeywordInfoDto;
import com.bilalalp.patentsearcher.entity.KeywordInfo;
import com.bilalalp.patentsearcher.service.base.BaseService;

public interface KeywordInfoService extends BaseService<KeywordInfo> {

    void persist(String keywordText);

    void update(KeywordInfoDto keywordInfoDto);
}