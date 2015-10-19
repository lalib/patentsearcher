package com.bilalalp.patentsearcher.dao.patentinfo;

import com.bilalalp.patentsearcher.dao.base.Dao;
import com.bilalalp.patentsearcher.dto.ContentSearchDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;

public interface PatentInfoDao extends Dao<PatentInfo> {

    Long getTotalLinkCount(Long searchInfoId);

    Long getAnalysiedLinkCount(Long searchInfoId);

    Long getNotAnalysiedLinkCount(Long searchId);
}
