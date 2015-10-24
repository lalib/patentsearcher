package com.bilalalp.patentsearcher.business.parser;

import com.bilalalp.patentsearcher.config.PatentSearcherInitialData;
import com.bilalalp.patentsearcher.dto.ConfigDto;
import com.bilalalp.patentsearcher.entity.ContentSearchInfoStatusType;
import com.bilalalp.patentsearcher.entity.PatentInfo;
import com.bilalalp.patentsearcher.entity.SiteInfo;
import com.bilalalp.patentsearcher.service.patentinfo.PatentInfoService;
import com.bilalalp.patentsearcher.service.siteinfo.SiteInfoService;
import com.bilalalp.patentsearcher.util.JSoupUtil;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
public class PatentScopeContentParserServiceImpl implements PatentContentParserService {

    @Autowired
    private SiteInfoService siteInfoService;

    @Autowired
    private PatentInfoService patentInfoService;

    private SiteInfo siteInfo;

    @PostConstruct
    public void init() {
        siteInfo = siteInfoService.findBySiteKey(PatentSearcherInitialData.getPatentScopeSiteInfo().getSiteKey());
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void getPatentContents(final PatentInfo patentInfo, final ConfigDto configDto) {

        final PatentInfo tempPatentInfo = patentInfoService.findById(patentInfo.getId());
        final String patentLink = tempPatentInfo.getPatentLink();
        configDto.getCrawlingDto().setCurrentLink(patentLink);

        final Element body = JSoupUtil.getBody(patentLink, configDto.getWaitingEnum(),configDto.getCrawlingDto());

        if (body != null) {
            boolean anyChange = false;

            if (Boolean.TRUE.equals(configDto.getCrawlAbstract())) {
                final String abstractContent = getAbstractContent(body);
                if (StringUtils.isNotEmpty(abstractContent)) {
                    tempPatentInfo.setAbstractContent(abstractContent);
                    anyChange = true;
                }
            }

            if (anyChange) {
                tempPatentInfo.setContentSearchInfoStatusType(ContentSearchInfoStatusType.ANALYSIED);
                patentInfoService.update(tempPatentInfo);
            }
        }
    }

    @Override
    public String getSiteUrl() {
        return siteInfo.getSiteAddres();
    }

    private String getAbstractContent(final Element body) {

        final Elements aClass = getaClass(body);

        if (aClass != null && aClass.size() != 0) {

            final Element spanElement = getSpanElement(aClass);
            if (spanElement != null) {
                return spanElement.text();
            }
        }

        return null;
    }

    private Elements getaClass(Element body) {
        final Elements aClass = body.getElementsByAttributeValue("class", "trans-section NPabstract");

        if (aClass != null && !aClass.isEmpty()) {
            return aClass;
        }

        final Elements aClass1 = body.getElementsByAttributeValue("class", "PCTabstract trans-section");

        if (aClass1 != null && !aClass1.isEmpty()) {
            return aClass1;
        }

        return aClass;
    }

    private Element getSpanElement(Elements spanElements) {

        for (Element element : spanElements) {
            if (element.attr("lang", "en") != null) {
                return element;
            }
        }
        return null;
    }
}
