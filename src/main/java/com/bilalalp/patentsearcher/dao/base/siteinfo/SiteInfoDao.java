package com.bilalalp.patentsearcher.dao.base.siteinfo;

import com.bilalalp.patentsearcher.dao.base.Dao;
import com.bilalalp.patentsearcher.entity.SiteInfo;

public interface SiteInfoDao extends Dao<SiteInfo> {

    SiteInfo getSiteInfoBySiteKey(String siteKey);
}