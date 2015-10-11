package com.bilalalp.patentsearcher.service.stopwordinfo;

import com.bilalalp.patentsearcher.dto.StopWordInfoDto;
import com.bilalalp.patentsearcher.entity.StopWordInfo;
import com.bilalalp.patentsearcher.service.base.BaseService;

public interface StopWordInfoService extends BaseService<StopWordInfo> {

    void update(StopWordInfoDto stopWordInfoDto);

    void persist(String wordText);

}
