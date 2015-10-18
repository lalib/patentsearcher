package com.bilalalp.patentsearcher.service.siteinfo;

import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.service.base.BaseService;

import java.util.List;

public interface SiteInfoService extends BaseService<SiteInfo> {

    void persistIfNotExist(List<SiteInfo> initialSiteInfoList);

    SiteInfo findBySiteKey(String siteName);
}
