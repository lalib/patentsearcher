package com.bilalalp.patentsearcher.service.searchinfo;

import com.bilalalp.patentsearcher.dto.SearchingDto;
import com.bilalalp.patentsearcher.entity.SearchInfo;
import com.bilalalp.patentsearcher.service.base.BaseService;

public interface SearchInfoService extends BaseService<SearchInfo> {

    SearchInfo persistWithNewTransaction(SearchingDto searchingDto);
}