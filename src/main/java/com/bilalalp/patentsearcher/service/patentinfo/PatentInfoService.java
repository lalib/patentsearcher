package com.bilalalp.patentsearcher.service.patentinfo;

import com.bilalalp.patentsearcher.dto.ContentSearchDto;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.service.base.BaseService;

import java.util.List;

public interface PatentInfoService extends BaseService<PatentInfo> {

    void persistWithNewTransaction(PatentInfo patentInfo);

    ContentSearchDto getContentSearchDto(Long searchInfoId);

    List<PatentInfo> getPatentInfoListBySearchInfoId(Long searchInfoId);
}