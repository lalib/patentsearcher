package com.bilalalp.patentsearcher.config;

import com.bilalalp.patentsearcher.entity.SiteInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class PatentSearcherInitialData {

    private PatentSearcherInitialData() {
        //Initializer Class
    }

    public static List<SiteInfo> getInitialSiteInfoList() {

        final List<SiteInfo> siteInfoList = new ArrayList<>();

        final SiteInfo patentScopeSite = new SiteInfo();
        patentScopeSite.setSiteAddres("https://patentscope.wipo.int/search/en/");
        patentScopeSite.setSiteKey("PATENT_SCOPE");
        patentScopeSite.setSiteName("Patent Scope");

        final SiteInfo usptoPatentSite = new SiteInfo();
        usptoPatentSite.setSiteAddres("http://patft.uspto.gov/netahtml/PTO/index.html");
        usptoPatentSite.setSiteName("Uspto");
        usptoPatentSite.setSiteKey("USPTO");

        siteInfoList.addAll(Arrays.asList(usptoPatentSite, patentScopeSite));
        return siteInfoList;
    }
}
