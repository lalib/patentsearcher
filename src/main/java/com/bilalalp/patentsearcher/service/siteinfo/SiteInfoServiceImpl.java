package com.bilalalp.patentsearcher.service.siteinfo;

import com.bilalalp.patentsearcher.dao.siteinfo.SiteInfoDao;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.service.base.AbstractService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Setter
@Service
public class SiteInfoServiceImpl extends AbstractService<SiteInfo> implements SiteInfoService {

    private SiteInfoDao siteInfoDao;

    @Autowired
    public SiteInfoServiceImpl(SiteInfoDao siteInfoDao) {
        super(siteInfoDao);
        setSiteInfoDao(siteInfoDao);
    }

    @Override
    @Transactional
    public void persistIfNotExist(final List<SiteInfo> initialSiteInfoList) {

        for (final SiteInfo siteInfo : initialSiteInfoList) {
            final String siteKey = siteInfo.getSiteKey();

            final SiteInfo foundSiteInfo = siteInfoDao.getSiteInfoBySiteKey(siteKey);

            if (foundSiteInfo == null) {
                siteInfoDao.persist(siteInfo);
            }
        }
    }

    @Override
    @Transactional
    public SiteInfo findBySiteKey(String siteKey) {

        return siteInfoDao.getSiteInfoBySiteKey(siteKey);
    }
}
