package com.bilalalp.patentsearcher.dao.searchinfo;

import com.bilalalp.patentsearcher.dao.base.Dao;
import com.bilalalp.patentsearcher.dto.SearchInfoDto;
import com.bilalalp.patentsearcher.entity.SearchInfo;

import java.util.List;

public interface SearchInfoDao extends Dao<SearchInfo> {

    List findAllSearchInfos();
}