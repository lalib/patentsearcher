package com.bilalalp.patentsearcher.dao.patentinfo;

import com.bilalalp.patentsearcher.dao.base.Dao;
import com.bilalalp.patentsearcher.dto.ContentSearchDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;

import java.util.List;

public interface PatentInfoDao extends Dao<PatentInfo> {

    Long getTotalLinkCount(Long searchInfoId);

    Long getAnalysiedLinkCount(Long searchInfoId);

    Long getNotAnalysiedLinkCount(Long searchId);

    List<PatentInfo> getPatentInfoListBySearchInfoId(Long searchInfoId);

    Long getAbstractCount(Long searchInfoId);

    Long getAbstractCount(List<Long> searchIdList);

    List<PatentInfo> getPatentInfoList(List<Long> searchInfoIdList);

}
