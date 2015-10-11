package com.bilalalp.patentsearcher.config;

import com.bilalalp.patentsearcher.service.siteinfo.SiteInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class PatentSearcherInitializer {

    @Autowired
    private SiteInfoService siteInfoService;

    @PostConstruct
    public void init() {

        siteInfoService.persistIfNotExist(PatentSearcherInitialData.getInitialSiteInfoList());
    }
}
